package com.example.projectdemo;

public class DonationListing {

    private String title, description, location, user, userID, id;

    public DonationListing() {}

    public DonationListing(String title, String description, String location, String user, String userID) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.user = user;
        this.userID = userID;

    }

    public DonationListing(String title, String description, String location, String user, String userID, String id) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.user = user;
        this.userID = userID;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
