package th.ac.sk.timetableapp.model;

public class TeacherDetail implements Comparable<TeacherDetail> {
    public String name;
    public String surname;
    public int id;

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
}
