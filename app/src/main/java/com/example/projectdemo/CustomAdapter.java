package com.example.projectdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class CustomAdapter extends RecyclerView.Adapter<ViewHolder>{

    ListActivity listActivity;
    List<FoodItem2> foodItem2List;
    Context context;

    public CustomAdapter(ListActivity listActivity, List<FoodItem2> foodItem2List) {
        this.listActivity = listActivity;
        this.foodItem2List = foodItem2List;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);


        //handle item clicks here
        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //this will be called when user click item

                //show data in Toast on clicking
                String name = foodItem2List.get(position).getFoodName2();
                String knownAs = foodItem2List.get(position).getKnownAs2();
                String brand = foodItem2List.get(position).getBrand2();
                String category = foodItem2List.get(position).getCategory2();
                String expiryDate = foodItem2List.get(position).getExpiryDate2();
                Toast.makeText(listActivity, "Details for " + name + " are as follows ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //this will be called when user long click item

                //creating alertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(listActivity);
                //options to display in dialog
                String[] options = {"Update", "Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            //update is clicked
                            //get data
                            String id = foodItem2List.get(position).getId();
                            String name = foodItem2List.get(position).getFoodName2();
                            String knownAs = foodItem2List.get(position).getKnownAs2();
                            String brand = foodItem2List.get(position).getBrand2();
                            String category = foodItem2List.get(position).getCategory2();
                            String expiryDate = foodItem2List.get(position).getExpiryDate2();

                            //intent to start activity
                            Intent intent = new Intent(listActivity, ScanProductActivity.class);
                            //put data in intent
                            intent.putExtra("pId", id);
                            intent.putExtra("pName", name);
                            intent.putExtra("pKnownAs", knownAs);
                            intent.putExtra("pBrand", brand);
                            intent.putExtra("pCategory", category);
                            intent.putExtra("pExpiryDate", expiryDate);

                            //start activity
                            listActivity.startActivity(intent);

                        }
                        if(which == 1){
                            //delete is clicked

                        }
                    }
                }).create().show();
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind views / set data
        holder.mNameTV.setText(foodItem2List.get(position).getFoodName2());
        holder.mKnownTV.setText(foodItem2List.get(position).getKnownAs2());
        holder.mBrandTV.setText(foodItem2List.get(position).getBrand2());
        holder.mCategoryTV.setText(foodItem2List.get(position).getCategory2());
        holder.mExpiryDateTV.setText(foodItem2List.get(position).getExpiryDate2());

        /*FoodItem2 foodItem2 = foodItem2List.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date expiryDate = null;
        try{
            expiryDate = dateFormat.parse(foodItem2.getExpiryDate2());
        }catch (ParseException e){
            e.printStackTrace();
        }

        //Calculate the days until the food item expires
        Date currentDate = new Date();
        long diffInMillies = Math.abs(expiryDate.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        int daysUntilExpiry = (int) diffInDays;

        holder.mRemainingDaysTV.setText("Expires in " + daysUntilExpiry + " days");*/

        FoodItem2 foodItem2 = foodItem2List.get(position);

        // Parse the expiry date string into a Date object
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date expiryDate = null;
        try {
            expiryDate = dateFormat.parse(foodItem2.getExpiryDate2());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Calculate the days until the food item expires
        Date currentDate = new Date();
        long timeDiff = expiryDate.getTime() - currentDate.getTime();
        long diffInDays = timeDiff / (24 * 60 * 60 * 1000);
        int daysUntilExpiry = (int) diffInDays;

        // Set the text view to display the remaining days until expiry
        if(daysUntilExpiry == 0){
            holder.mRemainingDaysTV.setTextColor(Color.RED);
            holder.mRemainingDaysTV.setText("Expires in " + daysUntilExpiry + " days, Item has expired");
        }else if(daysUntilExpiry == 1){
            holder.mRemainingDaysTV.setTextColor(Color.WHITE);
            holder.mRemainingDaysTV.setText("Expires in " + daysUntilExpiry + " days, Food needs to be Utilised");
        }else{
            holder.mRemainingDaysTV.setText("Expires in " + daysUntilExpiry + " days");
        }
        //holder.mRemainingDaysTV.setText("Expires in " + daysUntilExpiry + " days");


    }


    @Override
    public int getItemCount() {
        return foodItem2List.size();
    }
}
