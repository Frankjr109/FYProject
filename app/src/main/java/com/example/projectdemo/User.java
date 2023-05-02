package com.example.projectdemo;

//Making slight changes to the User POJO
public class User {
    private String id;
    private String fullName;
    private String number;
    private String email;
    private String imageURL;
    private String status;
    private String search;

    public User(){

    }

    public User(String id, String fullName, String number, String email, String imageURL, String status, String search){
        this.id = id;
        this.fullName = fullName;
        this.number = number;
        this.email = email;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
