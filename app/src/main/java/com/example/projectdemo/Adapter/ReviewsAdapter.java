package com.example.projectdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projectdemo.R;
import com.example.projectdemo.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Recipe> reviews;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;


    public ReviewsAdapter(Context mContext, ArrayList<Recipe> reviews) {
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.reviewed_item, parent, false);
        return new ReviewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = reviews.get(position);

        holder.recipeTitle.setText(recipe.getTitle());

        Glide.with(mContext)
                .load(recipe.getImageUrl())
                .into(holder.recipeImg);

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                String url = "https://api.spoonacular.com/recipes/" + recipe.getId() + "/information?d9668bc9d5385791b";

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

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue requestQueue = Volley.newRequestQueue(mContext);

                String url = "https://api.spoonacular.com/recipes/" + recipe.getId() + "/information?a0c668bc9d53891b";

                // Create a GET request
                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response
                                // Called when the request is successful
                                Log.i("TESTING//", response);
                                parseJSONAndShare(response, holder.recipeImg, recipe);

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


                holder.likeCount.setText(recipe.getLikeCount() + "");
                holder.dislikeCount.setText(recipe.getDislikeCount() + "");



        holder.likeButton.setEnabled(false);
        holder.dislikeButton.setEnabled(false);

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

    private void parseJSONAndShare(String jsonString, ImageView imgView, Recipe recipe) {
        try {
            JSONObject recipeData = new JSONObject(jsonString);
            // get the recipe url from the api response data
            String recipeUrl = recipeData.getString("spoonacularSourceUrl");


            // get drawble image and convert to bitmap
            Drawable image = imgView.getDrawable();

            if (!(image instanceof BitmapDrawable)) {
                return;
            }

            Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
            Uri imageUri = getBitMapUri(bitmap);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            String text = "Check out this recipe for " + recipe.getTitle() + " link to recipe: " + recipeUrl;
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            mContext.startActivity(Intent.createChooser(shareIntent, "Share recipe via"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitle;
        public ImageView recipeImg;
        public LinearLayoutCompat itemCard;
        public Button shareButton;
        public ImageButton likeButton, dislikeButton;
        public TextView likeCount, dislikeCount;

        public ViewHolder(View itemView) {
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeImg = itemView.findViewById(R.id.recipeImage);
            itemCard = itemView.findViewById(R.id.recipeCard);
            shareButton = itemView.findViewById(R.id.shareRecipeButton);
            likeButton = itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
            likeCount = itemView.findViewById(R.id.likeCount);
            dislikeCount = itemView.findViewById(R.id.dislikeCount);
        }
    }

    private Uri getBitMapUri(Bitmap bitmap) {
        // store the image in cache and get its URI path
        File cachePath = new File(mContext.getCacheDir(), "recipes");
        cachePath.mkdirs();
        File imageFile = new File(cachePath, "recipe.png");

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(mContext, "com.example.projectdemo.fileprovider", imageFile);
    }
}