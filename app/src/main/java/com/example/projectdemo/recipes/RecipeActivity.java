package com.example.projectdemo.recipes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.projectdemo.Adapter.DonationsListAdapter;
import com.example.projectdemo.Adapter.RecipeListAdapter;
import com.example.projectdemo.Ingredient;
import com.example.projectdemo.R;
import com.example.projectdemo.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity {
    private String searchQuery;
    private ArrayList<Recipe> recipeList;
    private  RecyclerView recipeRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        searchQuery = getIntent().getStringExtra("search");
        recipeList = new ArrayList<>();

        recipeRecyclerView = findViewById(R.id.recipeRecyclerView);

        getRecipesList();

    }

    void getRecipesList(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://api.spoonacular.com/recipes/findByIngredients?d549421d9668bc9d5385791b&ingredients="+searchQuery;

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

    void getReviewsFromDB(){
        ArrayList<Recipe> reviews = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("recipeReviews").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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

                //set up recycler view
                RecipeListAdapter adapter = new RecipeListAdapter(RecipeActivity.this, recipeList, reviews);
                LinearLayoutManager llm = new LinearLayoutManager(RecipeActivity.this);
                recipeRecyclerView.setLayoutManager(llm);
                recipeRecyclerView.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("RecipeActivity // ", e.toString());
            }
        });


    }

    private void parseJSON(String jsonString) {
        try {
            JSONArray recipesArray = new JSONArray(jsonString);

            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject recipeObject = recipesArray.getJSONObject(i);
                String title = recipeObject.getString("title");
                String image = recipeObject.getString("image");
                String id = recipeObject.getString("id");

                ArrayList<Ingredient> ingredients = new ArrayList<>();
                JSONArray jsonIngredients = recipeObject.getJSONArray("missedIngredients");

                for(int j=0; j<jsonIngredients.length();j++){
                    JSONObject ingObj =  jsonIngredients.getJSONObject(j);
                    Ingredient ingredient = new Ingredient(
                           ingObj.getString("name"),
                            ingObj.getString("image")
                    );
                    ingredients.add(ingredient);
                }

                Recipe recipe = new Recipe(title, image,id);
                recipe.setIngredients(ingredients);
                recipeList.add(recipe);
            }

           getReviewsFromDB();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}