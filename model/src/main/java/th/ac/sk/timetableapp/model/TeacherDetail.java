package th.ac.sk.timetableapp.model;

import com.google.gson.JsonObject;

public class TeacherDetail implements Comparable<TeacherDetail> {
    public final int id;
    public final String name;
    public final String surname;

    public TeacherDetail(int id, String name, String surname) {
        this.id=id;
        this.name=name;
        this.surname=surname;
    }
    public boolean detailIsNull() {
        return name == null || surname == null;
    }

    @Override
    public int compareTo(TeacherDetail detail) {
        int v;
        v = this.name.compareTo(detail.name);
        if (v == 0) v = this.surname.compareTo(detail.surname);
        return v;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("i", id);
        obj.addProperty("n", name);
        obj.addProperty("s", surname);
        return obj;
    }

    public static TeacherDetail fromJson(JsonObject o) {
        return new TeacherDetail(
                o.get("i").getAsInt(),
                o.get("n").getAsString(),
                o.get("s").getAsString());
    }
}
