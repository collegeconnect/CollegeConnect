package com.example.collegeconnect.datamodels;

public class Events {

    private String eventName;
    private String eventDescription;
    private String imageUrl;
    private String registrationUrl;
    private String date;
    private String organizer;

    public Events() {
    }

    public Events(String eventName, String eventDescription, String imageUrl, String registrationUrl, String date, String organizer) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.imageUrl = imageUrl;
        this.registrationUrl = registrationUrl;
        this.date = date;
        this.organizer = organizer;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getImageUrl() {
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
}
