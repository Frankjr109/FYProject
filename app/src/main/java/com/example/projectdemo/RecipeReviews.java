package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.projectdemo.Adapter.RecipeListAdapter;
import com.example.projectdemo.Adapter.ReviewsAdapter;
import com.example.projectdemo.recipes.RecipeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecipeReviews extends AppCompatActivity {
    ArrayList<Recipe> reviews;
    RecyclerView reviewsRecyclerView;
    Button orderLikes;
    Button orderDislikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_reviews);

        reviewsRecyclerView = findViewById(R.id.reviewedRecyclerView);
        orderLikes = findViewById(R.id.orderByLikesButton);
        orderDislikes = findViewById(R.id.orderByDislikesButton);

        reviews = new ArrayList<>();
        getReviewsFromDB();

        orderLikes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // order by decreasing likes
                Comparator<Recipe> comparator =
                        Comparator.comparingLong(Recipe::getLikeCount);

                Collections.sort(reviews, comparator);
                Collections.reverse(reviews);

                //set up recycler view
                ReviewsAdapter adapter = new ReviewsAdapter(RecipeReviews.this, reviews);
                LinearLayoutManager llm = new LinearLayoutManager(RecipeReviews.this);
                reviewsRecyclerView.setLayoutManager(llm);
                reviewsRecyclerView.setAdapter(adapter);

                orderLikes.setEnabled(false);
                orderDislikes.setEnabled(true);

                orderLikes.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                orderDislikes.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));

            }
        });

        orderDislikes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // order by decreasing dislikes
                Comparator<Recipe> comparator =
                        Comparator.comparingLong(Recipe::getDislikeCount);

                Collections.sort(reviews, comparator);
                Collections.reverse(reviews);

                //set up recycler view
                ReviewsAdapter adapter = new ReviewsAdapter(RecipeReviews.this, reviews);
                LinearLayoutManager llm = new LinearLayoutManager(RecipeReviews.this);
                reviewsRecyclerView.setLayoutManager(llm);
                reviewsRecyclerView.setAdapter(adapter);

                orderLikes.setEnabled(true);
                orderDislikes.setEnabled(false);

                orderLikes.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                orderDislikes.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));

            }
        });

    }

    void getReviewsFromDB(){
        reviews.clear();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recipeReviews").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Extract data from the document
                    String documentId = documentSnapshot.getId();

                    Recipe recipe = new Recipe(
                            documentSnapshot.getData().get("title").toString(),
                            documentSnapshot.getData().get("imageUrl").toString(),
                            documentId
                    );

                    ArrayList<String> dislikeArrayList = (ArrayList<String>) documentSnapshot.getData().get("dislikeArray");
// Convert the ArrayList to a String array
                    if(dislikeArrayList != null) {
                        String[] dislikeArray = new String[dislikeArrayList.size()];
                        dislikeArray = dislikeArrayList.toArray(dislikeArray);
                        recipe.setDislikeArray(dislikeArray);
                        recipe.setDislikeCount((long) documentSnapshot.getData().get("dislikeCount"));
                    }else{
                        String [] empty = new String[0];
                        recipe.setDislikeArray(empty);
                        recipe.setDislikeCount(0);

                    }

                    ArrayList<String> likeArrayList = (ArrayList<String>) documentSnapshot.getData().get("likeArray");

                    if(likeArrayList != null){
                        // Get the likeArray from documentSnapshot

// Convert the ArrayList to a String array
                        String[] likeArray = new String[likeArrayList.size()];
                        likeArray = likeArrayList.toArray(likeArray);
                        recipe.setLikeArray(likeArray);
                        recipe.setLikeCount((long) documentSnapshot.getData().get("likeCount"));
                    }else{
                        String [] empty = new String[0];
                        recipe.setLikeArray(empty);
                        recipe.setLikeCount(0);

                    }

                    reviews.add(recipe);
                    // Now you can use the 'data' object as needed
                    // Log.d(TAG, "Document ID: " + documentId + ", Data: " + data.toString());
                }

                // order by decreasing likes
                Comparator<Recipe> comparator =
                        Comparator.comparingLong(Recipe::getLikeCount);

                Collections.sort(reviews, comparator);
                Collections.reverse(reviews);

                //set up recycler view
                ReviewsAdapter adapter = new ReviewsAdapter(RecipeReviews.this, reviews);
                LinearLayoutManager llm = new LinearLayoutManager(RecipeReviews.this);
                reviewsRecyclerView.setLayoutManager(llm);
                reviewsRecyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("RecipeActivity // ", e.toString());
            }
        });


    }
}