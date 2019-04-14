package th.ac.sk.timetableapp;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.util.StaticUtil;

public class MasterActivity extends AppCompatActivity {

    public static final String SPLASH_SCREEN_ALREADY = "splashScreenAlready";
    public static final String TAG_SCREEN = "screenTitle";
    public static final String SCREEN_MAIN = "หน้าหลัก";
    public static final String SCREEN_DAY_DISPLAY = "เลือกวันที่";
    public static final String SCREEN_MODIFY_TEACHER_LOCATION_CHOOSER = "รายชื่อครู";
    public static final String HIDE_SETTINGS = "hideSettings";
    public static final String SCREEN_SETTINGS = "การตั้งค่า";
    public static final String SCREEN_MODIFY_CLASSROOM = "แก้ไขตารางสอน";
    public static boolean hideSettings = false;
    public NavController navController;

    NavController.OnDestinationChangedListener listener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            Objects.requireNonNull(getSupportActionBar())
                    .setTitle(arguments != null ? arguments.getString(TAG_SCREEN, "แอปพลิเคชัน") : "แอปพลิเคชัน");
            invalidateOptionsMenu();
            hideSettings = (arguments != null && arguments.getBoolean(HIDE_SETTINGS, false));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        StaticUtil.getInstance().applyContext(this);
        DataSaveHandler.getInstance();

        NavHostFragment fragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment));
        navController = fragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController);
        navController.addOnDestinationChangedListener(listener);

        if (savedInstanceState == null || !savedInstanceState.getBoolean(SPLASH_SCREEN_ALREADY, false))
            StaticUtil.showSplashScreen(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SPLASH_SCREEN_ALREADY, true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.master_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.settings).setVisible(!hideSettings);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                /*
                Log.w("MasterActivity", "Settings Button Pressed");
                Snackbar.make(findViewById(android.R.id.content), "ปุ่มตั้งค่าถูกกด", Snackbar.LENGTH_SHORT).show();)
                */
                Bundle args = new Bundle();
                args.putString(TAG_SCREEN, SCREEN_SETTINGS);
                args.putBoolean(HIDE_SETTINGS, true);
                navController.navigate(R.id.settingsFragment, args);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
