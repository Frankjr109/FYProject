package com.example.projectdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<FoodItem> foodItemArrayList;

    public FoodRecyclerAdapter(Context context, ArrayList<FoodItem> foodItemArrayList) {
        this.context = context;
        this.foodItemArrayList = foodItemArrayList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textName;
        TextView textCategory;
        TextView textQuantity;
        TextView textExpiryDate;

        Button buttonDelete;
        Button buttonUpdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            textCategory = itemView.findViewById(R.id.textCategory);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textExpiryDate = itemView.findViewById(R.id.textExpiryDate);

            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            buttonUpdate = itemView.findViewById(R.id.buttonUpdate);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.food_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return foodItemArrayList.size();
    }
}
