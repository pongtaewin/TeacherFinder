package th.ac.sk.timetableapp.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;

public class DayDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("กรุณาเลือกวัน");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_display);
        DataSaveHandler.loadMaster();
        findViewById(R.id.c1).setOnClickListener(new DayClickListener(1));
        findViewById(R.id.c2).setOnClickListener(new DayClickListener(2));
        findViewById(R.id.c3).setOnClickListener(new DayClickListener(3));
        findViewById(R.id.c4).setOnClickListener(new DayClickListener(4));
        findViewById(R.id.c5).setOnClickListener(new DayClickListener(5));
    }

    public void onClick(View v) {

    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        finishAfterTransition();
    }

    class DayClickListener implements View.OnClickListener {
        private final int day;

        DayClickListener(int day) {
            this.day = day;
        }

        @Override
        public void onClick(View v) {
            Intent intent = getIntent();
            intent.setClass(getApplication(), PeriodDisplayActivity.class);
            intent.putExtra("day", day);
            startActivity(intent);
        }
    }
}
