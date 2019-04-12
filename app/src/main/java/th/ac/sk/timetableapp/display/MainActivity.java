package th.ac.sk.timetableapp.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.modify.ModifyClassroomActivity;
import th.ac.sk.timetableapp.modify.ModifyTeacherLocationChooserActivity;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.util.StaticUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StaticUtil.getInstance().applyContext(this);
        DataSaveHandler.getInstance();

        findViewById(R.id.c1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), DayDisplayActivity.class));
            }
        });
        findViewById(R.id.c2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), ModifyClassroomActivity.class));
            }
        });
        findViewById(R.id.c3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), ModifyTeacherLocationChooserActivity.class));
                //Snackbar.make((View) v.getParent(), "แสดงหน้าแก้ไขข้อมูลอาจารย์", Snackbar.LENGTH_LONG).show();
            }
        });

        if (savedInstanceState == null || !savedInstanceState.getBoolean("resume", false))
            StaticUtil.showSplashScreen(this);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resume", true);
    }
}
