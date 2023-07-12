package com.example.projectdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectdemo.recipes.RecipeActivity;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoodAdapter extends FirestoreRecyclerAdapter<FoodItem, FoodAdapter.FoodViewHolder> {

    Context context;

    public FoodAdapter(@NonNull FirestoreRecyclerOptions<FoodItem> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull FoodItem model) {


        holder.nameTextView.setText(model.getFoodName());
        holder.categoryTextView.setText(model.getFoodCategory());
        holder.quantityTextView.setText(model.getFoodQuantity());
        holder.expiryDateTextView.setText(model.getFoodExpiryDate());

        holder.nameTextView.setText(model.getFoodName());
        holder.categoryTextView.setText(model.getFoodCategory());
        holder.quantityTextView.setText(model.getFoodQuantity());
        holder.expiryDateTextView.setText(model.getFoodExpiryDate());

        // Calculate days until expiry
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar expiryDate = Calendar.getInstance();
        Calendar currentDate = Calendar.getInstance();
        try {
            expiryDate.setTime(format.parse(model.getFoodExpiryDate()));
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        long diffInMillis = expiryDate.getTimeInMillis() - currentDate.getTimeInMillis();
        int daysUntilExpiry = (int) (diffInMillis / (24 * 60 * 60 * 1000));

        if(daysUntilExpiry <= 0){
            holder.daysTillExpiredTextView.setTextColor(Color.RED);
            holder.daysTillExpiredTextView.setText("Item has expired");
        }else{
            holder.daysTillExpiredTextView.setText(daysUntilExpiry + " days left until expiry");
        }
        //holder.daysTillExpiredTextView.setText(daysUntilExpiry + " days left until expiry");

        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context, FoodDetailsActivity.class);
            intent.putExtra("foodName", model.getFoodName());
            intent.putExtra("category", model.getFoodCategory());
            intent.putExtra("quantity", model.getFoodQuantity());
            intent.putExtra("expiryDate", model.getFoodExpiryDate());
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        });

        holder.recipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, RecipeActivity.class);
                i.putExtra("search",model.getFoodName());
                context.startActivity(i);
            }
        });


    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_food_item,parent,false);
        return new FoodViewHolder(view);
    }

    //this class will hold the view for the recycler food item
    class FoodViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView categoryTextView;
        TextView quantityTextView;
        TextView expiryDateTextView;

        //These are the new textViews added in for the checking of the expiry date
        TextView daysTillExpiredTextView;

        Button recipeButton;


        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textName);
            categoryTextView = itemView.findViewById(R.id.textCategory);
            quantityTextView = itemView.findViewById(R.id.textQuantity);
            expiryDateTextView = itemView.findViewById(R.id.textExpiryDate);

            daysTillExpiredTextView = itemView.findViewById(R.id.daysLeftTillExpired);
            recipeButton = itemView.findViewById(R.id.showRecipesButton);





            //try workout everything in the ViewHolder class
            /*daysTillExpiredTextView.setText("There is 5 days left from this current date" + expiryDateTextView.getText());

            CurrentProgress = CurrentProgress + 10;
            viewProgressBar.setProgress(CurrentProgress);
            viewProgressBar.setMax(100);*/

            //rememeber to get the docId for each food item in order to display the individual expiry date.


        }
    }
}
