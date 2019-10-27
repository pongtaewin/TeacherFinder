package th.ac.sk.timetableapp.model;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

public class Period {
    public int type;
    public String subject = null;
    public String subjectCode = null;
    public String teacherList = null;
    public String room = null;
    public int periodNum;

    public Period(int type, int periodNum) {
        if (type == Type.HAVE_CLASS)
            throw new UnsupportedOperationException();
        this.type = type;
        setPeriodNum(periodNum);
    }

    @Deprecated
    public Period(@Nullable String subject, @Nullable String subjectCode, @Nullable String teacherList, int periodNum) {
        this.type = Type.HAVE_CLASS;
        setPeriodNum(periodNum);
        this.subject = subject;
        this.subjectCode = subjectCode;
        this.teacherList = teacherList;
    }

    public Period(@Nullable String subject, @Nullable String subjectCode, @Nullable String teacherList, @Nullable String room, int periodNum) {
        this.type = Type.HAVE_CLASS;
        setPeriodNum(periodNum);
        this.subject = subject;
        this.subjectCode = subjectCode;
        this.teacherList = teacherList;
        this.room = room;
    }

    private void setPeriodNum(int periodNum) {
        this.periodNum = periodNum;
    }

    public boolean classIsNull() {
        Log.w("classIsNull", "------- Period Info -------");
        Log.w("classIsNull", "type = " + type);
        Log.w("classIsNull", "subject = " + subject);
        Log.w("classIsNull", "subjectCode = " + subjectCode);
        Log.w("classIsNull", "teacherList = " + teacherList);
        Log.w("classIsNull", "room = " + room);
        Log.w("classIsNull", "periodNum = " + periodNum);

        return type == Type.HAVE_CLASS && (
                subject == null || subjectCode == null || teacherList == null || room == null
                        || periodNum <= 0 || periodNum > 10);
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("t", type);
        obj.addProperty("s", subject);
        obj.addProperty("c", subjectCode);
        obj.addProperty("l", teacherList);
        obj.addProperty("r", room);
        obj.addProperty("n", periodNum);
        return obj;
    }

    public static Period fromJson(JsonObject o) {
        int t = o.get("t").getAsInt();
        if (t == Type.HAVE_CLASS)
            return new Period(
                    o.get("s").getAsString(),
                    o.get("c").getAsString(),
                    o.get("l").getAsString(),
                    o.get("r").getAsString(),
                    o.get("n").getAsInt());
        return new Period(t, o.get("n").getAsInt());
    }

    public static class Type {
        public static final int HAVE_CLASS = 1024;
        public static final int NO_CLASS = 1;
        public static final int LUNCH_PERIOD = 2;
        public static final int SCHOOL_CONFERENCE = 4;
        public static final int MILITARY_WORK = 8;

        public static String getStringFromType(int type) {
            if (type == NO_CLASS) return "ไม่มีการเรียนการสอน";
            if (type == LUNCH_PERIOD) return "พักรับประทานอาหารกลางวัน";
            if (type == SCHOOL_CONFERENCE) return "คาบประชุม";
            if (type == MILITARY_WORK) return "กิจกรรมรักษาดินแดน";
            return null;
        }
    }
}
