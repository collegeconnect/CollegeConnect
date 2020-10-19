package com.college.collegeconnect.datamodels;

public class User {

    private String rollNo;
    private String email;
    private String name;
    private String branch;
    private String college;

    public String getRollNo() {
        return rollNo;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getCollege() {
        return college;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String rollNo, String email, String name, String branch, String college) {
        this.rollNo = rollNo;
        this.email = email;
        this.name = name;
        this.branch = branch;
        this.college = college;
    }
}
