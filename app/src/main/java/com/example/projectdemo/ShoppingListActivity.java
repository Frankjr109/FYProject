package com.example.projectdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.projectdemo.Adapter.ShoppingListAdapter;
import com.example.projectdemo.recipes.RecipeActivity;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        ArrayList<String> names = getIntent().getStringArrayListExtra("names");
        ArrayList<String> images = getIntent().getStringArrayListExtra("images");

        RecyclerView shoppingListRecycler = findViewById(R.id.shoppingListRecycler);

        ShoppingListAdapter adapter = new ShoppingListAdapter(this, names, images);
        LinearLayoutManager llm = new LinearLayoutManager(ShoppingListActivity.this);
        shoppingListRecycler.setLayoutManager(llm);
        shoppingListRecycler.setAdapter(adapter);


    }
}