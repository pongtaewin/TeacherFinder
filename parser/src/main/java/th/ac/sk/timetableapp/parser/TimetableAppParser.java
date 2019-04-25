package th.ac.sk.timetableapp.parser;

import android.annotation.SuppressLint;

import java.util.HashMap;

import th.ac.sk.timetableapp.model.Period;
import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;

import static th.ac.sk.timetableapp.model.Period.Type.LUNCH_PERIOD;
import static th.ac.sk.timetableapp.model.Period.Type.NO_CLASS;
import static th.ac.sk.timetableapp.model.Period.Type.SCHOOL_CONFERENCE;

@SuppressLint("UseSparseArrays")
public class TimetableAppParser {
    public static void main(String[] args) {
        /*HashMap<Integer, Period> data = new HashMap<>();
        data.put(0, new Period("ฟิสิกส์เข้มข้น 4", "ว30214", "ปิยาภรณ์", 1));
        data.put(1, new Period("ฟิสิกส์เข้มข้น 4", "ว30214", "ปิยาภรณ์", 2));
        data.put(2, new Period("", "อ30211", "TC", 3));
        data.put(3, new Period("", "ท33101", "นัฐพงศ์", 4));
        data.put(4, new Period(LUNCH_PERIOD, 5));
        data.put(5, new Period("", "อ33101", "วิมลรัตน์", 6));
        data.put(6, new Period("คณิตศาสตร์ก้าวหน้า 3", "ค33223", "ธีรเดช", 7));
        data.put(7, new Period("คณิตศาสตร์ก้าวหน้า 3", "ค33223", "ธีรเดช", 8));
        data.put(8, new Period(NO_CLASS, 9));
        data.put(9, new Period(NO_CLASS, 10));

        data.put(10, new Period("เคมีเข้มข้น 4", "ว30234", "โพธิวัฒน์พล", 1));
        data.put(11, new Period("เคมีเข้มข้น 4", "ว30234", "โพธิวัฒน์พล", 2));
        data.put(12, new Period("สังคมศึกษา 5", "ส33101", "วรุตม์", 3));
        data.put(13, new Period("", "พ30221", "มณเฑียร", 4));
        data.put(14, new Period(LUNCH_PERIOD, 5));
        data.put(15, new Period("", "พ33101", "สุขะชัย", 6));
        data.put(16, new Period("", "อ30205", "อาทิตยา", 7));
        data.put(17, new Period("", "ท33101", "นัฐพงศ์", 8));
        data.put(18, new Period("คณิตศาสตร์ก้าวหน้า 3", "ค33223", "ธีรเดช", 9));
        data.put(19, new Period(NO_CLASS, 10));

        data.put(20, new Period(SCHOOL_CONFERENCE, 1));
        data.put(21, new Period("", "ส33108", "ภัทรวดี", 2));
        data.put(22, new Period("", "อ33101", "วิมลรัตน์", 3));
        data.put(23, new Period("", "อ30205", "อาทิตตยา", 4));
        data.put(24, new Period(LUNCH_PERIOD, 5));
        data.put(25, new Period("ชีววิทยาเข้มข้น 4", "ว30254", "หัสวนัส", 6));
        data.put(26, new Period("ชีววิทยาเข้มข้น 4", "ว30254", "ห้สวนัส", 7));
        data.put(27, new Period("ฟิสิกส์เข้มข้น 4", "ว30214", "ปิยาภรณ์", 8));
        data.put(28, new Period(NO_CLASS, 9));
        data.put(29, new Period(NO_CLASS, 10));

        data.put(30, new Period("ชีววิทยาเข้มข้น 4", "ว30254", "หัสวนัส", 1));
        data.put(31, new Period("ชีววิทยาเข้มข้น 4", "ว30254", "หัสวนัส", 2));
        data.put(32, new Period("", "ก33903", "วรชัย", 3));
        data.put(33, new Period("", "ก33903", "วรชัย", 4));
        data.put(34, new Period(LUNCH_PERIOD, 5));
        data.put(35, new Period("เคมีเข้มข้น 4", "ว30234", "โพธิ์วัฒนพล", 6));
        data.put(36, new Period("เคมีเข้มข้น 4", "ว30234", "โพธิ์วัฒนพล", 7));
        data.put(37, new Period(NO_CLASS, 8));
        data.put(38, new Period(NO_CLASS, 9));
        data.put(39, new Period(NO_CLASS, 10));

        data.put(40, new Period("ชีววิทยาเข้มข้น 4", "ส33108", "ภัทรวดี", 1));
        data.put(41, new Period("ชีววิทยาเข้มข้น 4", "ศ33101", "รัชนู", 2));
        data.put(42, new Period("คณิตศาสตร์ก้าวหน้า 3", "ค33223", "ธีรเดช", 3));
        data.put(43, new Period("คณิตศาสตร์ก้าวหน้า 3", "ค33223", "ธีรเดช", 4));
        data.put(44, new Period(LUNCH_PERIOD, 5));
        data.put(45, new Period("", "ว30295", "ปิยมาศ, สุภาภรณ์, หัสวนัส", 6));
        data.put(46, new Period("", "ว30295", "ปิยมาศ, สุภาภรณ์, หัสวนัส", 7));
        data.put(47, new Period(NO_CLASS, 8));
        data.put(48, new Period(NO_CLASS, 9));
        data.put(49, new Period(NO_CLASS, 10));


        DataParser.extractPeriod(data);
        HashMap<Integer, HashMap<Integer, TeacherLocation>> location = new HashMap<>();
        HashMap<Integer, TeacherDetail> detail = new HashMap<>();
        location.put(1, new HashMap<Integer, TeacherLocation>());
        detail.put(1, new TeacherDetail(1, "ทดสอบ", "หนักมาก"));
        System.out.println(DataParser.packString(DataParser.extractPeriod(data),DataParser.extractTeacherLocation(location, detail)));*/
    }
}
