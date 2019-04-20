package th.ac.sk.timetableapp.database;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class ImportExportUtil {
    public static boolean importData(String data) {
        try {
            JsonObject object = (JsonObject) new JsonParser().parse(data);
            String periodData = new Gson().toJson(object.get("period"));
            String teacherLocationData = new Gson().toJson(object.get("teacherLocation"));
            boolean success = DataSaveHandler.importPeriodData(periodData, false)
                    && DataSaveHandler.importTeacherLocationData(teacherLocationData, false);
            if (success) {
                DataSaveHandler.importPeriodData(periodData, true);
                DataSaveHandler.importTeacherLocationData(teacherLocationData, true);
            }
            return success;
        } catch (Exception e) {
            return false;
        }
    }

    private static String exportData() {
        JsonObject object = new JsonObject();
        object.add("period", new JsonParser().parse(DataSaveHandler.loadPeriodIO()));
        object.add("teacherLocation", new JsonParser().parse(DataSaveHandler.loadTeacherLocationIO()));
        return new Gson().toJson(object);
    }

    public static void shareData(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, exportData());
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "ส่งออกไปยัง..."));
    }

    public static void wipeData() {
        PeriodDatabase.getInstance().updateToNullPeriod();
        TeacherLocationDatabase.getInstance().updateToNullLocation();
        TeacherLocationDatabase.getInstance().updateToNullDetail();
        DataSaveHandler.saveCurrentPeriodData();
        DataSaveHandler.saveCurrentTeacherLocationData();
    }

    public static void rollbackToPlaceholderData() {
        PeriodDatabase.getInstance().updateToPlaceholderPeriod();
        TeacherLocationDatabase.getInstance().updateToPlaceholderLocation();
        TeacherLocationDatabase.getInstance().updateToPlaceholderDetail();
        DataSaveHandler.saveCurrentPeriodData();
        DataSaveHandler.saveCurrentTeacherLocationData();
    }
}
