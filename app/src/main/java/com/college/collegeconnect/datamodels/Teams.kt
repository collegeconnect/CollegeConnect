package com.college.collegeconnect.datamodels;

import java.io.Serializable;

public class Teams implements Serializable {

    public String code;
    public String teamname;
    public String teammate1;
    public String teammate2;
    public String teammate3;
    public String teammate4;

    public Teams() {
    }

    public Teams(String code, String teamname, String teamname1, String teamname2, String teamname3, String teamname4) {
        this.code = code;
        this.teamname = teamname;
        this.teammate1 = teamname1;
        this.teammate2 = teamname2;
        this.teammate3 = teamname3;
        this.teammate4 = teamname4;
    }

    public String getCode() {
        return code;
    }

    public String getTeamname() {
        return teamname;
    }

    public String getTeammate1() {
        return teammate1;
    }

    public String getTeammate2() {
        return teammate2;
    }

    public String getTeammate3() {
        return teammate3;
    }

    public String getTeammate4() {
        return teammate4;
    }
}
