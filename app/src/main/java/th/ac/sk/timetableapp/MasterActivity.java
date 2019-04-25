package th.ac.sk.timetableapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.tool.StaticUtil;

public class MasterActivity extends AppCompatActivity {

    public static final String SPLASH_SCREEN_ALREADY = "splashScreenAlready";
    public static boolean hideSettings = false;
    public NavController navController;
    public ActionBar actionBar;

    NavController.OnDestinationChangedListener listener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            int id = Objects.requireNonNull(navController.getCurrentDestination()).getId();

            actionBar.setTitle(StaticUtil.getTitleText(id, arguments));
            actionBar.setSubtitle(StaticUtil.getSubtitleText(id));
            invalidateOptionsMenu();

            hideSettings = id == R.id.settingsFragment;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        StaticUtil.getInstance().applyContext(this);
        DataSaveHandler.getInstance();

        actionBar = Objects.requireNonNull(getSupportActionBar());

        final NavHostFragment fragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment));
        navController = fragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController,
                new AppBarConfiguration.Builder(R.id.mainFragment).setFallbackOnNavigateUpListener(new AppBarConfiguration.OnNavigateUpListener() {
                    @Override
                    public boolean onNavigateUp(){
                        return navController.popBackStack();
                    }
                }).build());
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
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.settings:
                navController.navigate(R.id.settingsFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!navController.popBackStack()) super.onBackPressed();
    }

    @Override
    public boolean onNavigateUp() {
        return super.onNavigateUp();
    }

    @Override
    public boolean onNavigateUpFromChild(Activity child) {
        return super.onNavigateUpFromChild(child);
    }
}
