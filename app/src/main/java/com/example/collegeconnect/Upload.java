package com.example.collegeconnect;

public class Upload {

    public String name;
    public String course;
    public String semester;
    public String branch;
    public String unit;
    public String url;
    public String author;
    public int download;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String course, String semester, String branch, String unit,String author, int download, String url) {
        this.name = name;
        this.course = course;
        this.semester = semester;
        this.branch = branch;
        this.unit = unit;
        this.author = author;
        this.download = download;
        this.url = url;
    }

    public String getCourse() {
        return course;
    }

    public String getSemester() {
        return semester;
    }

    public String getBranch() {
        return branch;
    }

    public String getUnit() {
        return unit;
    }

    public String getAuthor() {
        return author;
    }

    public int getDownload() {
        return download;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
