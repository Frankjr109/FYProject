package com.example.projectdemo;
//Class to hold the recipe data
public class Recipe {
    private String title;
    private int imageUrl;
    private String sourceUrl;

    public Recipe(String title) {
        this.title = title;
        //this.imageUrl = imageUrl;
        //this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
