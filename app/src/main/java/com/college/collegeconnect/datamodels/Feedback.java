package com.college.collegeconnect.datamodels;

public class Feedback {
    public String overall;
    public String problem;
    public int solve;
    public String expectedFeature;
    public int satisfied;
    public String confused;
    public String addFeature;
    public String email;

    public Feedback(String email, String overall, String problem, int solve, String expectedFeature, int satisfied, String confused, String addFeature) {
        this.overall = overall;
        this.problem = problem;
        this.solve = solve;
        this.expectedFeature = expectedFeature;
        this.satisfied = satisfied;
        this.confused = confused;
        this.addFeature = addFeature;
        this.email = email;
    }

    public Feedback() {

    }
}
