package th.ac.sk.timetableapp.display;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.datamodel.TeacherDetail;
import th.ac.sk.timetableapp.datamodel.TeacherLocation;

public class TeacherLocationDisplayActivity extends AppCompatActivity {
    public RecyclerView rv;
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_location_display);
        int day = getIntent().getIntExtra("day", 0);
        int period = getIntent().getIntExtra("period", 0);
        if (day == 0 && period == 0) finish();

        Toast.makeText(this, "Day = " + day + " Period = " + period, Toast.LENGTH_SHORT).show();
        key = (day - 1) * 10 + (period-1); // key in subset [0,49]
        rv = findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new TeacherListAdapter());
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        finish();
    }


    public class TeacherListAdapter extends RecyclerView.Adapter<TeacherDataViewHolder> {


        @NonNull
        @Override
        public TeacherDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
            return new TeacherDataViewHolder((LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.widget_teacher_location_display_details, parent, false)));
        }

        @Override
        public void onBindViewHolder(@NonNull TeacherDataViewHolder holder, int pos) {
            TeacherDetail teacherDetail = TeacherLocationDatabase.getInstance().getDetail().valueAt(pos);
            holder.teacher.setText(String.format(Locale.getDefault(), "อ. %s", teacherDetail.name));
            holder.v.setBackgroundColor(getResources().getColor(
                    pos % 2 == 0 ? R.color.normalBackground : R.color.tintedBackground));

            TeacherLocation teacherLocation = TeacherLocationDatabase.getInstance().getLocation(teacherDetail.id, key);
            holder.status.setText(teacherLocation != null ?
                    String.format(Locale.getDefault(), "สอนที่ห้อง %s (%s)",
                            teacherLocation.classroom, teacherLocation.location) : "ว่าง");
            holder.status.setTextColor(getResources().getColor(teacherLocation != null ? android.R.color.holo_red_dark : android.R.color.holo_blue_dark));
        }

        @Override
        public int getItemCount() {
            return TeacherLocationDatabase.getTeacherCount();
        }

        @Override
        public void onViewAttachedToWindow(@NonNull TeacherDataViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            rv.setBackgroundColor(getResources().getColor(
                    TeacherLocationDatabase.getTeacherCount() % 2 == 0 ?
                            R.color.normalBackground : R.color.tintedBackground));
        }
    }
    class TeacherDataViewHolder extends RecyclerView.ViewHolder {
        TextView teacher;
        TextView status;
        View v;

        TeacherDataViewHolder(@NonNull View v) {
            super(v);
            this.teacher = v.findViewById(R.id.period);
            this.status = v.findViewById(R.id.status);
            this.v = v;
        }
    }
}

