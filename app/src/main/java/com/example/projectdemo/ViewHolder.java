package com.example.projectdemo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ViewHolder extends RecyclerView.ViewHolder{

    //FoodItem2 foodItem2;

    TextView mNameTV;
    TextView mKnownTV;
    TextView mBrandTV;
    TextView mCategoryTV;
    TextView mExpiryDateTV;
    TextView mRemainingDaysTV;

    View mView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        //item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });

        //initialize views with model_layout.xml

        //initialize views with model_layout.xml
        mNameTV = itemView.findViewById(R.id.rNameTV);
        mKnownTV = itemView.findViewById(R.id.rKnownTV);
        mBrandTV = itemView.findViewById(R.id.rBrandTV);
        mCategoryTV = itemView.findViewById(R.id.rCategoryTV);
        mExpiryDateTV = itemView.findViewById(R.id.rExpiryDateTV);
        mRemainingDaysTV = itemView.findViewById(R.id.rRemainingDays);



            /*mNameTV.setText(foodItem2.getFoodName2());
            mKnownTV.setText(foodItem2.getKnownAs2());
            mBrandTV.setText(foodItem2.getBrand2());
            mCategoryTV.setText(foodItem2.getCategory2());
            mExpiryDateTV.setText(foodItem2.getExpiryDate2());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date expiryDate = null;
            try {
                expiryDate = sdf.parse(foodItem2.getExpiryDate2());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long timeDiff = expiryDate.getTime() - Calendar.getInstance().getTime().getTime();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(timeDiff);

            mRemainingDaysTV.setText("This item will expire in " + daysDiff + " days");*/


    }

    private ViewHolder.ClickListener mClickListener;

    //interface for click listener
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
