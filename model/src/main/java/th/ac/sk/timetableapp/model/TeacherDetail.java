package th.ac.sk.timetableapp.model;

public class TeacherDetail {
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
}
