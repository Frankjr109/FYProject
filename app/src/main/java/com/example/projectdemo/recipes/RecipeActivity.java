package com.example.projectdemo.recipes;

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
import com.example.projectdemo.R;
import com.example.projectdemo.Recipe;

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

        String url = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=&ingredients="+searchQuery;

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

    private void parseJSON(String jsonString) {
        try {
            JSONArray recipesArray = new JSONArray(jsonString);

            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject recipeObject = recipesArray.getJSONObject(i);
                String title = recipeObject.getString("title");
                String image = recipeObject.getString("image");
                String id = recipeObject.getString("id");

                Recipe recipe = new Recipe(title, image,id);
                recipeList.add(recipe);
            }

            //set up recycler view
            RecipeListAdapter adapter = new RecipeListAdapter(RecipeActivity.this, recipeList);
            LinearLayoutManager llm = new LinearLayoutManager(RecipeActivity.this);
            recipeRecyclerView.setLayoutManager(llm);
            recipeRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}