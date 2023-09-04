package com.example.projectdemo;

import android.net.ParseException;
import android.os.Build;
import android.widget.ProgressBar;

import com.google.firebase.firestore.CollectionReference;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

public class FoodItem {
    private String foodName;
    private String foodCategory;
    private String foodQuantity;
    private String foodExpiryDate;

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


    public boolean isExpired() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate currentDate = LocalDate.now();

            String[] dateComponents = foodExpiryDate.split("/");
            int day = Integer.parseInt(dateComponents[0]);
            int month = Integer.parseInt(dateComponents[1]);
            int year = Integer.parseInt(dateComponents[2]);

            LocalDate expiryDate = LocalDate.of(year, month, day);

            return currentDate.isAfter(expiryDate);
        }

        return false;
    }

    public String getMonthLabel() {
        String date = this.foodExpiryDate; // "dd/MM/yyyy" format

        String[] dateParts = date.split("/");
        if (dateParts.length == 3) {
            int month = Integer.parseInt(dateParts[1]);

            if (month >= 1 && month <= 12) {
                String[] monthLabels = {"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"};
                return monthLabels[month - 1];
            }
        }

        return "";
    }

    public int calculateWeeksAgo() {
        // Get the current date
        Calendar currentDate = Calendar.getInstance();

        // Parse the food expiry date string
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Calendar expiryDate = Calendar.getInstance();
        try {
            expiryDate.setTime(format.parse(foodExpiryDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return -1; // Return -1 if the date format is invalid
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return -1;
        }

        // Calculate the week in the year for the expiry date
        int expiryWeek = expiryDate.get(Calendar.WEEK_OF_YEAR);

        // Calculate the current week in the year
        int currentWeek = currentDate.get(Calendar.WEEK_OF_YEAR);

        // Calculate the number of weeks ago the expiry date occurred
        int weeksAgo = currentWeek - expiryWeek;

        return weeksAgo;
    }
}


