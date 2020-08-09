package com.college.collegeconnect.datamodels;

public class Resume {

    public String name;
    public String aboutMe;
    public String personalWebsite;
    public String resumeLink;
    public String email;
    public String linkedIn;
    public String github;
    public String behance;
    public String medium;

    public String getName() {
        return name;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getPersonalWebsite() {
        return personalWebsite;
    }

    public String getResumeLink() {
        return resumeLink;
    }

    public String getEmail() {
        return email;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public String getGithub() {
        return github;
    }

    public String getBehance() {
        return behance;
    }

    public String getMedium() {
        return medium;
    }

    public Resume() {

    }

    public Resume(String name, String aboutMe, String personalWebsite, String resumeLink, String email, String linkedIn, String github, String behance, String medium) {
        this.name = name;
        this.aboutMe = aboutMe;
        this.personalWebsite = personalWebsite;
        this.resumeLink = resumeLink;
        this.email = email;
        this.linkedIn = linkedIn;
        this.github = github;
        this.behance = behance;
        this.medium = medium;
    }
}
