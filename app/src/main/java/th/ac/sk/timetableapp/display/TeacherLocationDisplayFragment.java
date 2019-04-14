package th.ac.sk.timetableapp.display;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.datamodel.TeacherDetail;
import th.ac.sk.timetableapp.datamodel.TeacherLocation;

public class TeacherLocationDisplayFragment extends Fragment {
    private RecyclerView rv;
    private int key;

    static String getActionBarText(@IntRange(from = 0, to = 49) int key) {
        return "สถานที่สอน วัน" + new String[]{"จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์"}[key / 10] + " คาบ " + String.valueOf((key % 10) + 1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = requireArguments();
        key = args.getInt("key");
        return inflater.inflate(R.layout.fragment_teacher_location_display, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        DataSaveHandler.loadMaster();
        rv = v.findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new TeacherListAdapter());
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

