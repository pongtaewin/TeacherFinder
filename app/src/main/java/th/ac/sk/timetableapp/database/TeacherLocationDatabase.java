package th.ac.sk.timetableapp.database;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import th.ac.sk.timetableapp.model.TeacherDetail;
import th.ac.sk.timetableapp.model.TeacherLocation;
import th.ac.sk.timetableapp.parser.DataParser;

@SuppressLint("UseSparseArrays")
public class TeacherLocationDatabase {
    public static final String TAG = "TeacherLocationDatabase";
    private static TeacherLocationDatabase ourInstance;
    private MutableLiveData<HashMap<Integer, HashMap<Integer, TeacherLocation>>> locationDatabase;
    private MutableLiveData<HashMap<Integer, TeacherDetail>> detailDatabase;

    private TeacherLocationDatabase() {
    }

    public static boolean checkIfCorrectlyImported(DataParser.TeacherLocationDatabaseFormat data) {
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
        return getInstance().getDetail().size();
    }

    public static int getNewTeacherID() {
        HashMap<Integer, TeacherDetail> detailMap = getInstance().getDetail();
        int size = detailMap.size();
        if (size == 0) return 1;
        int max = 1;
        for (HashMap.Entry<Integer, TeacherDetail> detailEntry : detailMap.entrySet()) {
            max = Math.max(max, detailEntry.getValue().id);
        }
        return max + 1;
    }

    public void updateToNullLocation() {
        Log.d(TAG, "updateToNullLocation() called");
        removeLocation();
    }

    public void updateToPlaceholderLocation() {
        Log.d(TAG, "updateToPlaceholderLocation() called");
        updateToNullLocation();
        for (int i = 1; i <= 20; i++) updateToPlaceholderLocation(i);
    }

    private void updateToPlaceholderLocation(int i) {
        Log.d(TAG, "updateToPlaceholderLocation() called with: i = [" + i + "]");
        switch (i) {
            case 1:
                putLocation(1, 0, new TeacherLocation(1, "ม.502", "4207", 0));
                putLocation(1, 1, new TeacherLocation(1, "ม.502", "4207", 1));
                putLocation(1, 5, new TeacherLocation(1, "ม.501", "4207", 5));
                putLocation(1, 6, new TeacherLocation(1, "ม.501", "4207", 6));
                putLocation(1, 22, new TeacherLocation(1, "ม.501", "4207", 22));
                putLocation(1, 23, new TeacherLocation(1, "ม.501", "4207", 23));
                putLocation(1, 27, new TeacherLocation(1, "ม.503", "4207", 27));
                putLocation(1, 28, new TeacherLocation(1, "ม.503", "4207", 28));
                putLocation(1, 30, new TeacherLocation(1, "ม.502", "4403", 30));
                putLocation(1, 31, new TeacherLocation(1, "ม.502", "4403", 31));
                putLocation(1, 40, new TeacherLocation(1, "ม.503", "4404", 40));
                putLocation(1, 41, new TeacherLocation(1, "ม.503", "4404", 41));
                break;
            case 2:
                putLocation(2, 2, new TeacherLocation(2, "ม.502", "4205", 2));
                putLocation(2, 3, new TeacherLocation(2, "ม.502", "4205", 3));
                putLocation(2, 8, new TeacherLocation(2, "ม.503", "4404", 8));
                putLocation(2, 9, new TeacherLocation(2, "ม.503", "4404", 9));
                putLocation(2, 11, new TeacherLocation(2, "ม.502", "4403", 11));
                putLocation(2, 12, new TeacherLocation(2, "ม.502", "4403", 12));
                putLocation(2, 15, new TeacherLocation(2, "ม.501", "4205", 15));
                putLocation(2, 16, new TeacherLocation(2, "ม.501", "4205", 16));
                putLocation(2, 20, new TeacherLocation(2, "ม.503", "4205", 20));
                putLocation(2, 21, new TeacherLocation(2, "ม.503", "4205", 21));
                putLocation(2, 27, new TeacherLocation(2, "ม.501", "4402", 27));
                putLocation(2, 28, new TeacherLocation(2, "ม.501", "4402", 28));
                putLocation(2, 30, new TeacherLocation(2, "ม.501", "4402", 30));
                putLocation(2, 31, new TeacherLocation(2, "ม.501", "4402", 31));
                putLocation(2, 42, new TeacherLocation(2, "ม.503", "4205", 42));
                putLocation(2, 43, new TeacherLocation(2, "ม.503", "4205", 43));
                putLocation(2, 45, new TeacherLocation(2, "ม.502", "4403", 45));
                putLocation(2, 46, new TeacherLocation(2, "ม.502", "4403", 46));
                break;
            case 3:
                putLocation(3, 5, new TeacherLocation(3, "ม.502", "4403", 5));
                putLocation(3, 6, new TeacherLocation(3, "ม.502", "4403", 6));
                putLocation(3, 12, new TeacherLocation(3, "ม.501", "4402", 12));
                putLocation(3, 13, new TeacherLocation(3, "ม.501", "4402", 13));
                putLocation(3, 16, new TeacherLocation(3, "ม.502", "4403", 16));
                putLocation(3, 25, new TeacherLocation(3, "ม.501", "4402", 25));
                putLocation(3, 28, new TeacherLocation(3, "ม.502", "4403", 28));
                putLocation(3, 32, new TeacherLocation(3, "ม.501", "4402", 32));
                putLocation(3, 40, new TeacherLocation(3, "ม.502", "4403", 40));
                putLocation(3, 41, new TeacherLocation(3, "ม.501", "4402", 41));
                break;
            case 4:
                putLocation(4, 5, new TeacherLocation(4, "ม.503", "4404", 5));
                putLocation(4, 7, new TeacherLocation(4, "ม.502", "4403", 7));
                putLocation(4, 8, new TeacherLocation(4, "ม.501", "4402", 8));
                putLocation(4, 16, new TeacherLocation(4, "ม.503", "4404", 16));
                putLocation(4, 22, new TeacherLocation(4, "ม.503", "4404", 22));
                putLocation(4, 25, new TeacherLocation(4, "ม.502", "4403", 25));
                putLocation(4, 26, new TeacherLocation(4, "ม.501", "4402", 26));
                putLocation(4, 40, new TeacherLocation(4, "ม.501", "4402", 40));
                putLocation(4, 41, new TeacherLocation(4, "ม.502", "4403", 41));
                break;
            case 5:
                putLocation(5, 5, new TeacherLocation(5, "ม.503", "5204", 5));
                putLocation(5, 7, new TeacherLocation(5, "ม.502", "5204", 7));
                putLocation(5, 8, new TeacherLocation(5, "ม.501", "5204", 8));
                putLocation(5, 16, new TeacherLocation(5, "ม.503", "5204", 16));
                putLocation(5, 22, new TeacherLocation(5, "ม.503", "5204", 22));
                putLocation(5, 25, new TeacherLocation(5, "ม.502", "5204", 25));
                putLocation(5, 26, new TeacherLocation(5, "ม.501", "5204", 26));
                putLocation(5, 40, new TeacherLocation(5, "ม.501", "5204", 40));
                putLocation(5, 41, new TeacherLocation(5, "ม.502", "5204", 41));
                break;
            case 6:
                putLocation(6, 7, new TeacherLocation(6, "ม.501", "4402", 7));
                putLocation(6, 8, new TeacherLocation(6, "ม.502", "4403", 8));
                putLocation(6, 18, new TeacherLocation(6, "ม.503", "4404", 18));
                break;
            case 7:
                putLocation(7, 6, new TeacherLocation(7, "ม.503", "3301", 6));
                putLocation(7, 9, new TeacherLocation(7, "ม.502", "3301", 9));
                putLocation(7, 48, new TeacherLocation(7, "ม.501", "3301", 48));
                break;
            case 8:
                putLocation(8, 11, new TeacherLocation(8, "ม.501", "4402", 11));
                putLocation(8, 13, new TeacherLocation(8, "ม.502", "4403", 13));
                putLocation(8, 47, new TeacherLocation(8, "ม.501", "4402", 47));
                putLocation(8, 48, new TeacherLocation(8, "ม.502", "4403", 48));
                break;
            case 9:
                putLocation(9, 9, new TeacherLocation(9, "ม.501", "6403", 9));
                putLocation(9, 15, new TeacherLocation(9, "ม.502", "6403", 15));
                putLocation(9, 45, new TeacherLocation(9, "ม.503", "6403", 45));
                break;
            case 10:
                putLocation(10, 8, new TeacherLocation(10, "ม.503", "4404", 8));
                putLocation(10, 9, new TeacherLocation(10, "ม.503", "4404", 9));
                putLocation(10, 17, new TeacherLocation(10, "ม.502", "4309", 17));
                putLocation(10, 18, new TeacherLocation(10, "ม.502", "4309", 18));
                putLocation(10, 22, new TeacherLocation(10, "ม.502", "4403", 22));
                putLocation(10, 23, new TeacherLocation(10, "ม.502", "4403", 23));
                break;
            case 11:
                putLocation(11, 2, new TeacherLocation(11, "ม.501", "4402", 2));
                putLocation(11, 17, new TeacherLocation(11, "ม.503", "4404", 17));
                putLocation(11, 18, new TeacherLocation(11, "ม.501", "4402", 18));
                putLocation(11, 20, new TeacherLocation(11, "ม.502", "4403", 20));
                putLocation(11, 46, new TeacherLocation(11, "ม.503", "4404", 46));
                putLocation(11, 47, new TeacherLocation(11, "ม.502", "4403", 47));
                break;
            case 12:
                putLocation(12, 7, new TeacherLocation(12, "ม.503", "7005", 7));
                putLocation(12, 17, new TeacherLocation(12, "ม.501", "7005", 17));
                putLocation(12, 21, new TeacherLocation(12, "ม.502", "7005", 21));
                break;
            case 13:
                putLocation(13, 26, new TeacherLocation(13, "ม.502", "4403", 26));
                putLocation(13, 46, new TeacherLocation(13, "ม.501", "4402", 46));
                putLocation(13, 48, new TeacherLocation(13, "ม.503", "4404", 48));
                break;
            case 14:
                putLocation(14, 3, new TeacherLocation(14, "ม.501", "4402", 3));
                putLocation(14, 11, new TeacherLocation(14, "ม.503", "4404", 11));
                putLocation(14, 26, new TeacherLocation(14, "ม.503", "4404", 26));
                putLocation(14, 27, new TeacherLocation(14, "ม.502", "4403", 27));
                putLocation(14, 32, new TeacherLocation(14, "ม.502", "4403", 32));
                putLocation(14, 45, new TeacherLocation(14, "ม.501", "4402", 45));
                break;
            case 15:
                putLocation(15, 2, new TeacherLocation(15, "ม.503", "4404", 2));
                putLocation(15, 3, new TeacherLocation(15, "ม.503", "4404", 3));
                putLocation(15, 20, new TeacherLocation(15, "ม.501", "4402", 20));
                putLocation(15, 21, new TeacherLocation(15, "ม.501", "4402", 21));
                putLocation(15, 42, new TeacherLocation(15, "ม.502", "4403", 42));
                putLocation(15, 43, new TeacherLocation(15, "ม.502", "4403", 43));
                break;
            case 16:
                putLocation(16, 8, new TeacherLocation(16, "ม.503", "4404", 8));
                putLocation(16, 9, new TeacherLocation(16, "ม.503", "4404", 9));
                putLocation(16, 27, new TeacherLocation(16, "ม.501", "4402", 27));
                putLocation(16, 28, new TeacherLocation(16, "ม.501", "4402", 28));
                putLocation(16, 45, new TeacherLocation(16, "ม.502", "4403", 45));
                putLocation(16, 46, new TeacherLocation(16, "ม.502", "4403", 46));
                break;
            case 17:
                putLocation(17, 27, new TeacherLocation(17, "ม.501", "4402", 27));
                putLocation(17, 28, new TeacherLocation(17, "ม.501", "4402", 28));
                putLocation(17, 45, new TeacherLocation(17, "ม.502", "4403", 45));
                putLocation(17, 46, new TeacherLocation(17, "ม.502", "4403", 46));
                break;
            case 18:
                putLocation(18, 8, new TeacherLocation(18, "ม.503", "4404", 8));
                putLocation(18, 9, new TeacherLocation(18, "ม.503", "4404", 9));
                putLocation(18, 27, new TeacherLocation(18, "ม.501", "4402", 27));
                putLocation(18, 28, new TeacherLocation(18, "ม.501", "4402", 28));
                putLocation(18, 45, new TeacherLocation(18, "ม.502", "4403", 45));
                putLocation(18, 46, new TeacherLocation(18, "ม.502", "4403", 46));
                break;
            case 19:
                putLocation(19, 0, new TeacherLocation(19, "ม.501", "4309", 0));
                putLocation(19, 1, new TeacherLocation(19, "ม.501", "4309", 1));
                putLocation(19, 8, new TeacherLocation(19, "ม.503", "4404", 8));
                putLocation(19, 9, new TeacherLocation(19, "ม.503", "4404", 9));
                putLocation(19, 12, new TeacherLocation(19, "ม.503", "4309", 12));
                putLocation(19, 13, new TeacherLocation(19, "ม.503", "4309", 13));
                putLocation(19, 27, new TeacherLocation(19, "ม.501", "4402", 27));
                putLocation(19, 28, new TeacherLocation(19, "ม.501", "4402", 28));
                putLocation(19, 31, new TeacherLocation(19, "ม.503", "4404", 31));
                putLocation(19, 32, new TeacherLocation(19, "ม.503", "4404", 32));
                putLocation(19, 42, new TeacherLocation(19, "ม.501", "4402", 42));
                putLocation(19, 43, new TeacherLocation(19, "ม.501", "4402", 43));
                putLocation(19, 45, new TeacherLocation(19, "ม.502", "4403", 45));
                putLocation(19, 46, new TeacherLocation(19, "ม.502", "4403", 46));
                break;
            case 20:
                putLocation(20, 8, new TeacherLocation(20, "ม.503", "4404", 8));
                putLocation(20, 9, new TeacherLocation(20, "ม.503", "4404", 9));
                putLocation(20, 27, new TeacherLocation(20, "ม.501", "4402", 27));
                putLocation(20, 28, new TeacherLocation(20, "ม.501", "4402", 28));
                putLocation(20, 45, new TeacherLocation(20, "ม.502", "4403", 45));
                putLocation(20, 46, new TeacherLocation(20, "ม.502", "4403", 46));
                break;
        }
    }

    public TeacherLocation getLocation(int teacherId, int key) {
        try {
            return Objects.requireNonNull(getLocation(teacherId)).get(key);
        } catch (NullPointerException e) {
            Log.w("TeacherLocationDatabase", "NPE Caught at getLocation(teacherId)");
            Log.w("TeacherLocationDatabase", "Returning null.");
            return null;
        }
    }

    public HashMap<Integer, TeacherLocation> getLocation(int teacherId) {
        try {
            return Objects.requireNonNull(getLocation()).get(teacherId);
        } catch (NullPointerException e) {
            Log.w("TeacherLocationDatabase", "NPE Caught at getLocation()");
            Log.w("TeacherLocationDatabase", "Returning null.");
            return null;
        }
    }

    public HashMap<Integer, HashMap<Integer, TeacherLocation>> getLocation() {
        return locationDatabase.getValue();
    }

    public void putLocation(HashMap<Integer, HashMap<Integer, TeacherLocation>> data) {
        locationDatabase.setValue(data);
    }

    public void putLocation(int teacherId, HashMap<Integer, TeacherLocation> data) {
        HashMap<Integer, HashMap<Integer, TeacherLocation>> parentData = getLocation();
        parentData.put(teacherId, data);
        locationDatabase.setValue(parentData);
    }

    public void putLocation(int teacherId, int key, TeacherLocation data) {
        HashMap<Integer, TeacherLocation> parentData = getLocation(teacherId);
        if (parentData == null) parentData = new HashMap<>();
        parentData.put(key, data);
        putLocation(teacherId, parentData);
    }

    public void removeLocation() {
        putLocation(new HashMap<Integer, HashMap<Integer, TeacherLocation>>());
    }

    public boolean removeLocation(int teacherId) {
        if (getLocation(teacherId) == null) return false;
        HashMap<Integer, HashMap<Integer, TeacherLocation>> newData = getLocation();
        newData.remove(teacherId);
        putLocation(newData);
        return true;
    }

    public boolean removeLocation(int teacherId, int key) {
        if (getLocation(teacherId, key) == null) return false;
        HashMap<Integer, HashMap<Integer, TeacherLocation>> newData = getLocation();
        newData.get(teacherId).remove(key);
        putLocation(newData);
        return true;
    }

    public void updateToNullDetail() {
        Log.d(TAG, "updateToNullDetail() called");
        removeDetail();
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

    public HashMap<Integer, TeacherDetail> getDetail() {
        return detailDatabase.getValue();
    }

    public TeacherDetail getDetailAt(int pos) {
        Set<Integer> positionSet = TeacherLocationDatabase.getInstance().getDetail().keySet();
        Iterator<Integer> it = positionSet.iterator();
        int key = 1;
        for (int i = 0; i < pos+1; i++) if (it.hasNext()) key = it.next();
        return TeacherLocationDatabase.getInstance().getDetail(key);
    }

    public TeacherDetail getDetail(int teacherId) {
        return getDetail().get(teacherId);
    }

    public void putDetail(HashMap<Integer, TeacherDetail> data) {
        detailDatabase.setValue(data);
    }

    public void putDetail(int teacherId, TeacherDetail data) {
        HashMap<Integer, TeacherDetail> parentData = getDetail();
        parentData.put(teacherId, data);
        putDetail(parentData);
    }

    public boolean removeDetail() {
        putDetail(new HashMap<Integer, TeacherDetail>());
        return true;
    }

    public boolean removeDetail(int teacherId) {
        if (getDetail(teacherId) == null) return false;
        HashMap<Integer, TeacherDetail> newData = getDetail();
        newData.remove(teacherId);
        putDetail(newData);
        return true;
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
