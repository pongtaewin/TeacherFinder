package th.ac.sk.timetableapp.display;

import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import th.ac.sk.timetableapp.R;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreenActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                finish();
            }
        };
    }

    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 1500);
    }

    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}