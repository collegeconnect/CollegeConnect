package com.example.collegeconnect;

public class Notes {
    String title;
    String author;
    String downloads;

    public Notes(String title, String author, String downloads) {
        this.title = title;
        this.author = author;
        this.downloads = downloads;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDownloads() {
        return downloads;
    }
}
