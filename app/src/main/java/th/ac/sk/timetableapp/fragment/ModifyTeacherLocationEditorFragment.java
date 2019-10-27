package th.ac.sk.timetableapp.fragment;

import android.os.Bundle;
import android.util.Log;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.database.TeacherLocationDatabase;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.tool.StaticUtil;

public class ModifyTeacherLocationEditorFragment extends Fragment {
    private static int teacherId;
    private static ArrayList<ModifyTeacherLocationData> dataList;
    private static ArrayList<ModifyTeacherLocationData> backupDataList;
    private static ModifyTeacherLocationAdapter adapter;
    private static RecyclerView rv;

    private static void importDataFromDatabase() {
        ArrayList<ModifyTeacherLocationData> input = new ArrayList<>();
        int[] d = {ModifyTeacherLocationData.MON, ModifyTeacherLocationData.TUE, ModifyTeacherLocationData.WED, ModifyTeacherLocationData.THU, ModifyTeacherLocationData.FRI};
        for (int day = 0; day < 5; day++) {
            ModifyTeacherLocationData header = new ModifyTeacherLocationData(d[day]);
            header.type = ModifyTeacherLocationData.VIEW_TYPE_HEADER;
            input.add(header);
            for (int i = 0; i < 10; i++) {
                int key = (day * 10) + i;
                TeacherLocation teacherLocation = TeacherLocationDatabase.getInstance().getLocationDataAt(teacherId, key);
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
        DataSaveHandler.loadMaster();
        backupDataList.set(data.displayPosition, data);
        data.type = ModifyTeacherLocationData.VIEW_TYPE_EDIT;
        dataList.set(data.displayPosition, data);
        getAdapter().notifyItemChanged(data.displayPosition);
        rv.scrollToPosition(data.displayPosition);
    }

    private static void updateData(TeacherLocation location, int position) {
        if (location == null)
            TeacherLocationDatabase.getInstance().removeLocationDataAt(teacherId, position);
        else
            TeacherLocationDatabase.getInstance().putLocationDataAt(teacherId, position, location);
        DataSaveHandler.saveMaster();
    }

    private static void onEditConfirmButtonClick(@NonNull ModifyTeacherLocationData data) {
        data.type = ModifyTeacherLocationData.VIEW_TYPE_DISPLAY;
        dataList.set(data.displayPosition, data);
        backupDataList.set(data.displayPosition, data);
        updateData(data.location, data.position);
        getAdapter().notifyItemChanged(data.displayPosition);
        DataSaveHandler.saveMaster();
        rv.scrollToPosition(data.displayPosition);
    }

    private static ModifyTeacherLocationAdapter getAdapter() {
        return adapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        teacherId = requireArguments().getInt("teacherId");
        return inflater.inflate(R.layout.fragment_modify_teacher_location_editor,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DataSaveHandler.loadMaster();
        importDataFromDatabase();
        TeacherLocationDatabase.getInstance().setDetailObserver(getViewLifecycleOwner(), new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                DataSaveHandler.saveMaster();
            }
        });

        rv = view.findViewById(R.id.recycler);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ModifyTeacherLocationAdapter();
        rv.setAdapter(getAdapter());
    }

    static class ModifyTeacherLocationViewHolder {
        static class Header extends RecyclerView.ViewHolder {
            final TextView banner;

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
            final TextView periodTV;
            final TextView statusTV;
            final ImageView icBtn;
            TeacherLocation location;


            Display(View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                statusTV = v.findViewById(R.id.status);
                icBtn = v.findViewById(R.id.ic_edit);
            }

            void setupData(final ModifyTeacherLocationData data) {
                location = data.location;

                periodTV.setText(String.format(Locale.getDefault(), "%d)", (location.key % 10) + 1));
                boolean occupied = !(location.classroom == null || location.location == null);
                statusTV.setText(occupied ? String.format(Locale.getDefault(), "สอนที่ห้อง %s (%s)", location.classroom, location.location) : "ว่าง");
                statusTV.setTextColor(StaticUtil.resources.getColor(occupied ? R.color.colorAccent : R.color.textColorPrimary));
                icBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRequestEditButtonClick(data);
                    }
                });
            }
        }

        static class Edit extends RecyclerView.ViewHolder {
            final TextView periodTV;
            final TextInputEditText classroomET;
            final TextInputEditText locationET;
            final MaterialButton submitBtn;
            final MaterialButton toggleBtn;
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
        final int day;
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
