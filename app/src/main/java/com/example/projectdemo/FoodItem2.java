package com.example.projectdemo;

public class FoodItem2 {

    private String id;
    private String foodName2;
    private String knownAs2;
    private String brand2;
    private String category2;
    private String expiryDate2;
    private String remainingDays2;

    public FoodItem2(){

    }

    public FoodItem2(String id, String foodName2, String knownAs2, String brand2, String category2, String expiryDate2){
        this.id = id;
        this.foodName2 = foodName2;
        this.knownAs2 = knownAs2;
        this.brand2 = brand2;
        this.category2 = category2;
        this.expiryDate2 = expiryDate2;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getFoodName2() {
        return foodName2;
    }

    public void setFoodName2(String foodName2) {
        this.foodName2 = foodName2;
    }

    public String getKnownAs2() {
        return knownAs2;
    }

    public void setKnownAs2(String knownAs2) {
        this.knownAs2 = knownAs2;
    }

    public String getBrand2() {
        return brand2;
    }

    public void setBrand2(String brand2) {
        this.brand2 = brand2;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getExpiryDate2() {
        return expiryDate2;
    }

    public void setExpiryDate2(String expiryDate2) {
        this.expiryDate2 = expiryDate2;
    }

    public String getRemainingDays2(){
        return remainingDays2;
    }

    public void setRemainingDays2(String remainingDays2){
        this.remainingDays2 = remainingDays2;
    }

}
