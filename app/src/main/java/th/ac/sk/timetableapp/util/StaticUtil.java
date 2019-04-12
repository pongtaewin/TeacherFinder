package th.ac.sk.timetableapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;

import java.io.File;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.display.SplashScreenActivity;

public class StaticUtil {

    public static SharedPreferences preferences;
    public static StaticUtil ourInstance;
    public static Resources resources;
    public static File filesDir;
    public static Typeface kanit;
    public static Typeface kanitMedium;
    public static Typeface kanitSemiBold;

    public static StaticUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new StaticUtil();
        return ourInstance;
    }

    public static void showSplashScreen(Context context) {
        context.startActivity(new Intent(context, SplashScreenActivity.class));
    }

    public void applyContext(Context context) {
        resources = context.getResources();
        filesDir = context.getFilesDir();
        kanit = ResourcesCompat.getFont(context, R.font.kanit);
        kanitMedium = ResourcesCompat.getFont(context, R.font.kanit_medium);
        kanitSemiBold = ResourcesCompat.getFont(context, R.font.kanit_semibold);
        preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
    }

    @NonNull
    public static NavController getNavControllerFromHost(@NonNull Object host){
        return ((NavHostFragment)host).getNavController();
    }
}
