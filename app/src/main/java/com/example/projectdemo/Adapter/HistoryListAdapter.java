package com.example.projectdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdemo.DonationListing;
import com.example.projectdemo.DonationListingActivity;
import com.example.projectdemo.FoodItem;
import com.example.projectdemo.R;
import com.example.projectdemo.recipes.RecipeActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> weeksList;
    private ArrayList<FoodItem> foodList;

    public HistoryListAdapter(Context mContext, ArrayList<String> weeks, ArrayList<FoodItem> foodList){
        this.mContext = mContext;
        this.weeksList = weeks;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.week_item, parent, false);
        return new HistoryListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int week = position;
        ArrayList<FoodItem> expiredThisWeek = new ArrayList<>();

        for (FoodItem item : foodList) {
            if (item.calculateWeeksAgo() == week) {
                expiredThisWeek.add(item);
            }
        }

        if (expiredThisWeek.size() == 0) {
            holder.itemView.setVisibility(View.GONE);
            return;
        }

        TextView expiryWeekTextView = new TextView(mContext);
        expiryWeekTextView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        expiryWeekTextView.setTextSize(20);
        expiryWeekTextView.setPadding(20, 20, 20, 20);
        expiryWeekTextView.setTextColor(mContext.getResources().getColor(R.color.black));

        if (position == 0) {
            expiryWeekTextView.setText("Expiring this week");
        } else {
            expiryWeekTextView.setText("Expired " + position + " week ago");
        }

// Add expiryWeekTextView to parentView
        holder.expiryItemView.addView(expiryWeekTextView);

// Create TableLayout
        TableLayout expiryTableLayout = new TableLayout(mContext);
        expiryTableLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        expiryTableLayout.setPadding(20, 20, 20, 20);
        expiryTableLayout.setClipToPadding(false);
        holder.expiryItemView.addView(expiryTableLayout);

// Create header row
        TableRow headerRow = new TableRow(mContext);
        headerRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

// Create TextView for "Item Name"
        TextView itemNameTextView = new TextView(mContext);
        itemNameTextView.setText("Item Name");
        itemNameTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        itemNameTextView.setPadding(0, 8, 0, 8);
        itemNameTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f));

// Create TextView for "Date"
        TextView dateTextView = new TextView(mContext);
        dateTextView.setText("Date");
        dateTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        dateTextView.setPadding(0, 8, 0, 8);
        dateTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f));

// Create TextView for "Quantity"
        TextView quantityTextView = new TextView(mContext);
        quantityTextView.setText("Quantity");
        quantityTextView.setTextColor(mContext.getResources().getColor(R.color.black));
        quantityTextView.setPadding(0, 8, 0, 8);
        quantityTextView.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1f));

// Add TextViews to header row
        headerRow.addView(itemNameTextView);
        headerRow.addView(dateTextView);
        headerRow.addView(quantityTextView); // Add the new TextView for Quantity

// Add header row to the TableLayout
        expiryTableLayout.addView(headerRow);

        for (FoodItem item : expiredThisWeek) {
            // Create a new TableRow
            TableRow tableRow = new TableRow(mContext);

            // Create TextView for Item Name
            TextView itemName = new TextView(mContext);
            itemName.setText(item.getFoodName());
            itemName.setPadding(8, 8, 8, 8);
            itemName.setLayoutParams(new TableRow.LayoutParams(
                    0, // Width will be calculated automatically based on layout weight
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f)); // Weight for stretching

            // Create TextView for Date
            TextView date = new TextView(mContext);
            date.setText(item.getFoodExpiryDate());
            date.setPadding(8, 8, 8, 8);
            date.setLayoutParams(new TableRow.LayoutParams(
                    0, // Width will be calculated automatically based on layout weight
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f)); // Weight for stretching

            // Create TextView for Quantity
            TextView quantity = new TextView(mContext);
            quantity.setText(String.valueOf(item.getFoodQuantity())); // Assuming getFoodQuantity() returns an integer
            quantity.setPadding(8, 8, 8, 8);
            quantity.setLayoutParams(new TableRow.LayoutParams(
                    0, // Width will be calculated automatically based on layout weight
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1f)); // Weight for stretching

            // Add TextViews to the TableRow
            tableRow.addView(itemName);
            tableRow.addView(date);
            tableRow.addView(quantity);

            // Add the TableRow to the expiryTable
            expiryTableLayout.addView(tableRow);
        }




    }

    @Override
    public int getItemCount() {
        return weeksList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

    //    TextView expiryWeek;
    //    TableLayout expiryTable;
        LinearLayoutCompat expiryItemView;

        public ViewHolder( View itemView) {
            super(itemView);
         //   expiryWeek = itemView.findViewById(R.id.expiryWeek);
         //   expiryTable = itemView.findViewById(R.id.expiryTable);
            expiryItemView = itemView.findViewById(R.id.weekItemView);
        }

    }
}