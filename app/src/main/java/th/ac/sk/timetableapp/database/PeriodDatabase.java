package th.ac.sk.timetableapp.database;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.Objects;

import th.ac.sk.timetableapp.model.Period;

import static th.ac.sk.timetableapp.model.Period.Type.LUNCH_PERIOD;
import static th.ac.sk.timetableapp.model.Period.Type.MILITARY_WORK;
import static th.ac.sk.timetableapp.model.Period.Type.NO_CLASS;
import static th.ac.sk.timetableapp.model.Period.Type.SCHOOL_CONFERENCE;

@SuppressLint("UseSparseArrays")
public class PeriodDatabase {
    private static final String TAG = "PeriodDatabase";

    private static PeriodDatabase ourInstance;
    private MutableLiveData<HashMap<Integer, Period>> periodDatabase;

    private PeriodDatabase() {

    }

    public static PeriodDatabase getInstance() {
        if (ourInstance == null) {
            ourInstance = new PeriodDatabase();
            ourInstance.periodDatabase = new MutableLiveData<>();
            ourInstance.periodDatabase.setValue(new HashMap<Integer, Period>());
            ourInstance.periodDatabase.observeForever(new Observer<Object>() {
                @Override
                public void onChanged(Object data) {
                    Log.w("observer", "Period Data Changed: " + data.toString());
                }
            });
        }
        return ourInstance;
    }

    public void updateToNullPeriod() {
        Log.d(TAG, "updateToNullDetail() called");
        removePeriod();
    }

    public void updateToPlaceholderPeriod() {
        Log.d(TAG, "updateToPlaceholderDetail() called");
        updateToNullPeriod();
        HashMap<Integer, Period> data = new HashMap<>();
        data.put(0, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", "4207", 1));
        data.put(1, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", "4207", 2));
        data.put(2, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", "4205", 3));
        data.put(3, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", "4205", 4));
        data.put(4, new Period(LUNCH_PERIOD, 5));
        data.put(5, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", "4403", 6));
        data.put(6, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", "4403", 7));
        data.put(7, new Period("ภาษาอังกฤษสร้างสรรค์ 4", "อ30216", "A, B", "4403/5204", 8));
        data.put(8, new Period("พระพุทธศาสนา 3", "ส32104", "บุญทรัพย์", "4403", 9));
        data.put(9, new Period("ศิลปะ 4 (ดนตรี)", "ศ32102", "อธิวัฒน์", "3301", 10));
        data.put(10, new Period(SCHOOL_CONFERENCE, 1));
        data.put(11, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", "4205", 2));
        data.put(12, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", "4205", 3));
        data.put(13, new Period("ภาษาไทย 4", "ท32102", "ทรงชัย", "4403", 4));
        data.put(14, new Period(LUNCH_PERIOD, 5));
        data.put(15, new Period("สุขศึกษาและพลศึกษา 4", "พ32102", "ดาวเรือง", "6401", 6));
        data.put(16, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", "4403", 7));
        data.put(17, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", "4307", 8));
        data.put(18, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", "4307", 9));
        data.put(19, new Period(NO_CLASS, 10));
        data.put(20, new Period("สังคมศึกษา 3", "ส32103", "กิตติธัช", "4403", 1));
        data.put(21, new Period("แบตมินตัน 2", "พ30204", "ศิริ", "โรงยิมชั้น 4", 2));
        data.put(22, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", "4307", 3));
        data.put(23, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", "4307", 4));
        data.put(24, new Period(LUNCH_PERIOD, 5));
        data.put(25, new Period("ภาษาอังกฤษสร้างสรรค์ 4", "อ30216", "A, B", "4403/5204", 6));
        data.put(26, new Period("กิจกรรมแนะแนว 4", "ก32902", "นิพนธ์", "4403", 7));
        data.put(27, new Period("ภาษาอังกฤษ 4", "อ32102", "อติพล", "4403", 8));
        data.put(28, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", "4403", 9));
        data.put(29, new Period(NO_CLASS, 10));
        data.put(30, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", "4207", 1));
        data.put(31, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", "4207", 2));
        data.put(32, new Period("ภาษาอังกฤษ 4", "อ32102", "อติพล", "4403", 3));
        data.put(33, new Period(MILITARY_WORK, 4));
        data.put(34, new Period(MILITARY_WORK, 5));
        data.put(35, new Period(MILITARY_WORK, 6));
        data.put(36, new Period(MILITARY_WORK, 7));
        data.put(37, new Period(MILITARY_WORK, 8));
        data.put(38, new Period(MILITARY_WORK, 9));
        data.put(39, new Period(MILITARY_WORK, 10));
        data.put(40, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", "4403", 1));
        data.put(41, new Period("ภาษาอังกฤษสร้างสรรค์ 4", "อ30216", "A, B", "4403/5204", 2));
        data.put(42, new Period("การงานอาชีพและเทคโนโลยี 2", "ง32102", "จิตรา", "4403", 3));
        data.put(43, new Period("การงานอาชีพและเทคโนโลยี 2", "ง32102", "จิตรา", "4403", 4));
        data.put(44, new Period(LUNCH_PERIOD, 5));
        data.put(45, new Period("โครงงานวิทยาศาสตร์สร้างสรรค์ 1", "ว30294", "ปิยมาศ, ปิยาภรณ์, ยลกร, ธนกร, บัญชาพร, อัญชานา", "4403", 6));
        data.put(46, new Period("โครงงานวิทยาศาสตร์สร้างสรรค์ 1", "ว30294", "ปิยมาศ, ปิยาภรณ์, ยลกร, ธนกร, บัญชาพร, อัญชานา", "4403", 7));
        data.put(47, new Period("สังคมศึกษา 3", "ส32103", "กิตติธัช", "4403", 8));
        data.put(48, new Period("ภาษาไทย 4", "ท32102", "ทรงชัย", "4403", 9));
        data.put(49, new Period(NO_CLASS, 10));
        periodDatabase.setValue(data);
    }

    public HashMap<Integer, Period> getPeriod() {
        return periodDatabase.getValue();
    }

    public void putPeriod(HashMap<Integer, Period> data) {
        periodDatabase.setValue(data);
    }

    public void putPeriod(int position, Period data) {
        HashMap<Integer, Period> parentData = getPeriod();
        parentData.put(position, data);
        putPeriod(parentData);
    }

    public void removePeriod() {
        HashMap<Integer, Period> data = new HashMap<>();
        for (int i = 0; i < 50; i++) data.put(i, new Period(NO_CLASS, (i % 10) + 1));
        putPeriod(data);
    }

    public void setObserver(LifecycleOwner owner, Observer<? super HashMap<Integer, Period>> observer) {
        periodDatabase.observe(owner, observer);
    }
}
