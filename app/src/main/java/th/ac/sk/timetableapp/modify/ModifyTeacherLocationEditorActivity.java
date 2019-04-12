package th.ac.sk.timetableapp.modify;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.datamodel.TeacherDetail;
import th.ac.sk.timetableapp.datamodel.TeacherLocation;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.util.StaticUtil;

public class ModifyTeacherLocationEditorActivity extends AppCompatActivity {
    public static int teacherId;
    public static ArrayList<ModifyTeacherLocationData> dataList;
    public static ArrayList<ModifyTeacherLocationData> backupDataList;
    public static ModifyTeacherLocationAdapter adapter;
    public TeacherDetail thisTeacherDetail;
    public Activity thisActivity;

    public static void importDataFromDatabase() {
        ArrayList<ModifyTeacherLocationData> input = new ArrayList<>();
        int[] d = {ModifyTeacherLocationData.MON, ModifyTeacherLocationData.TUE, ModifyTeacherLocationData.WED, ModifyTeacherLocationData.THU, ModifyTeacherLocationData.FRI};
        for (int day = 0; day < 5; day++) {
            ModifyTeacherLocationData header = new ModifyTeacherLocationData(d[day]);
            header.type = ModifyTeacherLocationData.VIEW_TYPE_HEADER;
            input.add(header);
            for (int i = 0; i < 10; i++) {
                int key = (day * 10) + i;
                TeacherLocation teacherLocation = TeacherLocationDatabase.getInstance().getLocation(teacherId, key);
                if (teacherLocation == null) {
                    teacherLocation = new TeacherLocation(teacherId, key);
                } else {
                    Log.w("importDataFromDatabase", "teacherLocation is not null.");
                }
                ModifyTeacherLocationData display = new ModifyTeacherLocationData(d[day]);
                display.type = ModifyTeacherLocationData.VIEW_TYPE_DISPLAY;
                display.location = teacherLocation;
                display.position = key;
                input.add(display);

            }
        }
        dataList = input;
        backupDataList = input;
    }

    private static void onRequestEditButtonClick(ModifyTeacherLocationData data) {
        backupDataList.set(data.displayPosition, data);
        data.type = ModifyTeacherLocationData.VIEW_TYPE_EDIT;
        dataList.set(data.displayPosition, data);
        adapter.notifyItemChanged(data.displayPosition);
    }

    private static void updateData(TeacherLocation location, int position) {
        if (location == null)
            TeacherLocationDatabase.getInstance().removeLocation(teacherId, position);
        else
            TeacherLocationDatabase.getInstance().putLocation(teacherId, position, location);
        DataSaveHandler.saveCurrentTeacherLocationData();
    }

    private static void onEditConfirmButtonClick(@NonNull ModifyTeacherLocationData data) {
        data.type = ModifyTeacherLocationData.VIEW_TYPE_DISPLAY;
        dataList.set(data.displayPosition, data);
        backupDataList.set(data.displayPosition, data);
        updateData(data.location, data.position);
        adapter.notifyItemChanged(data.displayPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_teacher_location_editor);

        DataSaveHandler.loadCurrentTeacherLocationData();
        onIntentCheck();
        thisActivity = this;
        importDataFromDatabase();
        TeacherLocationDatabase.getInstance().setDetailObserver(this, new Observer<SparseArray<TeacherDetail>>() {
            @Override
            public void onChanged(SparseArray<TeacherDetail> teacherDetailSparseArray) {
                DataSaveHandler.saveCurrentTeacherLocationData();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModifyTeacherLocationAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void onIntentCheck() {
        Intent intent = getIntent();
        teacherId = intent.getIntExtra("teacherId", -1);
        if (teacherId == -1) finish();
        this.thisTeacherDetail = TeacherLocationDatabase.getInstance().getDetail(teacherId);
    }

    static class ModifyTeacherLocationViewHolder {
        static class Header extends RecyclerView.ViewHolder {
            TextView banner;

            Header(@NonNull View v) {
                super(v);
                banner = v.findViewById(R.id.banner);
            }

            void setupData(ModifyTeacherLocationData data) {
                if (data.day == ModifyTeacherLocationData.MON) {
                    banner.setText("วันจันทร์");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorMonday));
                } else if (data.day == ModifyTeacherLocationData.TUE) {
                    banner.setText("วันอังคาร");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorTuesday));
                } else if (data.day == ModifyTeacherLocationData.WED) {
                    banner.setText("วันพุธ");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorWednesday));
                } else if (data.day == ModifyTeacherLocationData.THU) {
                    banner.setText("วันพฤหัสบดี");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorThursday));
                } else if (data.day == ModifyTeacherLocationData.FRI) {
                    banner.setText("วันศุกร์");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorFriday));
                }
            }
        }

        static class Display extends RecyclerView.ViewHolder {
            TextView periodTV;
            TextView statusTV;
            ImageView icBtn;
            TeacherLocation location;


            Display(View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                statusTV = v.findViewById(R.id.status);
                icBtn = v.findViewById(R.id.ic_edit);
            }

            void setupData(final ModifyTeacherLocationData data) {
                location = data.location;

                periodTV.setText(String.format(Locale.getDefault(), "คาบที่ %d :", (location.key % 10) + 1));
                boolean occupied = !(location.classroom == null || location.location == null);
                statusTV.setText(occupied ? String.format(Locale.getDefault(), "สอนที่ห้อง %s (%s)", location.classroom, location.location) : "ว่าง");
                statusTV.setTextColor(StaticUtil.resources.getColor(occupied ? android.R.color.holo_red_dark : android.R.color.holo_blue_dark));
                icBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRequestEditButtonClick(data);
                    }
                });
            }
        }

        static class Edit extends RecyclerView.ViewHolder {
            TextView periodTV;
            TextInputEditText classroomET;
            TextInputEditText locationET;
            MaterialButton submitBtn;
            MaterialButton toggleBtn;
            TeacherLocation location;

            Edit(View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                classroomET = v.findViewById(R.id.fieldClassroom);
                locationET = v.findViewById(R.id.fieldLocation);
                submitBtn = v.findViewById(R.id.submit);
                toggleBtn = v.findViewById(R.id.toggle);
            }

            void setupData(final ModifyTeacherLocationData data) {
                location = data.location;
                periodTV.setText(String.format(Locale.getDefault(), "คาบที่ %d", (location.key % 10) + 1));
                classroomET.setText(location.classroom == null ? "" : location.classroom);
                locationET.setText(location.location == null ? "" : location.location);
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.location = new TeacherLocation(teacherId,
                                Objects.requireNonNull(classroomET.getText()).toString(),
                                Objects.requireNonNull(locationET.getText()).toString(),
                                location.key);
                        onEditConfirmButtonClick(data);
                    }
                });
                toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.location = new TeacherLocation(teacherId, location.key);
                        onEditConfirmButtonClick(data);
                    }
                });
            }
        }
    }

    static class ModifyTeacherLocationData {
        private static final int MON = 111;
        private static final int TUE = 222;
        private static final int WED = 333;
        private static final int THU = 444;
        private static final int FRI = 555;
        private static final int VIEW_TYPE_HEADER = 982;
        private static final int VIEW_TYPE_EDIT = 931;
        private static final int VIEW_TYPE_DISPLAY = 997;

        TeacherLocation location;
        int position;
        int displayPosition;
        int day;
        int type;

        ModifyTeacherLocationData(int day) {
            this.day = day;
        }
    }

    private class ModifyTeacherLocationAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ModifyTeacherLocationData.VIEW_TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_day_header, parent, false);
                return new ModifyTeacherLocationViewHolder.Header(v);
            } else if (viewType == ModifyTeacherLocationData.VIEW_TYPE_DISPLAY) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_teacher_location_details_display, parent, false);
                return new ModifyTeacherLocationViewHolder.Display(v);
            } else if (viewType == ModifyTeacherLocationData.VIEW_TYPE_EDIT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_teacher_location_details_edit, parent, false);
                return new ModifyTeacherLocationViewHolder.Edit(v);
            } else {
                throw new NullPointerException();
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ModifyTeacherLocationData data = dataList.get(position);
            data.displayPosition = position;
            if (holder instanceof ModifyTeacherLocationViewHolder.Header) {
                ModifyTeacherLocationViewHolder.Header headerVH = (ModifyTeacherLocationViewHolder.Header) holder;
                headerVH.setupData(data);
            } else if (holder instanceof ModifyTeacherLocationViewHolder.Display) {
                ModifyTeacherLocationViewHolder.Display displayVH = (ModifyTeacherLocationViewHolder.Display) holder;
                displayVH.setupData(data);
            } else if (holder instanceof ModifyTeacherLocationViewHolder.Edit) {
                ModifyTeacherLocationViewHolder.Edit editVH = (ModifyTeacherLocationViewHolder.Edit) holder;
                editVH.setupData(data);
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return dataList.get(position).type;
        }
    }
}
