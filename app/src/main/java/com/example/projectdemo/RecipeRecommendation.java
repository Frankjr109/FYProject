package com.example.projectdemo;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecommendation extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    List<Recipe> recipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_recommendation);

        recyclerView = findViewById(R.id.myRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recipeAdapter = new RecipeAdapter(getApplicationContext(),recipes);
        recyclerView.setAdapter(recipeAdapter);

        // Fetch the user's food items from Firestore
        getFoodItemsFromFirestore();

        //List<FoodItem> foodItems = new ArrayList<>();
        // Get a list of the user's food items from Firebase
        //List<FoodItem> foodItems = getFoodItemsFromFirebase();
        //List<FoodItem> foodItems = getFoodItemsFromFirebase();

        // Fetch recipes based on the user's food items
        //fetchRecipes(foodItems);
    }

    private void getFoodItemsFromFirestore() {
        // Get the collection reference for the user's food items
        CollectionReference foodItemsRef = getCollectionReferenceForFoods();

        // Query the food items
        foodItemsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<FoodItem> foodItems = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    FoodItem foodItem = document.toObject(FoodItem.class);
                    foodItems.add(foodItem);
                }

                // Fetch recommended recipes based on the user's food items
                fetchRecipes(foodItems);
            } else {
                Log.d(TAG, "Error getting food items: ", task.getException());
            }
        });
    }

    private void fetchRecipes(List<FoodItem> foodItems) {
        String ingredients = "";
        for (FoodItem foodItem : foodItems) {
            ingredients += foodItem.getFoodName() + ",";
        }
        String url = "https://api.spoonacular.com/recipes/findByIngredients?ingredients="
                + ingredients
                + "&number=10&aa3251c11b1";

        // Make the API request using Volley
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                          recipes = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject recipeObj = response.getJSONObject(i);
                            String title = recipeObj.getString("title");
                            //int imageUrl = recipeObj.getInt("image");
                            //String sourceUrl = recipeObj.getString("sourceUrl");
                          //  Recipe recipe = new Recipe(title);
                           // recipes.add(recipe);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // Display the recommended recipes in the RecyclerView
                    //recipeAdapter = new RecipeAdapter(getApplicationContext(),recipes);
                    //recyclerView.setAdapter(recipeAdapter);
                },
                Throwable::printStackTrace);

        // Add the API request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }





    private static CollectionReference getCollectionReferenceForFoods() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        return FirebaseFirestore.getInstance().collection("foods")
                .document(currentUser.getUid()).collection("my_foods");
    }

}