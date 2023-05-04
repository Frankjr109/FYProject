package com.example.projectdemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

     List<Recipe> recipes;
     Context context;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
       // this.context = context;
        //this.recipes = recipes;

        this.context = context;
        if (recipes == null) {
            this.recipes = new ArrayList<>();
        } else {
            this.recipes = recipes;
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.food_view, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        /*Recipe recipe = recipes.get(position);
        holder.titleView.setText(recipe.getTitle());
        Picasso.get().load(recipe.getImageUrl()).into(holder.imageFoodView);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceUrl()));
            holder.itemView.getContext().startActivity(intent);
        });*/
        holder.titleView.setText(recipes.get(position).getTitle());
        holder.imageFoodView.setImageResource(recipes.get(position).getImageUrl());
        holder.UrlView.setText(recipes.get(position).getSourceUrl());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}

 class RecipeViewHolder extends RecyclerView.ViewHolder{

    ImageView imageFoodView;
    TextView titleView, UrlView;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);
        titleView = itemView.findViewById(R.id.title_text_view);
        imageFoodView = itemView.findViewById(R.id.image_view);
        UrlView = itemView.findViewById(R.id.url_text_view);
    }
}

