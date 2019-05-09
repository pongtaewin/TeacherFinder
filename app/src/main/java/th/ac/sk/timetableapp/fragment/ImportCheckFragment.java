package th.ac.sk.timetableapp.fragment;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import th.ac.sk.timetableapp.MasterActivity;
import th.ac.sk.timetableapp.R;
import th.ac.sk.timetableapp.database.DataSaveHandler;
import th.ac.sk.timetableapp.model.Period;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.parser.DataParser;


public class ImportCheckFragment extends Fragment {
    private static String time;
    private static String data;
    private static String prefix;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        time = requireArguments().getString("time");
        data = requireArguments().getString("data");
        prefix = requireArguments().getString("prefix");
        return inflater.inflate(R.layout.fragment_import_check, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.notes)).setText(String.format("เวลาที่ส่งออก %s\nเขียนทับด้วยข้อมูลนี้หรือไม่", time));
        ((TextView) view.findViewById(R.id.data)).setText(convertData());
        ((TextView) view.findViewById(R.id.data)).setMovementMethod(new ScrollingMovementMethod());
        view.findViewById(R.id.submit).setOnClickListener(new ConfirmImportClickListener());
    }


    class ConfirmImportClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            DataSaveHandler.importPeriodData(DataParser.extractPeriodFromPackedString(data), true);
            DataSaveHandler.importTeacherLocationData(DataParser.extractTeacherLocationFromPackedString(data), true);
            requireActivity().setResult(MasterActivity.RESULT_IMPORT_SUCCESS);
            requireActivity().finish();
        }
    }

    private static String convertData() {
        HashMap<Integer, Period> periodData = Objects.requireNonNull(
                DataParser.parsePeriod(DataParser.extractPeriodFromPackedString(data)));
        DataParser.TeacherLocationDatabaseFormat teacherData = Objects.requireNonNull(
                DataParser.parseTeacherLocation(DataParser.extractTeacherLocationFromPackedString(data)));
        StringBuilder builder = new StringBuilder();
        String[] tag = {"จันทร์", "อังคาร", "พุธ", "พฤหัสบดี", "ศุกร์"};
        builder.append(prefix).append(System.lineSeparator()).append(System.lineSeparator())
                .append("<<< ตารางเรียน >>>").append(System.lineSeparator()).append(System.lineSeparator());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                Period p = Objects.requireNonNull(periodData.get((10 * i) + j));
                builder.append("วัน").append(tag[i]).append(" คาบที่ ").append(p.periodNum).append(System.lineSeparator());
                if (p.type == Period.Type.HAVE_CLASS) {
                    builder.append(p.subject).append(System.lineSeparator())
                            .append("รหัส : ").append(p.subjectCode).append(System.lineSeparator())
                            .append("เรียนที่ : ").append(p.room).append(System.lineSeparator())
                            .append("ผู้สอน : ").append(p.teacherList).append(System.lineSeparator());
                } else {
                    builder.append(Period.Type.getStringFromType(p.type)).append(System.lineSeparator());
                }
                builder.append(System.lineSeparator());
            }
            builder.append(System.lineSeparator()).append(System.lineSeparator());
        }
        builder.append(System.lineSeparator()).append("<<< สถานที่สอนของอาจารย์ >>>").append(System.lineSeparator()).append(System.lineSeparator());
        HashMap<Integer, TeacherDetail> detail = teacherData.getTeacherDetail();
        HashMap<Integer, HashMap<Integer, TeacherLocation>> location = teacherData.getTeacherLocation();
        ArrayList<Integer> keyArray = new ArrayList<>(location.keySet());
        Collections.sort(keyArray);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                builder.append("วัน").append(tag[i]).append(" คาบที่ ").append(j + 1).append(":").append(System.lineSeparator());
                boolean x = true;
                for (int teacherId : keyArray) {
                    TeacherLocation data = Objects.requireNonNull(location.get(teacherId)).get(i * 10 + j);
                    TeacherDetail thisDetail = Objects.requireNonNull(detail.get(teacherId));
                    if (data != null) {
                        builder.append(thisDetail.name).append(" ").append(thisDetail.surname).append(" : ").append(data.location)
                                .append(" [").append(data.classroom).append("]").append(System.lineSeparator());
                        x = false;
                    }
                }
                if (x) builder.append("(ว่าง)").append(System.lineSeparator());
                builder.append(System.lineSeparator());
            }
            builder.append(System.lineSeparator()).append(System.lineSeparator());
        }
        return builder.toString();
    }
}
