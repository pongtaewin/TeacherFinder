package th.ac.sk.timetableapp.parser;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

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
            JsonObject obj = jsonElement.getAsJsonObject().getAsJsonObject("d");
            int key = obj.get("k").getAsInt();
            Period period = Period.fromJson(obj.getAsJsonObject("p"));
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
                innerObj.addProperty("k", periodHash.getKey());
                innerObj.add("p", periodHash.getValue().toJson());
                obj.add("d", innerObj);
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
        JsonArray teacherLocationJson = dataAsJson.getAsJsonArray("l");
        JsonArray teacherDetailJson = dataAsJson.getAsJsonArray("d");
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

    private static String extractTeacherLocation(JsonArray locationJson, JsonArray detailJson) {
        JsonObject result = new JsonObject();
        result.add("l", locationJson);
        result.add("d", detailJson);
        return extractTeacherLocation(result);
    }

    private static String extractTeacherLocation(JsonObject dataAsJson) {
        return new Gson().toJson(dataAsJson);
    }

    private static HashMap<Integer, TeacherDetail> parseTeacherDetailFromJson(@NonNull JsonArray teacherDetailJson) {
        HashMap<Integer, TeacherDetail> result = new HashMap<>();
        for (JsonElement element : teacherDetailJson) {
            JsonObject object = element.getAsJsonObject();
            int key = object.get("k").getAsInt();
            TeacherDetail value = TeacherDetail.fromJson(object.getAsJsonObject("v"));
            result.put(key, value);
        }
        return result;
    }

    private static HashMap<Integer, HashMap<Integer, TeacherLocation>> parseTeacherLocationFromJson(@NonNull JsonArray teacherLocationJson) {
        HashMap<Integer, HashMap<Integer, TeacherLocation>> result = new HashMap<>();
        for (JsonElement element : teacherLocationJson) {
            JsonObject object = element.getAsJsonObject();
            int key = object.get("k").getAsInt();
            JsonArray innerTeacherLocationJson = object.getAsJsonArray("v");
            HashMap<Integer, TeacherLocation> innerResult = new HashMap<>();
            for (JsonElement innerElement : innerTeacherLocationJson) {
                JsonObject innerObject = innerElement.getAsJsonObject();
                int innerKey = innerObject.get("k").getAsInt();
                TeacherLocation value = TeacherLocation.fromJson(innerObject.getAsJsonObject("v"));
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
            object.addProperty("k", detailHash.getKey());
            object.add("v", detailHash.getValue().toJson());
            result.add(object);
        }
        return result;
    }

    private static JsonArray parseTeacherLocationToJson(@NonNull HashMap<Integer, HashMap<Integer, TeacherLocation>> location) {
        JsonArray result = new JsonArray();
        for (HashMap.Entry<Integer, HashMap<Integer, TeacherLocation>> locationHash : location.entrySet()) {
            JsonObject object = new JsonObject();
            object.addProperty("k", locationHash.getKey());
            HashMap<Integer, TeacherLocation> innerLocation = locationHash.getValue();
            JsonArray innerResult = new JsonArray();
            for (HashMap.Entry<Integer, TeacherLocation> innerLocationHash : innerLocation.entrySet()) {
                JsonObject innerObject = new JsonObject();
                innerObject.addProperty("k", innerLocationHash.getKey());
                innerObject.add("v", innerLocationHash.getValue().toJson());
                innerResult.add(innerObject);
            }
            object.add("v", innerResult);
            result.add(object);
        }
        return result;
    }

    ///////////////////////////////////////////////////
    //                PACKED  STRING                 //
    ///////////////////////////////////////////////////

    public static String packString(String period, String teacherLocation) {
        JsonObject object = new JsonObject();
        object.add("p", new JsonParser().parse(period));
        object.add("t", new JsonParser().parse(teacherLocation));
        return new Gson().toJson(object);
    }

    public static String extractPeriodFromPackedString(String data) {
        JsonObject object = new JsonParser().parse(data).getAsJsonObject();
        return new Gson().toJson(object.getAsJsonArray("p"));
    }

    public static String extractTeacherLocationFromPackedString(String data) {
        JsonObject object = new JsonParser().parse(data).getAsJsonObject();
        return new Gson().toJson(object.getAsJsonObject("t"));
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