package th.ac.sk.timetableapp.modify;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.database.PeriodDatabase;
import th.ac.sk.timetableapp.datamodel.Period;
import th.ac.sk.timetableapp.settings.SettingsActivity;
import th.ac.sk.timetableapp.util.DialogBuilder;

public class ModifyClassroomActivity extends AppCompatActivity {
    public static ArrayList<ModifyClassroomData> dataList;
    public static ArrayList<ModifyClassroomData> backupDataList;
    public static ModifyClassroomAdapter adapter;

    public static void onEditConfirmButtonClick(@NonNull ModifyClassroomData data) {
        if (data.type == ModifyClassroomData.VIEW_TYPE_EDIT) {
            data.type = ModifyClassroomData.VIEW_TYPE_DISPLAY;
        } else if (data.type == ModifyClassroomData.VIEW_TYPE_EDIT_NULL) {
            data.type = ModifyClassroomData.VIEW_TYPE_DISPLAY_NULL;
        }

        dataList.set(data.displayPosition, data);
        backupDataList.set(data.displayPosition, data);
        updateData(data.period, data.position);
        adapter.notifyItemChanged(data.displayPosition);
    }

    public static void onRequestEditButtonClick(@NonNull ModifyClassroomData data) {
        backupDataList.set(data.displayPosition, data);
        if (data.type == ModifyClassroomData.VIEW_TYPE_DISPLAY) {
            data.type = ModifyClassroomData.VIEW_TYPE_EDIT;
        } else if (data.type == ModifyClassroomData.VIEW_TYPE_DISPLAY_NULL) {
            data.type = ModifyClassroomData.VIEW_TYPE_EDIT_NULL;
        }
        dataList.set(data.displayPosition, data);
        adapter.notifyItemChanged(data.displayPosition);
    }

    public static void onToggleEditType(@NonNull ModifyClassroomData data) {
        if (data.type == ModifyClassroomData.VIEW_TYPE_EDIT) {
            data.type = ModifyClassroomData.VIEW_TYPE_EDIT_NULL;
            data.period.type = Period.Type.NO_CLASS;
        } else if (data.type == ModifyClassroomData.VIEW_TYPE_EDIT_NULL) {
            data.type = ModifyClassroomData.VIEW_TYPE_EDIT;
            data.period.type = Period.Type.HAVE_CLASS;
        }
        dataList.set(data.displayPosition, data);
        adapter.notifyItemChanged(data.displayPosition);
    }

    public static void updateData(Period period, int position) {
        PeriodDatabase.getInstance().putToCurrentData(position, period);
        DataSaveHandler.saveCurrentPeriodData();
    }

    public static void importDataFromDatabase() {
        ArrayList<ModifyClassroomData> input = new ArrayList<>();
        int[] d = {ModifyClassroomData.MON, ModifyClassroomData.TUE, ModifyClassroomData.WED, ModifyClassroomData.THU, ModifyClassroomData.FRI};
        for (int day = 0; day < 5; day++) {
            ModifyClassroomData header = new ModifyClassroomData(d[day]);
            header.type = ModifyClassroomData.VIEW_TYPE_HEADER;
            input.add(header);
            for (int i = 0; i < 10; i++) {
                Period period = PeriodDatabase.getInstance().getCurrentData().get((day * 10) + i);
                Objects.requireNonNull(period);
                if (period.type == Period.Type.HAVE_CLASS) {
                    ModifyClassroomData display = new ModifyClassroomData(d[day]);
                    display.type = ModifyClassroomData.VIEW_TYPE_DISPLAY;
                    display.period = period;
                    display.position = (day * 10) + i;
                    input.add(display);
                } else {
                    ModifyClassroomData displayNull = new ModifyClassroomData(d[day]);
                    displayNull.type = ModifyClassroomData.VIEW_TYPE_DISPLAY_NULL;
                    displayNull.period = period;
                    displayNull.position = (day * 10) + i;
                    input.add(displayNull);
                }
            }
        }
        dataList = input;
        backupDataList = input;
    }

    @Override
    public void supportNavigateUpTo(@NonNull Intent upIntent) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_classroom);

        DataSaveHandler.loadCurrentPeriodData();

        importDataFromDatabase();
        PeriodDatabase.getInstance().setObserver(this, new Observer<SparseArray<SparseArray<Period>>>() {
            @Override
            public void onChanged(SparseArray<SparseArray<Period>> data) {
                DataSaveHandler.saveCurrentPeriodData();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ModifyClassroomAdapter();

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_classroom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.wipeData:
                DialogBuilder.getWipeDataDialog(this).show();
                return true;
            case R.id.rollbackData:
                DialogBuilder.getRollbackDialog(this).show();
                return true;
            case R.id.importData:
                DialogBuilder.getImportDataDialog(this).show();
                return true;
            case R.id.exportData:
                DialogBuilder.getExportDataDialog(this).show();
                return true;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static class ModifyClassroomViewHolder {
        static class Header extends RecyclerView.ViewHolder {
            TextView banner;

            Header(@NonNull View v) {
                super(v);
                banner = v.findViewById(R.id.banner);
            }

            void setupData(ModifyClassroomData data) {
                if (data.day == ModifyClassroomData.MON) {
                    banner.setText("วันจันทร์");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorMonday));
                } else if (data.day == ModifyClassroomData.TUE) {
                    banner.setText("วันอังคาร");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorTuesday));
                } else if (data.day == ModifyClassroomData.WED) {
                    banner.setText("วันพุธ");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorWednesday));
                } else if (data.day == ModifyClassroomData.THU) {
                    banner.setText("วันพฤหัสบดี");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorThursday));
                } else if (data.day == ModifyClassroomData.FRI) {
                    banner.setText("วันศุกร์");
                    banner.setBackgroundColor(banner.getResources().getColor(R.color.colorFriday));
                }
            }
        }

        static class Edit extends RecyclerView.ViewHolder {
            TextView periodTV;
            TextInputEditText subjectET;
            TextInputEditText subjectCodeET;
            TextInputEditText teacherListET;
            MaterialButton submitBtn;
            MaterialButton toggleBtn;
            Period backupPeriod;
            TextView errorMessage;

            Edit(@NonNull View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                subjectET = v.findViewById(R.id.classroom).findViewWithTag("EditText");
                subjectCodeET = v.findViewById(R.id.location).findViewWithTag("EditText");
                teacherListET = v.findViewById(R.id.teacherList).findViewWithTag("EditText");
                submitBtn = v.findViewById(R.id.submit);
                toggleBtn = v.findViewById(R.id.toggle);
                errorMessage = v.findViewById(R.id.errorMessage);

            }

            void setupData(final ModifyClassroomData data) {
                backupPeriod = data.period;
                errorMessage.setVisibility(View.GONE);
                periodTV.setText(String.format(Locale.getDefault(), "คาบที่ %d", backupPeriod.periodNum));
                subjectET.setText(backupPeriod.subject);
                subjectCodeET.setText(backupPeriod.subjectCode);
                teacherListET.setText(backupPeriod.teacherList);
                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.period = new Period(
                                nullOrToString(subjectET.getText()),
                                nullOrToString(subjectCodeET.getText()),
                                nullOrToString(teacherListET.getText()),
                                backupPeriod.periodNum);
                        if (data.period.classIsNull()) {
                            data.period = backupPeriod;
                            errorMessage.setVisibility(View.VISIBLE);
                        } else {
                            onEditConfirmButtonClick(data);
                        }
                    }

                    private String nullOrToString(Editable text) {
                        if (text == null || text.toString().equals("")) return null;
                        return text.toString();
                    }
                });
                toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onToggleEditType(data);
                    }
                });
            }

        }

        static class EditNull extends RecyclerView.ViewHolder {
            TextView periodTV;
            RadioGroup chooser;
            RadioButton choose1;
            RadioButton choose2;
            RadioButton choose3;
            RadioButton choose4;
            MaterialButton submitBtn;
            MaterialButton toggleBtn;
            Period backupPeriod;

            EditNull(@NonNull View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                chooser = v.findViewById(R.id.chooser);
                choose1 = v.findViewById(R.id.choose1);
                choose2 = v.findViewById(R.id.choose2);
                choose3 = v.findViewById(R.id.choose3);
                choose4 = v.findViewById(R.id.choose4);

                submitBtn = v.findViewById(R.id.submit);
                toggleBtn = v.findViewById(R.id.toggle);
            }

            void setupData(final ModifyClassroomData data) {
                backupPeriod = data.period;
                periodTV.setText(String.format(Locale.getDefault(), "คาบที่ %d", backupPeriod.periodNum));
                if (data.period.type == Period.Type.NO_CLASS) choose1.toggle();
                if (data.period.type == Period.Type.LUNCH_PERIOD) choose2.toggle();
                if (data.period.type == Period.Type.SCHOOL_CONFERENCE) choose3.toggle();
                if (data.period.type == Period.Type.MILITARY_WORK) choose4.toggle();

                submitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = chooser.getCheckedRadioButtonId();
                        if (id == R.id.choose1)
                            data.period = new Period(Period.Type.NO_CLASS, backupPeriod.periodNum);
                        else if (id == R.id.choose2)
                            data.period = new Period(Period.Type.LUNCH_PERIOD, backupPeriod.periodNum);
                        else if (id == R.id.choose3)
                            data.period = new Period(Period.Type.SCHOOL_CONFERENCE, backupPeriod.periodNum);
                        else if (id == R.id.choose4)
                            data.period = new Period(Period.Type.MILITARY_WORK, backupPeriod.periodNum);
                        onEditConfirmButtonClick(data);
                    }
                });
                toggleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onToggleEditType(data);
                    }
                });
            }

        }

        static class Display extends RecyclerView.ViewHolder {
            TextView periodTV;
            TextView line1TV;
            TextView line2TV;
            ImageView icBtn;
            Period period;

            Display(@NonNull View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                line1TV = v.findViewById(R.id.line1);
                line2TV = v.findViewById(R.id.line2);
                icBtn = v.findViewById(R.id.ic_edit);
            }

            void setupData(final ModifyClassroomData data) {
                period = data.period;
                periodTV.setText(String.format(Locale.getDefault(), "คาบที่ %d :", period.periodNum));
                line1TV.setText(String.format(Locale.getDefault(), "%s (%s)", period.subject, period.subjectCode));
                line2TV.setText(period.teacherList);
                icBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRequestEditButtonClick(data);
                    }
                });
            }
        }

        static class DisplayNull extends RecyclerView.ViewHolder {
            TextView periodTV;
            TextView descTV;
            ImageView icBtn;
            Period period;

            DisplayNull(@NonNull View v) {
                super(v);
                periodTV = v.findViewById(R.id.period);
                descTV = v.findViewById(R.id.desc);
                icBtn = v.findViewById(R.id.ic_edit);
            }

            void setupData(final ModifyClassroomData data) {
                period = data.period;
                periodTV.setText(String.format(Locale.getDefault(), "คาบที่ %d :", period.periodNum));

                descTV.setText(Period.Type.getStringFromType(period.type));
                icBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRequestEditButtonClick(data);
                    }
                });
            }
        }
    }

    static class ModifyClassroomData {
        private static final int MON = 515;
        private static final int TUE = 501;
        private static final int WED = 505;
        private static final int THU = 591;
        private static final int FRI = 559;
        private static final int VIEW_TYPE_HEADER = 982;
        private static final int VIEW_TYPE_EDIT = 931;
        private static final int VIEW_TYPE_EDIT_NULL = 910;
        private static final int VIEW_TYPE_DISPLAY = 997;
        private static final int VIEW_TYPE_DISPLAY_NULL = 974;

        Period period;
        int position;
        int displayPosition;
        int day;
        int type;

        ModifyClassroomData(int day) {
            this.day = day;
        }
    }

    private class ModifyClassroomAdapter extends RecyclerView.Adapter {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ModifyClassroomData.VIEW_TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_day_header, parent, false);
                return new ModifyClassroomViewHolder.Header(v);
            } else if (viewType == ModifyClassroomData.VIEW_TYPE_EDIT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_classroom_edit_period, parent, false);
                return new ModifyClassroomViewHolder.Edit(v);
            } else if (viewType == ModifyClassroomData.VIEW_TYPE_EDIT_NULL) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_classroom_edit_null_period, parent, false);
                return new ModifyClassroomViewHolder.EditNull(v);
            } else if (viewType == ModifyClassroomData.VIEW_TYPE_DISPLAY) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_classroom_display_period, parent, false);
                return new ModifyClassroomViewHolder.Display(v);
            } else if (viewType == ModifyClassroomData.VIEW_TYPE_DISPLAY_NULL) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_modify_classroom_display_null_period, parent, false);
                return new ModifyClassroomViewHolder.DisplayNull(v);
            } else {
                throw new NullPointerException();
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ModifyClassroomData data = dataList.get(position);
            if (holder instanceof ModifyClassroomViewHolder.Header) {
                ModifyClassroomViewHolder.Header headerVH = ((ModifyClassroomViewHolder.Header) holder);
                headerVH.setupData(data);
            } else if (holder instanceof ModifyClassroomViewHolder.Edit) {
                ModifyClassroomViewHolder.Edit editVH = (ModifyClassroomViewHolder.Edit) holder;
                data.displayPosition = position;
                editVH.setupData(data);
            } else if (holder instanceof ModifyClassroomViewHolder.EditNull) {
                ModifyClassroomViewHolder.EditNull editNullVH = (ModifyClassroomViewHolder.EditNull) holder;
                data.displayPosition = position;
                editNullVH.setupData(data);
            } else if (holder instanceof ModifyClassroomViewHolder.Display) {
                ModifyClassroomViewHolder.Display displayVH = (ModifyClassroomViewHolder.Display) holder;
                data.displayPosition = position;
                displayVH.setupData(data);
            } else if (holder instanceof ModifyClassroomViewHolder.DisplayNull) {
                ModifyClassroomViewHolder.DisplayNull displayNullVH = (ModifyClassroomViewHolder.DisplayNull) holder;
                data.displayPosition = position;
                displayNullVH.setupData(data);
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
