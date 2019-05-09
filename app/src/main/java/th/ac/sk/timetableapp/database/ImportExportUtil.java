package th.ac.sk.timetableapp.database;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;

import th.ac.sk.timetableapp.parser.DataParser;

public abstract class ImportExportUtil {
    private static final String prefix = "ข้อมูลอย่างไม่เป็นทางการ" + System.lineSeparator() + "สร้างจากอุปกรณ์ " + Build.MODEL;

    public static Intent importData(String input) {
        JsonObject obj = new JsonParser().parse(input).getAsJsonObject();
        String periodData = DataParser.extractPeriodFromPackedString(new Gson().toJson(obj.get("d")));
        String teacherLocationData = DataParser.extractTeacherLocationFromPackedString(new Gson().toJson(obj.get("d")));
        if (!DataSaveHandler.importPeriodData(periodData, false)
                || !DataSaveHandler.importTeacherLocationData(teacherLocationData, false))
            return null;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("p", obj.get("p").getAsString());
        intent.putExtra("d", new Gson().toJson(obj.get("d")));
        intent.putExtra("t", obj.get("t").getAsLong());
        return intent;
    }

    private static String exportData() {
        return DataParser.packString(DataSaveHandler.getPeriod(), DataSaveHandler.getTeacherLocation());
    }

    public static void shareData(Context context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("p", prefix);
        obj.add("d", new JsonParser().parse(exportData()));
        obj.addProperty("t", Calendar.getInstance().getTimeInMillis());
        Log.w("teacherFinderShareData", new Gson().toJson(obj));
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(obj));
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, "ส่งข้อมูลไปยัง..."));
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
