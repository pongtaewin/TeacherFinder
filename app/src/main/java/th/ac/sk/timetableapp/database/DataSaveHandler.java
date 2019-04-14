package th.ac.sk.timetableapp.database;

import android.util.SparseArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.annotation.NonNull;
import th.ac.sk.timetableapp.datamodel.Period;
import th.ac.sk.timetableapp.datamodel.TeacherDetail;
import th.ac.sk.timetableapp.datamodel.TeacherLocation;
import th.ac.sk.timetableapp.util.StaticUtil;


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

    private static void savePeriod(SparseArray<Period> data) {
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < data.size(); i++) {
            JsonObject obj = new JsonObject();
            JsonObject innerObj = new JsonObject();
            innerObj.addProperty("key", i);
            innerObj.add("period", new JsonParser().parse(new Gson().toJson(data.valueAt(i))));
            obj.add("data", innerObj);
            jsonArray.add(obj);
        }
        savePeriod(new Gson().toJson(jsonArray));

    }

    private static void savePeriod(String saveData) {
        FileIO.save(saveData, DATA_PERIOD);
    }

    private static SparseArray<Period> loadPeriod() {
        String saveFile = loadPeriodIO();
        SparseArray<Period> result = null;
        try {
            result = loadPeriod(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static SparseArray<Period> loadPeriod(String data) {
        if (data == null) return null;
        return loadPeriod(new Gson().fromJson(String.valueOf(data), JsonArray.class));
    }

    private static SparseArray<Period> loadPeriod(JsonArray dataAsJson) {
        SparseArray<Period> result = new SparseArray<>();
        for (JsonElement jsonElement : dataAsJson) {
            JsonObject obj = jsonElement.getAsJsonObject().getAsJsonObject("data");
            int key = obj.get("key").getAsInt();
            Period period = new Gson().fromJson(obj.get("period"), Period.class);
            result.put(key, period);
        }
        return result;
    }

    public static void loadCurrentPeriodData() {
        SparseArray<Period> loadedData = loadPeriod();

        if (loadedData != null) PeriodDatabase.getInstance().putToCurrentData(loadedData);
        else PeriodDatabase.getInstance().updateToNullPeriod();
    }

    public static void saveCurrentPeriodData() {
        savePeriod(PeriodDatabase.getInstance().getCurrentData());
    }

    public static String loadPeriodIO() {
        return FileIO.load(DATA_PERIOD);
    }

    public static boolean importPeriodData(String saveData, boolean check) {
        boolean success = false;
        try {
            success = PeriodDatabase.checkIfCorrectlyImported(loadPeriod(saveData));
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

    private static TeacherLocationDatabase.format loadTeacherLocation() {
        String saveFile = loadTeacherLocationIO();
        TeacherLocationDatabase.format result = null;
        try {
            result = loadTeacherLocation(saveFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static TeacherLocationDatabase.format loadTeacherLocation(String data) {
        if (data == null) return null;
        return loadTeacherLocation(new Gson().fromJson(String.valueOf(data), JsonObject.class));
    }

    private static TeacherLocationDatabase.format loadTeacherLocation(@NonNull JsonObject dataAsJson) {
        JsonArray teacherLocationJson = dataAsJson.getAsJsonArray("teacherLocation");
        JsonArray teacherDetailJson = dataAsJson.getAsJsonArray("teacherDetail");
        return new TeacherLocationDatabase.format(
                parseTeacherLocationFromJson(teacherLocationJson),
                parseTeacherDetailFromJson(teacherDetailJson));
    }

    private static void saveTeacherLocation(SparseArray<SparseArray<TeacherLocation>> location, SparseArray<TeacherDetail> detail) {
        saveTeacherLocation(parseTeacherLocationToJson(location), parseTeacherDetailToJson(detail));
    }

    private static void saveTeacherLocation(JsonArray locationJson, JsonArray teacherDetailJson) {
        JsonObject result = new JsonObject();
        result.add("teacherLocation", locationJson);
        result.add("teacherDetail", teacherDetailJson);
        saveTeacherLocation(result);
    }

    private static void saveTeacherLocation(JsonObject dataAsJson) {
        saveTeacherLocation(new Gson().toJson(dataAsJson));
    }

    private static void saveTeacherLocation(String saveData) {
        FileIO.save(saveData, DATA_TEACHER_LOCATION);
    }

    private static SparseArray<TeacherDetail> parseTeacherDetailFromJson(@NonNull JsonArray teacherDetailJson) {
        SparseArray<TeacherDetail> result = new SparseArray<>();
        for (JsonElement element : teacherDetailJson) {
            JsonObject object = element.getAsJsonObject();
            int key = object.get("key").getAsInt();
            TeacherDetail value = new Gson().fromJson(object.get("value"), TeacherDetail.class);
            result.put(key, value);
        }
        return result;
    }

    private static SparseArray<SparseArray<TeacherLocation>> parseTeacherLocationFromJson(@NonNull JsonArray teacherLocationJson) {
        SparseArray<SparseArray<TeacherLocation>> result = new SparseArray<>();
        for (JsonElement element : teacherLocationJson) {
            JsonObject object = element.getAsJsonObject();
            int key = object.get("key").getAsInt();
            JsonArray innerTeacherLocationJson = object.getAsJsonArray("value");
            SparseArray<TeacherLocation> innerResult = new SparseArray<>();
            for (JsonElement innerElement : innerTeacherLocationJson) {
                JsonObject innerObject = innerElement.getAsJsonObject();
                int innerKey = innerObject.get("key").getAsInt();
                TeacherLocation value = new Gson().fromJson(innerObject.get("value"), TeacherLocation.class);
                innerResult.put(innerKey, value);
            }
            result.put(key, innerResult);
        }
        return result;
    }

    private static JsonArray parseTeacherDetailToJson(@NonNull SparseArray<TeacherDetail> detail) {
        JsonArray result = new JsonArray();
        for (int i = 0; i < detail.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("key", detail.keyAt(i));
            object.add("value", new JsonParser().parse(new Gson().toJson(detail.valueAt(i))));
            result.add(object);
        }
        return result;
    }

    private static JsonArray parseTeacherLocationToJson(@NonNull SparseArray<SparseArray<TeacherLocation>> location) {
        JsonArray result = new JsonArray();
        for (int i = 0; i < location.size(); i++) {
            JsonObject object = new JsonObject();
            object.addProperty("key", location.keyAt(i));
            SparseArray<TeacherLocation> innerLocation = location.valueAt(i);
            JsonArray innerResult = new JsonArray();
            for (int j = 0; j < innerLocation.size(); j++) {
                //Log.w("innerLocation","InnerLocation = "+innerLocation.size()+" at i = "+j);
                JsonObject innerObject = new JsonObject();
                innerObject.addProperty("key", innerLocation.keyAt(j));
                innerObject.add("value", new JsonParser().parse(new Gson().toJson(innerLocation.valueAt(j))));
                innerResult.add(innerObject);
            }
            object.add("value", innerResult);
            result.add(object);
        }
        return result;
    }

    public static void loadCurrentTeacherLocationData() {
        TeacherLocationDatabase.format loadedData = loadTeacherLocation();
        if (loadedData != null) {
            if (loadedData.teacherDetail != null)
                TeacherLocationDatabase.getInstance().putDetail(loadedData.teacherDetail);
            if (loadedData.teacherLocation != null)
                TeacherLocationDatabase.getInstance().putLocation(loadedData.teacherLocation);
        }
    }

    public static void saveCurrentTeacherLocationData() {
        saveTeacherLocation(TeacherLocationDatabase.getInstance().getLocation(), TeacherLocationDatabase.getInstance().getDetail());
    }


    public static String loadTeacherLocationIO() {
        return FileIO.load(DATA_TEACHER_LOCATION);
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

    static class FileIO {
        static void save(String saveData, String path) {
            StaticUtil.preferences.edit()
                    .putString(path, saveData)
                    .apply();
        }

        static String load(String path) {
            return StaticUtil.preferences.getString(path, "");
        }
    }
}