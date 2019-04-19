package th.ac.sk.timetableapp.database;

import android.util.SparseArray;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import th.ac.sk.timetableapp.model.Period;

import static th.ac.sk.timetableapp.model.Period.Type.LUNCH_PERIOD;
import static th.ac.sk.timetableapp.model.Period.Type.MILITARY_WORK;
import static th.ac.sk.timetableapp.model.Period.Type.NO_CLASS;
import static th.ac.sk.timetableapp.model.Period.Type.SCHOOL_CONFERENCE;


public class PeriodDatabase {
    private static final int PLACEHOLDER_M502_DATA = 502;
    private static final int PLACEHOLDER_NULL_DATA = 73;
    private static final int CURRENT_DATA = 941;

    private static PeriodDatabase ourInstance;
    private MutableLiveData<SparseArray<SparseArray<Period>>> liveDatabase;

    private PeriodDatabase() {

    }

    public static PeriodDatabase getInstance() {
        if (ourInstance == null) {
            ourInstance = new PeriodDatabase();
            ourInstance.liveDatabase = new MutableLiveData<>();
            ourInstance.liveDatabase.setValue(new SparseArray<SparseArray<Period>>());
            ourInstance.initPlaceholder();
        }
        return ourInstance;
    }


    private void initPlaceholder() {

        if (getDatabase().get(PLACEHOLDER_NULL_DATA) == null) {
            SparseArray<Period> data = new SparseArray<>();
            for (int i = 0; i < 50; i++)
                data.put(i, new Period(Period.Type.NO_CLASS, (i % 10) + 1));
            putToDatabase(PLACEHOLDER_NULL_DATA, data);

        }

        if (getDatabase().get(PLACEHOLDER_M502_DATA) == null) {
            SparseArray<Period> data = new SparseArray<>();
            data.put(0, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", 1));
            data.put(1, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", 2));
            data.put(2, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", 3));
            data.put(3, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", 4));
            data.put(4, new Period(LUNCH_PERIOD, 5));
            data.put(5, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", 6));
            data.put(6, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", 7));
            data.put(7, new Period("ภาษาอังกฤษสร้างสรรค์ 4", "อ30216", "A, B", 8));
            data.put(8, new Period("พระพุทธศาสนา 3", "ส32104", "บุญทรัพย์", 9));
            data.put(9, new Period("ศิลปะ 4 (ดนตรี)", "ศ32102", "อธิวัฒน์", 10));
            data.put(10, new Period(SCHOOL_CONFERENCE, 1));
            data.put(11, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", 2));
            data.put(12, new Period("เคมีเข้มข้น 3", "ว30233", "ปิยมาศ", 3));
            data.put(13, new Period("ภาษาไทย 4", "ท32102", "ทรงชัย", 4));
            data.put(14, new Period(LUNCH_PERIOD, 5));
            data.put(15, new Period("สุขศึกษาและพลศึกษา 4", "พ32102", "ดาวเรือง", 6));
            data.put(16, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", 7));
            data.put(17, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", 8));
            data.put(18, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", 9));
            data.put(19, new Period(NO_CLASS, 10));
            data.put(20, new Period("สังคมศึกษา 3", "ส32103", "กิตติธัช", 1));
            data.put(21, new Period("แบตมินตัน 2", "พ30204", "ศิริ", 2));
            data.put(22, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", 3));
            data.put(23, new Period("ชีววิทยาเข้มข้น 3", "ว30253", "หัสวนัส", 4));
            data.put(24, new Period(LUNCH_PERIOD, 5));
            data.put(25, new Period("ภาษาอังกฤษสร้างสรรค์ 4", "อ30216", "A, B", 6));
            data.put(26, new Period("กิจกรรมแนะแนว 4", "ก32902", "นิพนธ์", 7));
            data.put(27, new Period("ภาษาอังกฤษ 4", "อ32102", "อติพล", 8));
            data.put(28, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", 9));
            data.put(29, new Period(NO_CLASS, 10));
            data.put(30, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", 1));
            data.put(31, new Period("ฟิสิกส์เข้มข้น 3", "ว30213", "สุมิตร", 2));
            data.put(32, new Period("ภาษาอังกฤษ 4", "อ32102", "อติพล", 3));
            data.put(33, new Period(MILITARY_WORK, 4));
            data.put(34, new Period(MILITARY_WORK, 5));
            data.put(35, new Period(MILITARY_WORK, 6));
            data.put(36, new Period(MILITARY_WORK, 7));
            data.put(37, new Period(MILITARY_WORK, 8));
            data.put(38, new Period(MILITARY_WORK, 9));
            data.put(39, new Period(MILITARY_WORK, 10));
            data.put(40, new Period("คณิตศาสตร์ก้าวหน้า 2", "ค32224", "ธนดล", 1));
            data.put(41, new Period("ภาษาอังกฤษสร้างสรรค์ 4", "อ30216", "A, B", 2));
            data.put(42, new Period("การงานอาชีพและเทคโนโลยี 2", "ง32102", "จิตรา", 3));
            data.put(43, new Period("การงานอาชีพและเทคโนโลยี 2", "ง32102", "จิตรา", 4));
            data.put(44, new Period(LUNCH_PERIOD, 5));
            data.put(45, new Period("โครงงานวิทยาศาสตร์สร้างสรรค์ 1", "ว30294", "ปิยมาศ, ปิยาภรณ์, ยลกร, ธนกร, บัญชาพร, อัญชานา", 6));
            data.put(46, new Period("โครงงานวิทยาศาสตร์สร้างสรรค์ 1", "ว30294", "ปิยมาศ, ปิยาภรณ์, ยลกร, ธนกร, บัญชาพร, อัญชานา", 7));
            data.put(47, new Period("สังคมศึกษา 3", "ส32103", "กิตติธัช", 8));
            data.put(48, new Period("ภาษาไทย 4", "ท32102", "ทรงชัย", 9));
            data.put(49, new Period(NO_CLASS, 10));
            putToDatabase(PLACEHOLDER_M502_DATA, data);
        }
        if (getDatabase().get(CURRENT_DATA) == null) copyFrom(PLACEHOLDER_NULL_DATA);

    }

    private void copyFrom(int fromID) {
        SparseArray<Period> fromArray = getDatabase().get(fromID);
        SparseArray<Period> toArray = new SparseArray<>();
        for (int i = 0; i < fromArray.size(); i++)
            toArray.put(fromArray.keyAt(i), fromArray.valueAt(i));
        putToDatabase(CURRENT_DATA, toArray);
    }

    public SparseArray<Period> getCurrentData() {
        return getDatabase().get(CURRENT_DATA);
    }

    public void updateToNullPeriod() {
        copyFrom(PLACEHOLDER_NULL_DATA);
    }

    public void updateToPlaceholderPeriod() {
        copyFrom(PLACEHOLDER_M502_DATA);
    }

    private SparseArray<SparseArray<Period>> getDatabase() {
        return liveDatabase.getValue();
    }

    private void putToDatabase(int dest, SparseArray<Period> value) {
        SparseArray<SparseArray<Period>> db = getDatabase();
        db.put(dest, value);
        liveDatabase.setValue(db);
    }

    public void putToCurrentData(int key, Period value) {
        SparseArray<Period> data = getDatabase().get(CURRENT_DATA);
        data.put(key, value);
        putToCurrentData(data);
    }

    public void putToCurrentData(SparseArray<Period> data) {
        putToDatabase(CURRENT_DATA, data);
    }

    public void setObserver(LifecycleOwner owner, Observer<? super SparseArray<SparseArray<Period>>> observer) {
        liveDatabase.observe(owner, observer);
    }
}
