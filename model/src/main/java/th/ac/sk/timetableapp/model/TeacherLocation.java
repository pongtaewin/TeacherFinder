package th.ac.sk.timetableapp.model;

import androidx.annotation.NonNull;

public class TeacherLocation implements Comparable<TeacherLocation> {
    public int teacherId;
    public String classroom = null;
    public String location = null;
    public int key;

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
}
