package com.example.projectdemo;

import java.util.ArrayList;

//Class to hold the recipe data
public class Recipe {
    private String title;
    private String imageUrl;
    private String id;
    private long likeCount;
    private long dislikeCount;
    private String[] likeArray;
    private String[] dislikeArray;
    private ArrayList<Ingredient> ingredients;


    public Recipe() {}

    public Recipe(String title, String imageUrl, String id) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.id = id;
        this.ingredients = new ArrayList<>();
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(long dislikeCount) {
        this.dislikeCount = dislikeCount;
    }

    public String[] getLikeArray() {
        return likeArray;
    }

    public void setLikeArray(String[] likeArray) {
        this.likeArray = likeArray;
    }

    public String[] getDislikeArray() {
        return dislikeArray;
    }

    public void setDislikeArray(String[] dislikeArray) {
        this.dislikeArray = dislikeArray;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

