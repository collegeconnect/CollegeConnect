package com.bvcoe.bvpconnect;

public class Subject {

    private String subjectName;
    private int attended, missed;

    public Subject(String subjectName, int attended, int missed) {
        this.subjectName = subjectName;
        this.attended = attended;
        this.missed = missed;
    }

    public int getAttended() {
        return attended;
    }

    public int getMissed() {
        return missed;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
