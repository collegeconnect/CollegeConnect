package com.example.collegeconnect;

public class Upload {

    public String name;
    public String course;
    public String semester;
    public String branch;
    public String unit;
    public String url;
    public String author;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String course, String semester, String branch, String unit,String author, String url) {
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.branch = branch;
        this.unit = unit;
        this.author = author;
        this.url = url;
    }


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
