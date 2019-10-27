package th.ac.sk.timetableapp.model;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;

public class TeacherLocation implements Comparable<TeacherLocation> {
    private final int teacherId;
    public final String classroom;
    public final String location;
    public final int key;

    public TeacherLocation(int teacherId, @NonNull String classroom, @NonNull String location, int key) {
        this.teacherId = teacherId;
        this.classroom = classroom;
        this.location = location;
        this.key = key;
    }

    public TeacherLocation(int teacherId, int key) {
        this.teacherId = teacherId;
        this.classroom = null;
        this.location = null;
        this.key = key;
    }


    @Override
    public int compareTo(TeacherLocation o) {
        int v = Integer.compare(this.teacherId, o.teacherId);
        if (v == 0) return Integer.compare(this.key, o.key);
        return v;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("i", teacherId);
        obj.addProperty("c", classroom);
        obj.addProperty("l", location);
        obj.addProperty("k", key);
        return obj;
    }

    public static TeacherLocation fromJson(JsonObject o) {
        if (o.get("c").isJsonNull())
            return new TeacherLocation(o.get("i").getAsInt(), o.get("k").getAsInt());
        return new TeacherLocation(
                o.get("i").getAsInt(),
                o.get("c").getAsString(),
                o.get("l").getAsString(),
                o.get("k").getAsInt());
    }
}
