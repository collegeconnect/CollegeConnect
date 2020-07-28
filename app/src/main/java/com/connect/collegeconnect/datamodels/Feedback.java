package com.connect.collegeconnect.datamodels;

public class Feedback {
    String overall;
    String problem;
    int solve;
    String expectedFeature;
    int satisfied;
    String confused;
    String addFeature;

    public Feedback(String overall, String problem, int solve, String expectedFeature, int satisfied, String confused, String addFeature) {
        this.overall = overall;
        this.problem = problem;
        this.solve = solve;
        this.expectedFeature = expectedFeature;
        this.satisfied = satisfied;
        this.confused = confused;
        this.addFeature = addFeature;
    }
    public Feedback(){

    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public void setSolve(int solve) {
        this.solve = solve;
    }

    public void setExpectedFeature(String expectedFeature) {
        this.expectedFeature = expectedFeature;
    }

    public void setSatisfied(int satisfied) {
        this.satisfied = satisfied;
    }

    public void setConfused(String confused) {
        this.confused = confused;
    }

    public void setAddFeature(String addFeature) {
        this.addFeature = addFeature;
    }
}
