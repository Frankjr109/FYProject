package com.example.projectdemo;

import android.widget.ProgressBar;

import com.google.firebase.firestore.CollectionReference;

public class FoodItem {
    private String foodName;
    private String foodCategory;
    private String foodQuantity;
    private String foodExpiryDate;

    private String daysUntilExpired;
    private ProgressBar progressBar;

    public FoodItem(){

    }

    public FoodItem(String foodName, String foodCategory, String foodQuantity, String foodExpiryDate){
        this.foodName = foodName;
        this.foodCategory = foodCategory;
        this.foodQuantity = foodQuantity;
        this.foodExpiryDate = foodExpiryDate;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodExpiryDate() {
        return foodExpiryDate;
    }

    public void setFoodExpiryDate(String foodExpiryDate) {
        this.foodExpiryDate = foodExpiryDate;
    }



}
