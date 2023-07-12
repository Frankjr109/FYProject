package com.example.projectdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectdemo.DonationListing;
import com.example.projectdemo.DonationListingActivity;
import com.example.projectdemo.MessageActivity;
import com.example.projectdemo.Model.Chat;
import com.example.projectdemo.R;
import com.example.projectdemo.Recipe;
import com.example.projectdemo.User;
import com.example.projectdemo.recipes.RecipeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList;

    public RecipeListAdapter(Context mContext, List<Recipe> listings){
        this.mContext = mContext;
        this.recipeList = listings;
    }

    @NonNull
    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item_layout, parent, false);
        return new RecipeListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.recipeTitle.setText(recipe.getTitle());

        Glide.with(mContext)
                .load(recipe.getImageUrl())
                .into(holder.recipeImg);

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                String url = "https://api.spoonacular.com/recipes/"+recipe.getId()+"/information?apiKey=";

                // Create a GET request
                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response
                                // Called when the request is successful
                                Log.i("TESTING//", response);
                                parseJSON(response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle the error
                                // Called when there is an error with the request
                            }
                        });

                requestQueue.add(request);
            }
        });

    }

    private void parseJSON(String jsonString) {
        try {
            JSONObject recipeData = new JSONObject(jsonString);

            String recipeUrl = recipeData.getString("spoonacularSourceUrl");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipeUrl));

            mContext.startActivity(intent);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView recipeTitle;
        public ImageView recipeImg;
        public LinearLayoutCompat itemCard;

        public ViewHolder(View itemView){
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeImg = itemView.findViewById(R.id.recipeImage);
            itemCard = itemView.findViewById(R.id.recipeCard);
        }
    }
}
