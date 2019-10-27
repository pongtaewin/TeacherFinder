package th.ac.sk.timetableapp.database;

import android.util.Log;

import java.util.HashMap;

import th.ac.sk.timetableapp.model.Period;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.parser.DataParser;
import th.ac.sk.timetableapp.tool.StaticUtil;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class DataSaveHandler {
    private static final String DATA_SETTINGS = "settings";

    private static DataSaveHandler ourInstance;

    public static void getInstance() {
        if (ourInstance == null) {
            ourInstance = new DataSaveHandler();
        }
    }

    ///////////////////////////////////////////////////
    //                    PERIOD                     //
    ///////////////////////////////////////////////////

    private static void savePeriod(HashMap<Integer, Period> data) {
        savePeriod(DataParser.extractPeriod(data));
    }

    private static void savePeriod(String saveData) {
        SharedPreferencesHelper.setString(saveData, "periodData");
    }

    private static HashMap<Integer, Period> loadPeriod() {
        return DataParser.parsePeriod(getPeriod());
    }

    public static String getPeriod() {
        return SharedPreferencesHelper.getString("periodData");
    }

    private static HashMap<Integer, Period> loadPeriod(String data) {
        return DataParser.parsePeriod(data);
    }

    private static void loadCurrentPeriodData() {
        HashMap<Integer, Period> loadedData = loadPeriod();

        if (loadedData != null) PeriodDatabase.getInstance().putPeriodHash(loadedData);
        else PeriodDatabase.getInstance().updateToNullPeriod();
    }

    public static void saveCurrentPeriodData() {
        savePeriod(PeriodDatabase.getInstance().getPeriodHash());
    }

    public static boolean importPeriodData(String saveData, boolean apply) {
        boolean success = false;
        try {
            success = loadPeriod(saveData).size() == 50;
            if (apply) {
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

    private static void saveTeacherLocation(HashMap<Integer, HashMap<Integer, TeacherLocation>> location, HashMap<Integer, TeacherDetail> detail) {
        saveTeacherLocation(DataParser.extractTeacherLocation(location, detail));
    }

    private static void saveTeacherLocation(String saveData) {
        SharedPreferencesHelper.setString(saveData, "teacherLocation");
    }

    private static void loadCurrentTeacherLocationData() {
        DataParser.TeacherLocationDatabaseFormat loadedData = loadTeacherLocation();
        if (loadedData != null) {
            if (loadedData.getTeacherDetail() != null)
                TeacherLocationDatabase.getInstance().putDetailHash(loadedData.getTeacherDetail());
            if (loadedData.getTeacherLocation() != null)
                TeacherLocationDatabase.getInstance().putLocationHash(loadedData.getTeacherLocation());
        }
    }

    public static void saveCurrentTeacherLocationData() {
        saveTeacherLocation(TeacherLocationDatabase.getInstance().getLocationHash(), TeacherLocationDatabase.getInstance().getDetailHash());
    }


    public static String getTeacherLocation() {
        return SharedPreferencesHelper.getString("teacherLocation");
    }

    public static boolean importTeacherLocationData(String saveData, boolean apply) {
        boolean success = false;
        try {
            success = TeacherLocationDatabase.checkIfCorrectlyImported(loadTeacherLocation(saveData));
            if (apply) {
                saveTeacherLocation(saveData);
                loadTeacherLocation();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    ///////////////////////////////////////////////////
    //                    SETTINGS                   //
    ///////////////////////////////////////////////////

    private static void loadCurrentSettingsData() {

    }

    public static void saveCurrentSettingsData() {

    }

    public static void loadMaster() {
        loadCurrentPeriodData();
        loadCurrentTeacherLocationData();
        loadCurrentSettingsData();
    }

    public static void saveMaster() {
        saveCurrentPeriodData();
        saveCurrentTeacherLocationData();
        saveCurrentSettingsData();
    }

    public static class SharedPreferencesHelper {
        public static void setString(String saveData, String path) {
            StaticUtil.preferences.edit()
                    .putString(path, saveData)
                    .apply();
        }
        public static void setInt(int saveData, String path) {
            StaticUtil.preferences.edit()
                    .putInt(path, saveData)
                    .apply();
        }
        public static void setBoolean(boolean saveData, String path) {
            StaticUtil.preferences.edit()
                    .putBoolean(path, saveData)
                    .apply();
            Log.w("DSH", "setBoolean(hide) set to: " + saveData);
        }
        public static String getString(String path) {
            return StaticUtil.preferences.getString(path, "");
        }
        public static int getInt(String path) {
            return StaticUtil.preferences.getInt(path, 0);
        }
        public static boolean getBoolean(String path) {
            boolean res = StaticUtil.preferences.getBoolean(path, false);
            Log.w("DSH", "getBoolean(hide) returned: " + res);
            return res;
        }
    }
}