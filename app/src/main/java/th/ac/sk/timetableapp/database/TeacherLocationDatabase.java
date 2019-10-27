package th.ac.sk.timetableapp.database;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.parser.DataParser;

@SuppressLint("UseSparseArrays")
public class TeacherLocationDatabase {
    private static final String TAG = "TeacherLocationDatabase";
    private static TeacherLocationDatabase ourInstance;
    private MutableLiveData<HashMap<Integer, HashMap<Integer, TeacherLocation>>> locationDatabase;
    private MutableLiveData<HashMap<Integer, TeacherDetail>> detailDatabase;

    private TeacherLocationDatabase() {
    }

    static boolean checkIfCorrectlyImported(DataParser.TeacherLocationDatabaseFormat data) {
        return data.getTeacherDetail() != null && data.getTeacherLocation() != null;
    }

    public static TeacherLocationDatabase getInstance() {
        if (ourInstance == null) {
            ourInstance = new TeacherLocationDatabase();
            ourInstance.locationDatabase = new MutableLiveData<>();
            ourInstance.locationDatabase.setValue(new HashMap<Integer, HashMap<Integer, TeacherLocation>>());
            ourInstance.locationDatabase.observeForever(new Observer<Object>() {
                @Override
                public void onChanged(Object data) {
                    Log.w("observer", "TeacherLocation Data Changed: " + data.toString());
                }
            });
            ourInstance.detailDatabase = new MutableLiveData<>();
            ourInstance.detailDatabase.setValue(new HashMap<Integer, TeacherDetail>());
            ourInstance.detailDatabase.observeForever(new Observer<Object>() {
                @Override
                public void onChanged(Object data) {
                    Log.w("observer", "TeacherDetail Data Changed: " + data.toString());
                }
            });
            //ourInstance.updateToNullLocation();
            //ourInstance.updateToNullDetail();
            ourInstance.updateToPlaceholderLocation();
            ourInstance.updateToPlaceholderDetail();
        }
        return ourInstance;
    }

    public static int getTeacherCount() {
        return getInstance().getDetailHash().size();
    }

    public static int getNewTeacherID() {
        HashMap<Integer, TeacherDetail> detailMap = getInstance().getDetailHash();
        int size = detailMap.size();
        if (size == 0) return 1;
        int max = 1;
        for (HashMap.Entry<Integer, TeacherDetail> detailEntry : detailMap.entrySet()) {
            max = Math.max(max, detailEntry.getValue().id);
        }
        return max + 1;
    }

    void updateToNullLocation() {
        Log.d(TAG, "updateToNullLocation() called");
        removeLocationHash();
    }

    void updateToPlaceholderLocation() {
        Log.d(TAG, "updateToPlaceholderLocation() called");
        updateToNullLocation();
        for (int i = 1; i <= 20; i++) updateToPlaceholderLocation(i);
    }

    private void updateToPlaceholderLocation(int i) {
        Log.d(TAG, "updateToPlaceholderLocation() called with: i = [" + i + "]");
        switch (i) {
            case 1:
                putLocationDataAt(1, 0, new TeacherLocation(1, "ม.502", "4207", 0));
                putLocationDataAt(1, 1, new TeacherLocation(1, "ม.502", "4207", 1));
                putLocationDataAt(1, 5, new TeacherLocation(1, "ม.501", "4207", 5));
                putLocationDataAt(1, 6, new TeacherLocation(1, "ม.501", "4207", 6));
                putLocationDataAt(1, 22, new TeacherLocation(1, "ม.501", "4207", 22));
                putLocationDataAt(1, 23, new TeacherLocation(1, "ม.501", "4207", 23));
                putLocationDataAt(1, 27, new TeacherLocation(1, "ม.503", "4207", 27));
                putLocationDataAt(1, 28, new TeacherLocation(1, "ม.503", "4207", 28));
                putLocationDataAt(1, 30, new TeacherLocation(1, "ม.502", "4403", 30));
                putLocationDataAt(1, 31, new TeacherLocation(1, "ม.502", "4403", 31));
                putLocationDataAt(1, 40, new TeacherLocation(1, "ม.503", "4404", 40));
                putLocationDataAt(1, 41, new TeacherLocation(1, "ม.503", "4404", 41));
                break;
            case 2:
                putLocationDataAt(2, 2, new TeacherLocation(2, "ม.502", "4205", 2));
                putLocationDataAt(2, 3, new TeacherLocation(2, "ม.502", "4205", 3));
                putLocationDataAt(2, 8, new TeacherLocation(2, "ม.503", "4404", 8));
                putLocationDataAt(2, 9, new TeacherLocation(2, "ม.503", "4404", 9));
                putLocationDataAt(2, 11, new TeacherLocation(2, "ม.502", "4403", 11));
                putLocationDataAt(2, 12, new TeacherLocation(2, "ม.502", "4403", 12));
                putLocationDataAt(2, 15, new TeacherLocation(2, "ม.501", "4205", 15));
                putLocationDataAt(2, 16, new TeacherLocation(2, "ม.501", "4205", 16));
                putLocationDataAt(2, 20, new TeacherLocation(2, "ม.503", "4205", 20));
                putLocationDataAt(2, 21, new TeacherLocation(2, "ม.503", "4205", 21));
                putLocationDataAt(2, 27, new TeacherLocation(2, "ม.501", "4402", 27));
                putLocationDataAt(2, 28, new TeacherLocation(2, "ม.501", "4402", 28));
                putLocationDataAt(2, 30, new TeacherLocation(2, "ม.501", "4402", 30));
                putLocationDataAt(2, 31, new TeacherLocation(2, "ม.501", "4402", 31));
                putLocationDataAt(2, 42, new TeacherLocation(2, "ม.503", "4205", 42));
                putLocationDataAt(2, 43, new TeacherLocation(2, "ม.503", "4205", 43));
                putLocationDataAt(2, 45, new TeacherLocation(2, "ม.502", "4403", 45));
                putLocationDataAt(2, 46, new TeacherLocation(2, "ม.502", "4403", 46));
                break;
            case 3:
                putLocationDataAt(3, 5, new TeacherLocation(3, "ม.502", "4403", 5));
                putLocationDataAt(3, 6, new TeacherLocation(3, "ม.502", "4403", 6));
                putLocationDataAt(3, 12, new TeacherLocation(3, "ม.501", "4402", 12));
                putLocationDataAt(3, 13, new TeacherLocation(3, "ม.501", "4402", 13));
                putLocationDataAt(3, 16, new TeacherLocation(3, "ม.502", "4403", 16));
                putLocationDataAt(3, 25, new TeacherLocation(3, "ม.501", "4402", 25));
                putLocationDataAt(3, 28, new TeacherLocation(3, "ม.502", "4403", 28));
                putLocationDataAt(3, 32, new TeacherLocation(3, "ม.501", "4402", 32));
                putLocationDataAt(3, 40, new TeacherLocation(3, "ม.502", "4403", 40));
                putLocationDataAt(3, 41, new TeacherLocation(3, "ม.501", "4402", 41));
                break;
            case 4:
                putLocationDataAt(4, 5, new TeacherLocation(4, "ม.503", "4404", 5));
                putLocationDataAt(4, 7, new TeacherLocation(4, "ม.502", "4403", 7));
                putLocationDataAt(4, 8, new TeacherLocation(4, "ม.501", "4402", 8));
                putLocationDataAt(4, 16, new TeacherLocation(4, "ม.503", "4404", 16));
                putLocationDataAt(4, 22, new TeacherLocation(4, "ม.503", "4404", 22));
                putLocationDataAt(4, 25, new TeacherLocation(4, "ม.502", "4403", 25));
                putLocationDataAt(4, 26, new TeacherLocation(4, "ม.501", "4402", 26));
                putLocationDataAt(4, 40, new TeacherLocation(4, "ม.501", "4402", 40));
                putLocationDataAt(4, 41, new TeacherLocation(4, "ม.502", "4403", 41));
                break;
            case 5:
                putLocationDataAt(5, 5, new TeacherLocation(5, "ม.503", "5204", 5));
                putLocationDataAt(5, 7, new TeacherLocation(5, "ม.502", "5204", 7));
                putLocationDataAt(5, 8, new TeacherLocation(5, "ม.501", "5204", 8));
                putLocationDataAt(5, 16, new TeacherLocation(5, "ม.503", "5204", 16));
                putLocationDataAt(5, 22, new TeacherLocation(5, "ม.503", "5204", 22));
                putLocationDataAt(5, 25, new TeacherLocation(5, "ม.502", "5204", 25));
                putLocationDataAt(5, 26, new TeacherLocation(5, "ม.501", "5204", 26));
                putLocationDataAt(5, 40, new TeacherLocation(5, "ม.501", "5204", 40));
                putLocationDataAt(5, 41, new TeacherLocation(5, "ม.502", "5204", 41));
                break;
            case 6:
                putLocationDataAt(6, 7, new TeacherLocation(6, "ม.501", "4402", 7));
                putLocationDataAt(6, 8, new TeacherLocation(6, "ม.502", "4403", 8));
                putLocationDataAt(6, 18, new TeacherLocation(6, "ม.503", "4404", 18));
                break;
            case 7:
                putLocationDataAt(7, 6, new TeacherLocation(7, "ม.503", "3301", 6));
                putLocationDataAt(7, 9, new TeacherLocation(7, "ม.502", "3301", 9));
                putLocationDataAt(7, 48, new TeacherLocation(7, "ม.501", "3301", 48));
                break;
            case 8:
                putLocationDataAt(8, 11, new TeacherLocation(8, "ม.501", "4402", 11));
                putLocationDataAt(8, 13, new TeacherLocation(8, "ม.502", "4403", 13));
                putLocationDataAt(8, 47, new TeacherLocation(8, "ม.501", "4402", 47));
                putLocationDataAt(8, 48, new TeacherLocation(8, "ม.502", "4403", 48));
                break;
            case 9:
                putLocationDataAt(9, 9, new TeacherLocation(9, "ม.501", "6403", 9));
                putLocationDataAt(9, 15, new TeacherLocation(9, "ม.502", "6403", 15));
                putLocationDataAt(9, 45, new TeacherLocation(9, "ม.503", "6403", 45));
                break;
            case 10:
                putLocationDataAt(10, 8, new TeacherLocation(10, "ม.503", "4404", 8));
                putLocationDataAt(10, 9, new TeacherLocation(10, "ม.503", "4404", 9));
                putLocationDataAt(10, 17, new TeacherLocation(10, "ม.502", "4309", 17));
                putLocationDataAt(10, 18, new TeacherLocation(10, "ม.502", "4309", 18));
                putLocationDataAt(10, 22, new TeacherLocation(10, "ม.502", "4403", 22));
                putLocationDataAt(10, 23, new TeacherLocation(10, "ม.502", "4403", 23));
                break;
            case 11:
                putLocationDataAt(11, 2, new TeacherLocation(11, "ม.501", "4402", 2));
                putLocationDataAt(11, 17, new TeacherLocation(11, "ม.503", "4404", 17));
                putLocationDataAt(11, 18, new TeacherLocation(11, "ม.501", "4402", 18));
                putLocationDataAt(11, 20, new TeacherLocation(11, "ม.502", "4403", 20));
                putLocationDataAt(11, 46, new TeacherLocation(11, "ม.503", "4404", 46));
                putLocationDataAt(11, 47, new TeacherLocation(11, "ม.502", "4403", 47));
                break;
            case 12:
                putLocationDataAt(12, 7, new TeacherLocation(12, "ม.503", "7005", 7));
                putLocationDataAt(12, 17, new TeacherLocation(12, "ม.501", "7005", 17));
                putLocationDataAt(12, 21, new TeacherLocation(12, "ม.502", "7005", 21));
                break;
            case 13:
                putLocationDataAt(13, 26, new TeacherLocation(13, "ม.502", "4403", 26));
                putLocationDataAt(13, 46, new TeacherLocation(13, "ม.501", "4402", 46));
                putLocationDataAt(13, 48, new TeacherLocation(13, "ม.503", "4404", 48));
                break;
            case 14:
                putLocationDataAt(14, 3, new TeacherLocation(14, "ม.501", "4402", 3));
                putLocationDataAt(14, 11, new TeacherLocation(14, "ม.503", "4404", 11));
                putLocationDataAt(14, 26, new TeacherLocation(14, "ม.503", "4404", 26));
                putLocationDataAt(14, 27, new TeacherLocation(14, "ม.502", "4403", 27));
                putLocationDataAt(14, 32, new TeacherLocation(14, "ม.502", "4403", 32));
                putLocationDataAt(14, 45, new TeacherLocation(14, "ม.501", "4402", 45));
                break;
            case 15:
                putLocationDataAt(15, 2, new TeacherLocation(15, "ม.503", "4404", 2));
                putLocationDataAt(15, 3, new TeacherLocation(15, "ม.503", "4404", 3));
                putLocationDataAt(15, 20, new TeacherLocation(15, "ม.501", "4402", 20));
                putLocationDataAt(15, 21, new TeacherLocation(15, "ม.501", "4402", 21));
                putLocationDataAt(15, 42, new TeacherLocation(15, "ม.502", "4403", 42));
                putLocationDataAt(15, 43, new TeacherLocation(15, "ม.502", "4403", 43));
                break;
            case 16:
                putLocationDataAt(16, 8, new TeacherLocation(16, "ม.503", "4404", 8));
                putLocationDataAt(16, 9, new TeacherLocation(16, "ม.503", "4404", 9));
                putLocationDataAt(16, 27, new TeacherLocation(16, "ม.501", "4402", 27));
                putLocationDataAt(16, 28, new TeacherLocation(16, "ม.501", "4402", 28));
                putLocationDataAt(16, 45, new TeacherLocation(16, "ม.502", "4403", 45));
                putLocationDataAt(16, 46, new TeacherLocation(16, "ม.502", "4403", 46));
                break;
            case 17:
                putLocationDataAt(17, 27, new TeacherLocation(17, "ม.501", "4402", 27));
                putLocationDataAt(17, 28, new TeacherLocation(17, "ม.501", "4402", 28));
                putLocationDataAt(17, 45, new TeacherLocation(17, "ม.502", "4403", 45));
                putLocationDataAt(17, 46, new TeacherLocation(17, "ม.502", "4403", 46));
                break;
            case 18:
                putLocationDataAt(18, 8, new TeacherLocation(18, "ม.503", "4404", 8));
                putLocationDataAt(18, 9, new TeacherLocation(18, "ม.503", "4404", 9));
                putLocationDataAt(18, 27, new TeacherLocation(18, "ม.501", "4402", 27));
                putLocationDataAt(18, 28, new TeacherLocation(18, "ม.501", "4402", 28));
                putLocationDataAt(18, 45, new TeacherLocation(18, "ม.502", "4403", 45));
                putLocationDataAt(18, 46, new TeacherLocation(18, "ม.502", "4403", 46));
                break;
            case 19:
                putLocationDataAt(19, 0, new TeacherLocation(19, "ม.501", "4309", 0));
                putLocationDataAt(19, 1, new TeacherLocation(19, "ม.501", "4309", 1));
                putLocationDataAt(19, 8, new TeacherLocation(19, "ม.503", "4404", 8));
                putLocationDataAt(19, 9, new TeacherLocation(19, "ม.503", "4404", 9));
                putLocationDataAt(19, 12, new TeacherLocation(19, "ม.503", "4309", 12));
                putLocationDataAt(19, 13, new TeacherLocation(19, "ม.503", "4309", 13));
                putLocationDataAt(19, 27, new TeacherLocation(19, "ม.501", "4402", 27));
                putLocationDataAt(19, 28, new TeacherLocation(19, "ม.501", "4402", 28));
                putLocationDataAt(19, 31, new TeacherLocation(19, "ม.503", "4404", 31));
                putLocationDataAt(19, 32, new TeacherLocation(19, "ม.503", "4404", 32));
                putLocationDataAt(19, 42, new TeacherLocation(19, "ม.501", "4402", 42));
                putLocationDataAt(19, 43, new TeacherLocation(19, "ม.501", "4402", 43));
                putLocationDataAt(19, 45, new TeacherLocation(19, "ม.502", "4403", 45));
                putLocationDataAt(19, 46, new TeacherLocation(19, "ม.502", "4403", 46));
                break;
            case 20:
                putLocationDataAt(20, 8, new TeacherLocation(20, "ม.503", "4404", 8));
                putLocationDataAt(20, 9, new TeacherLocation(20, "ม.503", "4404", 9));
                putLocationDataAt(20, 27, new TeacherLocation(20, "ม.501", "4402", 27));
                putLocationDataAt(20, 28, new TeacherLocation(20, "ม.501", "4402", 28));
                putLocationDataAt(20, 45, new TeacherLocation(20, "ม.502", "4403", 45));
                putLocationDataAt(20, 46, new TeacherLocation(20, "ม.502", "4403", 46));
                break;
        }
    }

    public HashMap<Integer, HashMap<Integer, TeacherLocation>> getLocationHash() {
        return locationDatabase.getValue();
    }

    public HashMap<Integer, TeacherLocation> getLocationHashAt(int teacherId) {
        return getLocationHash().get(teacherId);
    }

    public ArrayList<TeacherLocation> getLocationListAt(int teacherId) {
        ArrayList<TeacherLocation> list = new ArrayList<>(getLocationHashAt(teacherId).values());
        Collections.sort(list, new Comparator<TeacherLocation>() {
            @Override
            public int compare(TeacherLocation location1, TeacherLocation location2) {
                return Integer.compare(location1.key, location2.key);
            }
        });
        return list;
    }

    public TeacherLocation getLocationDataAt(int teacherId, int key) {
        return getLocationHashAt(teacherId).get(key);
    }

    public void putLocationHash(HashMap<Integer, HashMap<Integer, TeacherLocation>> data) {
        locationDatabase.setValue(data);
    }

    public void putLocationHashAt(int teacherId, HashMap<Integer, TeacherLocation> data) {
        HashMap<Integer, HashMap<Integer, TeacherLocation>> parentData = getLocationHash();
        parentData.put(teacherId, data);
        putLocationHash(parentData);
    }

    public void putLocationDataAt(int teacherId, int key, TeacherLocation data) {
        HashMap<Integer, TeacherLocation> parentData = getLocationHashAt(teacherId);
        if (parentData == null) parentData = new HashMap<>();
        parentData.put(key, data);
        putLocationHashAt(teacherId, parentData);

    }

    public void putLocationListAt(int teacherId, ArrayList<TeacherLocation> list) {
        HashMap<Integer, TeacherLocation> data = new HashMap<>();
        for (TeacherLocation location : list) {
            data.put(location.key, location);
        }
        putLocationHashAt(teacherId, data);
    }

    public void removeLocationHash() {
        putLocationHash(new HashMap<Integer, HashMap<Integer, TeacherLocation>>());
    }

    public void removeLocationHashAt(int teacherId) {
        if (getLocationHashAt(teacherId) == null) return;
        HashMap<Integer, HashMap<Integer, TeacherLocation>> newData = getLocationHash();
        newData.remove(teacherId);
        putLocationHash(newData);
    }

    public void removeLocationDataAt(int teacherId, int key) {
        HashMap<Integer, TeacherLocation> newData = getLocationHashAt(teacherId);
        if (newData == null) return;
        newData.remove(key);
        putLocationHashAt(teacherId, newData);
    }

    public void updateToNullDetail() {
        Log.d(TAG, "updateToNullDetail() called");
        removeDetailHash();
    }

    public void updateToPlaceholderDetail() {
        Log.d(TAG, "updateToPlaceholderDetail() called");
        updateToNullDetail();
        HashMap<Integer, TeacherDetail> data = new HashMap<>();
        data.put(1, new TeacherDetail(1, "สุมิตร", "สวนสุข"));
        data.put(2, new TeacherDetail(2, "ปิยมาศ", "ศรีสมพันธ์"));
        data.put(3, new TeacherDetail(3, "ธนดล", "ยิ้มถนอม"));
        data.put(4, new TeacherDetail(4, "A", "English Teacher"));
        data.put(5, new TeacherDetail(5, "B", "English Teacher"));
        data.put(6, new TeacherDetail(6, "บุญทรัพย์", "ลิมปนะชัย"));
        data.put(7, new TeacherDetail(7, "อธิวัฒน์", "ไพโรจน์"));
        data.put(8, new TeacherDetail(8, "ทรงชัย", "อารีวัฒนากุล"));
        data.put(9, new TeacherDetail(9, "ดาวเรือง", "บุปผา"));
        data.put(10, new TeacherDetail(10, "หัสวนัส", "เพ็งสันเทียะ"));
        data.put(11, new TeacherDetail(11, "กิตติธัช", "ทรงศิริ"));
        data.put(12, new TeacherDetail(12, "ศิริ", "สวัสดิ์แดง"));
        data.put(13, new TeacherDetail(13, "นิพนธ์", "ด้วงคำจันทร์"));
        data.put(14, new TeacherDetail(14, "อติพล", "สุกฤษฎานนท์"));
        data.put(15, new TeacherDetail(15, "จิตรา", "สุทธิกลม"));
        data.put(16, new TeacherDetail(16, "ปิยาภรณ์", "หอมจันทร์"));
        data.put(17, new TeacherDetail(17, "ยลกร", "จีนทองคำ"));
        data.put(18, new TeacherDetail(18, "ธนกร", "อรรจนาวัฒน์"));
        data.put(19, new TeacherDetail(19, "บัญชาพร", "อรุณเลิศอารีย์"));
        data.put(20, new TeacherDetail(20, "อัญชานา", "นิ่มอนุสสรณ์กุล"));
        detailDatabase.setValue(data);
    }

    HashMap<Integer, TeacherDetail> getDetailHash() {
        return detailDatabase.getValue();
    }

    public ArrayList<TeacherDetail> getDetailList() {
        HashMap<Integer, TeacherDetail> data = getDetailHash();
        ArrayList<TeacherDetail> list = new ArrayList<>(data.values());
        Collections.sort(list, new Comparator<TeacherDetail>() {
            @Override
            public int compare(TeacherDetail detail1, TeacherDetail detail2) {
                int value = detail1.name.compareTo(detail2.name);
                return (value != 0) ? value : detail1.surname.compareTo(detail2.surname);
            }
        });
        return list;
    }

    public TeacherDetail getDetailAt(int teacherId) {
        return getDetailHash().get(teacherId);
    }

    void putDetailHash(HashMap<Integer, TeacherDetail> data) {
        detailDatabase.setValue(data);
    }

    public void putDetailAt(int teacherId, TeacherDetail data) {
        HashMap<Integer, TeacherDetail> parentData = getDetailHash();
        parentData.put(teacherId, data);
        putDetailHash(parentData);
    }

    private void removeDetailHash() {
        putDetailHash(new HashMap<Integer, TeacherDetail>());
    }

    public void removeDetailAt(int teacherId) {
        if (getDetailAt(teacherId) == null) return;
        HashMap<Integer, TeacherDetail> newData = getDetailHash();
        newData.remove(teacherId);
        putDetailHash(newData);
    }

    public void setLocationObserver(LifecycleOwner owner, Observer<? super HashMap<Integer, HashMap<Integer, TeacherLocation>>> observer) {
        locationDatabase.observe(owner, observer);
    }

    public void setDetailObserver(LifecycleOwner owner, Observer<? super HashMap<Integer, TeacherDetail>> observer) {
        detailDatabase.observe(owner, observer);
    }
    /*
    public static class format {
        public HashMap<Integer, HashMap<Integer, TeacherLocation>> teacherLocation;
        public HashMap<Integer, TeacherDetail> teacherDetail;

        public format(HashMap<Integer, HashMap<Integer, TeacherLocation>> teacherLocation, HashMap<Integer, TeacherDetail> teacherDetail) {
            this.teacherLocation = teacherLocation;
            this.teacherDetail = teacherDetail;
        }
    }
    */

}
