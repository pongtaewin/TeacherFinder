package th.ac.sk.timetableapp.database;

import android.content.Context;
import android.content.Intent;

import th.ac.sk.timetableapp.parser.DataParser;

public abstract class ImportExportUtil {
    public static boolean importData(String data) {
        try {
            String periodData = DataParser.extractPeriodFromPackedString(data);
            String teacherLocationData = DataParser.extractTeacherLocationFromPackedString(data);
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
        return DataParser.packString(DataSaveHandler.getPeriod(),DataSaveHandler.getTeacherLocation());
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
