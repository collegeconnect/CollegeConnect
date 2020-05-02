package com.example.collegeconnect.datamodels;

public class Events {

    private String eventName;
    private String eventDescription;
    private String imageUrl;
    private String registrationUrl;

    public Events() {
    }

    public Events(String eventName, String eventDescription, String imageUrl, String registrationUrl) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.imageUrl = imageUrl;
        this.registrationUrl = registrationUrl;
    }

    public Events(String eventName, String eventDescription, String registrationUrl) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.registrationUrl = registrationUrl;
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
}
