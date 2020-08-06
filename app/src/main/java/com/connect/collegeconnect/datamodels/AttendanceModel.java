package com.connect.collegeconnect.datamodels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Attendance_Table")
public class AttendanceModel {

    @PrimaryKey
    int ID;

    String NAME;

    int ATTENDED;

    String MISSED;


    public AttendanceModel(int ID, String NAME, int ATTENDED, String MISSED) {
        this.ID = ID;
        this.NAME = NAME;
        this.ATTENDED = ATTENDED;
        this.MISSED = MISSED;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getATTENDED() {
        return ATTENDED;
    }

    public void setATTENDED(int ATTENDED) {
        this.ATTENDED = ATTENDED;
    }

    public String getMISSED() {
        return MISSED;
    }

    public void setMISSED(String MISSED) {
        this.MISSED = MISSED;
    }
}
