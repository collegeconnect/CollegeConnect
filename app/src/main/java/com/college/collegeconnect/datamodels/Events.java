package com.college.collegeconnect.datamodels;

import java.io.Serializable;
import java.util.ArrayList;

public class Events implements Serializable {

    private String eventName;
    private String eventDescription;
    private ArrayList<String> imageUrl;
    private String registrationUrl;
    private String date;
    private String organizer;
    private String endDate;

    public Events() {
    }

    public Events(String eventName, String eventDescription, ArrayList<String> imageUrl, String registrationUrl, String date, String organizer, String endDate) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.imageUrl = imageUrl;
        this.registrationUrl = registrationUrl;
        this.date = date;
        this.organizer = organizer;
        this.endDate = endDate;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public void setImageUrl(ArrayList<String> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public ArrayList<String> getImageUrl() {
        return imageUrl;
    }

    public String getRegistrationUrl() {
        return registrationUrl;
    }

    public String getDate() {
        return date;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getEndDate() {
        return endDate;
    }

}
