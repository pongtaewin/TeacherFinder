package th.ac.sk.timetableapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

import th.ac.sk.timetableapp.tool.InstantiateHelper;
import th.ac.sk.timetableapp.tool.StaticUtil;

public class MasterActivity extends AppCompatActivity {

    public static final String SPLASH_SCREEN_ALREADY = "splashScreenAlready";
    public static final int REQUEST_IMPORT = 99;
    public static final int RESULT_IMPORT_SUCCESS = 138;
    public static final int RESULT_IMPORT_FAIL = 934;
    public static boolean showImportSuccessText = false;
    public static boolean hideSettings = false;
    public NavController navController;
    public ActionBar actionBar;
    private int currentDestinationId = -1;
    private int previousDestinationId = -1;

    NavController.OnDestinationChangedListener listener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            currentDestinationId = Objects.requireNonNull(navController.getCurrentDestination()).getId();

            actionBar.setTitle(StaticUtil.getTitleText(currentDestinationId, arguments));
            invalidateOptionsMenu();

            if (currentDestinationId == R.id.mainFragment && previousDestinationId == R.id.importCheckFragment) {
                setResult(RESULT_IMPORT_FAIL);
                finish();
                return;
            }

            hideSettings = currentDestinationId == R.id.settingsFragment || currentDestinationId == R.id.importCheckFragment;
            previousDestinationId = currentDestinationId;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        InstantiateHelper.instantiate(this);

        actionBar = Objects.requireNonNull(getSupportActionBar());

        final NavHostFragment fragment = Objects.requireNonNull((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragment));
        navController = fragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController,
                new AppBarConfiguration.Builder(R.id.mainFragment).setFallbackOnNavigateUpListener(new AppBarConfiguration.OnNavigateUpListener() {
                    @Override
                    public boolean onNavigateUp() {
                        return navController.popBackStack();
                    }
                }).build());
        navController.addOnDestinationChangedListener(listener);

        if (savedInstanceState == null || !savedInstanceState.getBoolean(SPLASH_SCREEN_ALREADY, false))
            StaticUtil.showSplashScreen(this);

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (showImportSuccessText) {
            Snackbar.make(findViewById(android.R.id.content), "นำเข้าข้อมูลสำเร็จ", Snackbar.LENGTH_LONG).show();
            showImportSuccessText = false;
        }
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

    public void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (Objects.equals(action, Intent.ACTION_VIEW)) {
            Bundle args = new Bundle();
            args.putString("time", new SimpleDateFormat("d MMM yyyy H:mm", new Locale("th", "TH")).format(intent.getLongExtra("t", 0)));
            args.putString("data", intent.getStringExtra("d"));
            args.putString("prefix", intent.getStringExtra("p"));
            if (currentDestinationId != R.id.importCheckFragment)
                navController.navigate(R.id.action_import_check, args);
        }
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMPORT:
                switch (resultCode) {
                    case RESULT_IMPORT_SUCCESS:
                        showImportSuccessText = true;
                        navController.navigateUp();
                }
        }
    }
}
