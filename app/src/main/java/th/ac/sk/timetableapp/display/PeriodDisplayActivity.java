package th.ac.sk.timetableapp.display;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.PeriodDatabase;
import th.ac.sk.timetableapp.datamodel.Period;

public class PeriodDisplayActivity extends AppCompatActivity {
    public static ArrayList<Period> dataList;
    int day = 0;

    public static void importData(int day) {
        SparseArray<Period> data = PeriodDatabase.getInstance().getCurrentData();
        ArrayList<Period> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) result.add(data.valueAt((10 * (day - 1) + i)));
        dataList = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("กรุณาเลือกคาบ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_display);

        day = getIntent().getIntExtra("day", 0);
        if (day == 0) finish();

        RecyclerView recyclerView = findViewById(R.id.grid);

        initActionBar();
        importData(day);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PeriodDisplayAdapter());
    }

    public void initActionBar(){
        String[] tag = {"จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์"};
        Objects.requireNonNull(getSupportActionBar()).setTitle("คาบเรียน วัน"+tag[day-1]);
    }

    private Period getData(int i) {
        return dataList.get(i);
    }

    private String[] getPeriodInfo(@NonNull Period period) {
        String[] result = new String[2];
        switch (period.periodNum) {
            case 1:
                result[0] = "08:30 - 09:20 น.";
                result[1] = "08:30 - 09:10 น.";
                break;
            case 2:
                result[0] = "09:20 - 10:10 น.";
                result[1] = "09:10 - 09:50 น.";
                break;
            case 3:
                result[0] = "10:10 - 11:00 น.";
                result[1] = "09:50 - 10:30 น.";
                break;
            case 4:
                result[0] = "11:00 - 11:50 น.";
                result[1] = "10:30 - 11:10 น.";
                break;
            case 5:
                result[0] = "11:50 - 12:40 น.";
                result[1] = "11:10 - 11:50 น.";
                break;
            case 6:
                result[0] = "12:40 - 13:30 น.";
                result[1] = "11:50 - 12:30 น.";
                break;
            case 7:
                result[0] = "13:30 - 14:20 น.";
                result[1] = "12:30 - 13:10 น.";
                break;
            case 8:
                result[0] = "14:20 - 15:10 น.";
                result[1] = "13:10 - 13:50 น.";
                break;
            case 9:
                result[0] = "15:10- 16:00 น.";
                result[1] = "13:50- 14:30 น.";
                break;
            case 10:
                result[0] = "16:00- 16:50 น.";
                result[1] = "14:30- 15:10 น.";
                break;
        }
        return result;
    }

    class PeriodDisplayViewHolder extends RecyclerView.ViewHolder {
        TextView dayTag;
        TextView periodNum;
        TextView subject;
        TextView subjectCode;
        TextView teacherList;

        TextView dataNormal;
        TextView dataReduced;
        Group group;
        View banner;
        View v;

        PeriodDisplayViewHolder(@NonNull View v) {
            super(v);
            dayTag = v.findViewById(R.id.dayTag);
            periodNum = v.findViewById(R.id.periodNum);
            subject = v.findViewById(R.id.header);
            subjectCode = v.findViewById(R.id.location);
            teacherList = v.findViewById(R.id.teacherList);
            dataNormal = v.findViewById(R.id.dataNormal);
            dataReduced = v.findViewById(R.id.dataReduced);
            group = v.findViewById(R.id.noClass);
            banner = v.findViewById(R.id.banner);
            this.v = v;
        }

        void setTag(int position) {
            v.setTag(String.valueOf(position));
        }
    }

    class PeriodDisplayAdapter extends RecyclerView.Adapter<PeriodDisplayViewHolder> {

        @NonNull
        @Override
        public PeriodDisplayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PeriodDisplayViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.widget_period_card_display, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PeriodDisplayViewHolder holder, int position) {
            bindData(holder, getData(position));
            holder.setTag(position);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        void bindData(final PeriodDisplayViewHolder holder, final Period period) {
            String[] tag = {"จันทร์", "อังคาร", "พุธ", "พฤหัสฯ", "ศุกร์"};
            boolean haveClass = period.type == Period.Type.HAVE_CLASS;
            holder.dayTag.setText(tag[day - 1]);
            holder.subject.setText(haveClass ? period.subject : Period.Type.getStringFromType(period.type));
            holder.subjectCode.setText(period.subjectCode);
            holder.periodNum.setText(String.valueOf(period.periodNum));
            holder.teacherList.setText(period.teacherList);
            String[] periodInfo = getPeriodInfo(period);
            holder.dataNormal.setText(periodInfo[0]);
            holder.dataReduced.setText(periodInfo[1]);
            holder.group.setVisibility(haveClass ? View.VISIBLE : View.GONE);
            if (!haveClass)
                holder.banner.setBackgroundColor(getResources().getColor(R.color.inactive));
            else if (day == 1)
                holder.banner.setBackgroundColor(getResources().getColor(R.color.colorMonday));
            else if (day == 2)
                holder.banner.setBackgroundColor(getResources().getColor(R.color.colorTuesday));
            else if (day == 3)
                holder.banner.setBackgroundColor(getResources().getColor(R.color.colorWednesday));
            else if (day == 4)
                holder.banner.setBackgroundColor(getResources().getColor(R.color.colorThursday));
            else if (day == 5)
                holder.banner.setBackgroundColor(getResources().getColor(R.color.colorFriday));
            holder.v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    intent.setClass(getApplication(), TeacherLocationDisplayActivity.class);
                    intent.putExtra("period", Integer.parseInt(String.valueOf(period.periodNum)));
                    startActivity(intent);
                }
            });
        }
    }
}