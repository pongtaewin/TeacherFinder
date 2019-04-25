package th.ac.sk.timetableapp.database;

import androidx.annotation.NonNull;

import java.util.HashMap;

import th.ac.sk.timetableapp.model.Period;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.parser.DataParser;
import th.ac.sk.timetableapp.tool.StaticUtil;


public class DataSaveHandler {
    private static final String DATA_TEACHER_LOCATION = "teacherLocation";
    private static final String DATA_PERIOD = "periodData";
    private static DataSaveHandler ourInstance;

    @NonNull
    public static DataSaveHandler getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataSaveHandler();
        }
        return ourInstance;
    }

    ///////////////////////////////////////////////////
    //                    PERIOD                     //
    ///////////////////////////////////////////////////

    private static void savePeriod(HashMap<Integer,Period> data) {
        savePeriod(DataParser.extractPeriod(data));
    }

    private static void savePeriod(String saveData) {
        SharedPreferencesHelper.set(saveData, DATA_PERIOD);
    }

    private static HashMap<Integer,Period> loadPeriod() {
        return DataParser.parsePeriod(getPeriod());
    }

    public static String getPeriod() {
        return SharedPreferencesHelper.get(DATA_PERIOD);
    }

    private static HashMap<Integer,Period> loadPeriod(String data) {
        return DataParser.parsePeriod(data);
    }

    public static void loadCurrentPeriodData() {
        HashMap<Integer,Period> loadedData = loadPeriod();

        if (loadedData != null) PeriodDatabase.getInstance().putPeriod(loadedData);
        else PeriodDatabase.getInstance().updateToNullPeriod();
    }

    public static void saveCurrentPeriodData() {
        savePeriod(PeriodDatabase.getInstance().getPeriod());
    }

    public static boolean importPeriodData(String saveData, boolean check) {
        boolean success = false;
        try {
            success = loadPeriod(saveData).size() == 50;
            if (check) {
                savePeriod(saveData);
                loadCurrentPeriodData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    ///////////////////////////////////////////////////
    //               TEACHER LOCATION                //
    ///////////////////////////////////////////////////

    private static DataParser.TeacherLocationDatabaseFormat loadTeacherLocation() {
        String saveFile = getTeacherLocation();
        DataParser.TeacherLocationDatabaseFormat result = null;
        try {
            result = loadTeacherLocation(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static DataParser.TeacherLocationDatabaseFormat loadTeacherLocation(String data) {
        return DataParser.parseTeacherLocation(data);
    }

    private static void saveTeacherLocation(HashMap<Integer,HashMap<Integer,TeacherLocation>> location, HashMap<Integer,TeacherDetail> detail) {
        saveTeacherLocation(DataParser.extractTeacherLocation(location, detail));
    }

    private static void saveTeacherLocation(String saveData) {
        SharedPreferencesHelper.set(saveData, DATA_TEACHER_LOCATION);
    }
    
    public static void loadCurrentTeacherLocationData() {
        DataParser.TeacherLocationDatabaseFormat loadedData = loadTeacherLocation();
        if (loadedData != null) {
            if (loadedData.getTeacherDetail() != null)
                TeacherLocationDatabase.getInstance().putDetail(loadedData.getTeacherDetail());
            if (loadedData.getTeacherLocation() != null)
                TeacherLocationDatabase.getInstance().putLocation(loadedData.getTeacherLocation());
        }
    }

    public static void saveCurrentTeacherLocationData() {
        saveTeacherLocation(TeacherLocationDatabase.getInstance().getLocation(), TeacherLocationDatabase.getInstance().getDetail());
    }


    public static String getTeacherLocation() {
        return SharedPreferencesHelper.get(DATA_TEACHER_LOCATION);
    }

    public static boolean importTeacherLocationData(String saveData, boolean check) {
        boolean success = false;
        try {
            success = TeacherLocationDatabase.checkIfCorrectlyImported(loadTeacherLocation(saveData));
            if (check) {
                saveTeacherLocation(saveData);
                loadTeacherLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public static void loadMaster() {
        loadCurrentPeriodData();
        loadCurrentTeacherLocationData();
    }

    public static void saveMaster() {
        saveCurrentPeriodData();
        saveCurrentTeacherLocationData();
    }

    static class SharedPreferencesHelper {
        static void set(String saveData, String path) {
            StaticUtil.preferences.edit()
                    .putString(path, saveData)
                    .apply();
        }

        static String get(String path) {
            return StaticUtil.preferences.getString(path, "");
        }
    }
}