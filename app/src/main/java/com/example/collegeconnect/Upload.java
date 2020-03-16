package com.example.collegeconnect;

public class Upload {

    public String name;
    public String semester;
    public String branch;
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String name, String semester, String branch, String url) {
        this.name = name;
        this.semester = semester;
        this.branch = branch;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
