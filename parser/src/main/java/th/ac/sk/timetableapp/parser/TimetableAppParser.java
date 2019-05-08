package th.ac.sk.timetableapp.parser;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import th.ac.sk.timetableapp.model.Period;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;

import static th.ac.sk.timetableapp.model.Period.Type.NO_CLASS;

@SuppressLint("UseSparseArrays")
public class TimetableAppParser {
    public static void main(String[] args) {
        HashMap<Integer, Period> period = new HashMap<>();
        for (int i = 0; i < 50; i++) period.put(i, new Period(NO_CLASS, (i % 10) + 1));
        period.put(0, new Period("ทดสอบ", "ท11111", "รายชื่อครู", "ห้อง", 1));
        HashMap<Integer, HashMap<Integer, TeacherLocation>> location = new HashMap<>();
        HashMap<Integer, TeacherDetail> detail = new HashMap<>();

        String s = DataParser.packString(DataParser.extractTeacherLocation(location, detail), DataParser.extractPeriod(period));
        try {
            System.out.println("teacherfinder://pongtaewin.wordpress.com/import?d=" + URLEncoder.encode(s, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
