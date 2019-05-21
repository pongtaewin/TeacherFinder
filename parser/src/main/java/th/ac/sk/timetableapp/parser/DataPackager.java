package th.ac.sk.timetableapp.parser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;

class DataPackager {
    static String pack(String prefix, String data) {
        JsonObject obj = new JsonObject();
        obj.addProperty("p", prefix);
        obj.add("d", new JsonParser().parse(data));
        obj.addProperty("t", Calendar.getInstance().getTimeInMillis());
        return new Gson().toJson(obj);
    }
}
