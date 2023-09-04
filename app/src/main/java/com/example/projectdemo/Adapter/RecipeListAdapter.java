package com.example.projectdemo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.FileProvider;
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
import com.example.projectdemo.Ingredient;
import com.example.projectdemo.MessageActivity;
import com.example.projectdemo.Model.Chat;
import com.example.projectdemo.R;
import com.example.projectdemo.Recipe;
import com.example.projectdemo.ShoppingListActivity;
import com.example.projectdemo.User;
import com.example.projectdemo.recipes.RecipeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private Context mContext;
    private List<Recipe> recipeList;
    private ArrayList<Recipe> reviews;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;


    public RecipeListAdapter(Context mContext, List<Recipe> listings, ArrayList<Recipe> reviews){
        this.mContext = mContext;
        this.recipeList = listings;
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.reviews = reviews;
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

                String url = "https://api.spoonacular.com/recipes/"+recipe.getId()+"/information?";

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

                String url = "https://api.spoonacular.com/recipes/"+recipe.getId()+"/information?9668bc9d5385791b";

                // Create a GET request
                StringRequest request = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Handle the response
                                // Called when the request is successful
                                Log.i("TESTING//", response);
                                parseJSONAndShare(response,holder.recipeImg, recipe);

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

        Recipe reviewedRecipe = null;
        for(Recipe review: reviews){
            if(review.getId().equals(recipe.getId())){
                holder.likeCount.setText(review.getLikeCount()+"");
                holder.dislikeCount.setText(review.getDislikeCount()+"");
                reviewedRecipe = review;
            }

        }

        for(Recipe review: reviews){
            if(review.getId().equals(recipe.getId())){
               String myId = firebaseUser.getUid();
               if(
                       Arrays.asList(review.getLikeArray()).contains(myId) ||
                               Arrays.asList(review.getDislikeArray()).contains(myId)){
                   holder.likeButton.setEnabled(false);
                   holder.dislikeButton.setEnabled(false);
               }

            }


        }

        Recipe finalReviewedRecipe = reviewedRecipe;
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReview(recipe.getId(),"like", recipe);
                if(finalReviewedRecipe == null){
                    holder.likeCount.setText("1");
                } else {
                    holder.likeCount.setText((finalReviewedRecipe.getLikeCount()+1)+"");
                }

                holder.likeButton.setEnabled(false);
            }
        });

        holder.dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReview(recipe.getId(), "dislike", recipe);

                if(finalReviewedRecipe == null){
                    holder.dislikeCount.setText("1");
                }else{
                    holder.dislikeCount.setText((finalReviewedRecipe.getDislikeCount()+1)+"");
                }
                holder.dislikeButton.setEnabled(false);
            }
        });

        holder.cartButton.setOnClickListener(v -> {
            Intent i = new Intent(mContext, ShoppingListActivity.class);

            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> images = new ArrayList<>();

            for(Ingredient ingredient: recipe.getIngredients()){
                names.add(ingredient.getName());
                images.add(ingredient.getImgUrl());
            }

            i.putStringArrayListExtra("names", names);
            i.putStringArrayListExtra("images", images);

            mContext.startActivity(i);
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

    private void parseJSONAndShare(String jsonString, ImageView imgView, Recipe recipe){
        try {
            JSONObject recipeData = new JSONObject(jsonString);
            // get the recipe url from the api response data
            String recipeUrl = recipeData.getString("spoonacularSourceUrl");


            // get drawble image and convert to bitmap
        Drawable image = imgView.getDrawable();

        if(!(image instanceof BitmapDrawable)){
            return;
        }

        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        Uri imageUri = getBitMapUri(bitmap);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        String text = "Check out this recipe for "+recipe.getTitle()+" link to recipe: "+recipeUrl;
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        mContext.startActivity(Intent.createChooser(shareIntent, "Share recipe via"));

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
        public Button shareButton;
        public ImageButton likeButton, dislikeButton;
        public TextView likeCount, dislikeCount;
        public ImageButton cartButton;

        public ViewHolder(View itemView){
            super(itemView);

            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeImg = itemView.findViewById(R.id.recipeImage);
            itemCard = itemView.findViewById(R.id.recipeCard);
            shareButton = itemView.findViewById(R.id.shareRecipeButton);
            likeButton= itemView.findViewById(R.id.likeButton);
            dislikeButton = itemView.findViewById(R.id.dislikeButton);
            likeCount = itemView.findViewById(R.id.likeCount);
            dislikeCount = itemView.findViewById(R.id.dislikeCount);
            cartButton = itemView.findViewById(R.id.cartButton);
        }
    }

    private Uri getBitMapUri(Bitmap bitmap){
        // store the image in cache and get its URI path
        File cachePath = new File(mContext.getCacheDir(), "recipes");
        cachePath.mkdirs();
        File imageFile = new File(cachePath,"recipe.png");

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(mContext,"com.example.projectdemo.fileprovider",imageFile);
    }

    public void updateReview(String recipeId, String reviewType, Recipe recipe) {
        String userId = firebaseUser.getUid();
        DocumentReference recipeRef = db.collection("recipeReviews").document(recipeId);

        // Create a map to store the data you want to update
        Map<String, Object> updateData = new HashMap<>();

        if ("like".equals(reviewType)) {
            updateData.put("likeCount", FieldValue.increment(1));
            updateData.put("likeArray", FieldValue.arrayUnion(userId));
            updateData.put("imageUrl", recipe.getImageUrl());
            updateData.put("title",recipe.getTitle());
        } else if ("dislike".equals(reviewType)) {
            updateData.put("dislikeCount", FieldValue.increment(1));
            updateData.put("dislikeArray", FieldValue.arrayUnion(userId));
            updateData.put("imageUrl", recipe.getImageUrl());
            updateData.put("title",recipe.getTitle());
        }

        // Use a transaction to update or add the document
        db.runTransaction(transaction -> {
            DocumentSnapshot recipeSnapshot = transaction.get(recipeRef);

            if (recipeSnapshot.exists()) {
                // Document exists, update it
                transaction.update(recipeRef, updateData);
            } else {
                // Document does not exist, create it
                transaction.set(recipeRef, updateData, SetOptions.merge());
            }

            return null;
        }).addOnSuccessListener(new OnSuccessListener<Object>() {
            @Override
            public void onSuccess(Object o) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

}
