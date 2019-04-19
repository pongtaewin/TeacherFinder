package th.ac.sk.timetableapp.tool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import java.io.File;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.SplashScreenActivity;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.model.TeacherDetail;

public class StaticUtil {

    public static SharedPreferences preferences;
    public static StaticUtil ourInstance;
    public static Resources resources;
    public static File filesDir;

    public static StaticUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new StaticUtil();
        return ourInstance;
    }

    public static void showSplashScreen(Context context) {
        context.startActivity(new Intent(context, SplashScreenActivity.class));
    }

    @Nullable
    public static String getTitleText(@IdRes int id, Bundle arguments) {
        try {
            switch (id) {
                case R.id.settingsFragment:
                    return "การตั้งค่า";
                case R.id.mainFragment:
                    return "หน้าหลัก";
                case R.id.dayDisplayFragment:
                    return "เลือกวันที่";
                case R.id.periodDisplayFragment:
                    int day = arguments.getInt("day");
                    return "วัน" + new String[]{"จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์"}[day - 1];
                case R.id.teacherLocationDisplayFragment:
                    int key = arguments.getInt("key");
                    return "วัน" + new String[]{"จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์"}[key / 10] + " คาบ " + String.valueOf((key % 10) + 1);
                case R.id.modifyClassroomFragment:
                    return "แก้ไขตารางสอน";
                case R.id.modifyTeacherLocationChooserFragment:
                    return "รายชื่อครู";
                case R.id.modifyTeacherLocationEditorFragment:
                    TeacherDetail detail = TeacherLocationDatabase.getInstance().getDetail(arguments.getInt("teacherId"));
                    return String.format("อ. %s %s", detail.name, detail.surname);
                default:
                    return "แอปพลิเคชัน";
            }
        } catch (NullPointerException e) {
            Log.w("StaticUtil", "NPE caught at getTitleText()");
            Log.w("StaticUtil", "Returning null.");
            return null;
        }
    }

    @Nullable
    public static String getSubtitleText(@IdRes int id) {
        switch (id) {
            case R.id.periodDisplayFragment:
                return "ตารางสอน";
            case R.id.teacherLocationDisplayFragment:
                return "สถานที่สอน";
            case R.id.modifyTeacherLocationEditorFragment:
                return "แก้ไขสถานที่สอน";
            default:
                return null;
        }
    }

    public void applyContext(Context context) {
        resources = context.getResources();
        filesDir = context.getFilesDir();
        preferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
    }

}
