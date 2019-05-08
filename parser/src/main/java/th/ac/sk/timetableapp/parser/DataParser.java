package th.ac.sk.timetableapp.parser;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.HashMap;
import java.util.Objects;

import th.ac.sk.timetableapp.model.Period;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;

@SuppressLint("UseSparseArrays")
public class DataParser {

    ///////////////////////////////////////////////////
    //                    PERIOD                     //
    ///////////////////////////////////////////////////
    public static HashMap<Integer, Period> parsePeriod(String data) {
        try {
            if (data == null) return null;
            return parsePeriod(Objects.requireNonNull(new Gson().fromJson(data, JsonArray.class)));
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static HashMap<Integer, Period> parsePeriod(@NonNull JsonArray dataAsJson) throws JsonParseException {
        HashMap<Integer, Period> result = new HashMap<>();
        for (JsonElement jsonElement : dataAsJson) {
            JsonObject obj = jsonElement.getAsJsonObject().getAsJsonObject("data");
            int key = obj.get("key").getAsInt();
            Period period = new Gson().fromJson(obj.get("period"), Period.class);
            result.put(key, period);
        }
        return result;
    }

    public static String extractPeriod(HashMap<Integer, Period> data) {
        try {
            JsonArray jsonArray = new JsonArray();
            for (HashMap.Entry<Integer, Period> periodHash : data.entrySet()) {
                JsonObject obj = new JsonObject();
                JsonObject innerObj = new JsonObject();
                innerObj.addProperty("key", periodHash.getKey());
                innerObj.add("period", new JsonParser().parse(new Gson().toJson(periodHash.getValue())));
                obj.add("data", innerObj);
                jsonArray.add(obj);
            }
            return new Gson().toJson(jsonArray);
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
    ///////////////////////////////////////////////////
    //               TEACHER LOCATION                //
    ///////////////////////////////////////////////////

    public static TeacherLocationDatabaseFormat parseTeacherLocation(String data) {
        try {
            if (data == null) return null;
            return parseTeacherLocation(new Gson().fromJson(data, JsonObject.class));
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static TeacherLocationDatabaseFormat parseTeacherLocation(@NonNull JsonObject dataAsJson) throws JsonParseException {
        JsonArray teacherLocationJson = dataAsJson.getAsJsonArray("teacherLocation");
        JsonArray teacherDetailJson = dataAsJson.getAsJsonArray("teacherDetail");
        return new TeacherLocationDatabaseFormat(
                parseTeacherLocationFromJson(teacherLocationJson),
                parseTeacherDetailFromJson(teacherDetailJson));
    }

    public static String extractTeacherLocation(HashMap<Integer, HashMap<Integer, TeacherLocation>> location, HashMap<Integer, TeacherDetail> detail) {
        try {
            return extractTeacherLocation(parseTeacherLocationToJson(location), parseTeacherDetailToJson(detail));
        } catch (JsonParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String extractTeacherLocation(JsonArray locationJson, JsonArray teacherDetailJson) {
        JsonObject result = new JsonObject();
        result.add("teacherLocation", locationJson);
        result.add("teacherDetail", teacherDetailJson);
        return extractTeacherLocation(result);
    }

    private static String extractTeacherLocation(JsonObject dataAsJson) {
        return new Gson().toJson(dataAsJson);
    }

    private static HashMap<Integer, TeacherDetail> parseTeacherDetailFromJson(@NonNull JsonArray teacherDetailJson) {
        HashMap<Integer, TeacherDetail> result = new HashMap<>();
        for (JsonElement element : teacherDetailJson) {
            JsonObject object = element.getAsJsonObject();
            int key = object.get("key").getAsInt();
            TeacherDetail value = new Gson().fromJson(object.get("value"), TeacherDetail.class);
            result.put(key, value);
        }
        return result;
    }

    private static HashMap<Integer, HashMap<Integer, TeacherLocation>> parseTeacherLocationFromJson(@NonNull JsonArray teacherLocationJson) {
        HashMap<Integer, HashMap<Integer, TeacherLocation>> result = new HashMap<>();
        for (JsonElement element : teacherLocationJson) {
            JsonObject object = element.getAsJsonObject();
            int key = object.get("key").getAsInt();
            JsonArray innerTeacherLocationJson = object.getAsJsonArray("value");
            HashMap<Integer, TeacherLocation> innerResult = new HashMap<>();
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

    private static JsonArray parseTeacherDetailToJson(@NonNull HashMap<Integer, TeacherDetail> detail) {
        JsonArray result = new JsonArray();
        for (HashMap.Entry<Integer, TeacherDetail> detailHash : detail.entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("key", detailHash.getKey());
            object.add("value", new JsonParser().parse(new Gson().toJson(detailHash.getValue())));
            result.add(object);
        }
        return result;
    }

    private static JsonArray parseTeacherLocationToJson(@NonNull HashMap<Integer, HashMap<Integer, TeacherLocation>> location) {
        JsonArray result = new JsonArray();
        for (HashMap.Entry<Integer, HashMap<Integer, TeacherLocation>> locationHash : location.entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("key", locationHash.getKey());
            HashMap<Integer, TeacherLocation> innerLocation = locationHash.getValue();
            JsonArray innerResult = new JsonArray();
            for (HashMap.Entry<Integer, TeacherLocation> innerLocationHash : innerLocation.entrySet()) {
                JsonObject innerObject = new JsonObject();
                innerObject.addProperty("key", innerLocationHash.getKey());
                innerObject.add("value", new JsonParser().parse(new Gson().toJson(innerLocationHash.getValue())));
                innerResult.add(innerObject);
            }
            object.add("value", innerResult);
            result.add(object);
        }
        return result;
    }

    ///////////////////////////////////////////////////
    //                PACKED  STRING                 //
    ///////////////////////////////////////////////////

    public static String packString(String period, String teacherLocation) {
        JsonObject object = new JsonObject();
        object.add("period", new JsonParser().parse(period));
        object.add("teacherLocation", new JsonParser().parse(teacherLocation));
        return new Gson().toJson(object);
    }

    public static String extractPeriodFromPackedString(String data) {
        JsonObject object = new JsonParser().parse(data).getAsJsonObject();
        return new Gson().toJson(object.getAsJsonArray("period"));
    }

    public static String extractTeacherLocationFromPackedString(String data) {
        JsonObject object = new JsonParser().parse(data).getAsJsonObject();
        return new Gson().toJson(object.getAsJsonObject("teacherLocation"));
    }

    public static class TeacherLocationDatabaseFormat {
        private HashMap<Integer, HashMap<Integer, TeacherLocation>> teacherLocation;
        private HashMap<Integer, TeacherDetail> teacherDetail;

        TeacherLocationDatabaseFormat(HashMap<Integer, HashMap<Integer, TeacherLocation>> teacherLocation, HashMap<Integer, TeacherDetail> teacherDetail) {
            this.setTeacherLocation(teacherLocation);
            this.setTeacherDetail(teacherDetail);
        }

        public HashMap<Integer, HashMap<Integer, TeacherLocation>> getTeacherLocation() {
            return teacherLocation;
        }

        private void setTeacherLocation(HashMap<Integer, HashMap<Integer, TeacherLocation>> teacherLocation) {
            this.teacherLocation = teacherLocation;
        }

        public HashMap<Integer, TeacherDetail> getTeacherDetail() {
            return teacherDetail;
        }

        private void setTeacherDetail(HashMap<Integer, TeacherDetail> teacherDetail) {
            this.teacherDetail = teacherDetail;
        }
    }

}