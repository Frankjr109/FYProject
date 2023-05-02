package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    List<FoodItem2> foodItem2List = new ArrayList<>();
    RecyclerView mRecyclerView;

    //layout manager for recyclerView
    RecyclerView.LayoutManager layoutManager;

    Button mAddBtn2;

    //firestore instance
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    CustomAdapter adapter;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("List Data");

        //init firestore
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();


        //initialise views
        mRecyclerView = findViewById(R.id.recycler_view2);
        mAddBtn2 = findViewById(R.id.addBtn2);

        //set recycler view properties
        mRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        //init Progress Dialog
        pd = new ProgressDialog(this);

        //show data in recyclerView
        showData();

        //handle add button to go back to ScanProductActivity
        mAddBtn2.setBackgroundColor(getResources().getColor(R.color.purple_200));
        mAddBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this, BarCodeActivity.class));
                finish();
            }
        });

    }

    private void showData() {

        //set title of progress dialog
        pd.setTitle("Loading data...");

        //show progress dialog
        pd.show();

        db.collection("mainFoods").document(firebaseUser.getUid()).collection("myMain_foods")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //called when data is retrieved
                        pd.dismiss();

                        //show data
                        for(DocumentSnapshot doc: task.getResult()){
                            FoodItem2 foodItem2 = new FoodItem2(doc.getString("id"),
                                    doc.getString("foodName"),
                                    doc.getString("knownAs"),
                                    doc.getString("brand"),
                                    doc.getString("category"),
                                    doc.getString("expiryDate"));
                            foodItem2List.add(foodItem2);
                        }
                        //adapter
                        adapter = new CustomAdapter(ListActivity.this, foodItem2List);
                        //set adapter to recyclerView
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //called when there is any error while retrieving
                        pd.dismiss();

                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchData(String s){
        //set title of progress bar
        pd.setTitle("Searching....");
        //show progress bar when user clicks save Button
        pd.show();
        db.collection("mainFoods").document(firebaseUser.getUid()).collection("myMain_foods").whereEqualTo("search", s.toLowerCase())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //called when searching is succeedded
                        foodItem2List.clear();
                        pd.dismiss();

                        for(DocumentSnapshot doc: task.getResult()){
                            FoodItem2 foodItem2 = new FoodItem2(doc.getString("id"),
                                    doc.getString("foodName"),
                                    doc.getString("knownAs"),
                                    doc.getString("brand"),
                                    doc.getString("category"),
                                    doc.getString("expiryDate"));
                            foodItem2List.add(foodItem2);
                        }
                        //adapter
                        adapter = new CustomAdapter(ListActivity.this, foodItem2List);
                        //set adapter to recyclerView
                        mRecyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //when there is any error
                        pd.dismiss();
                        //get and show error message
                        Toast.makeText(ListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu_main.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //SearchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //called when we press search button
                searchData(query); //function call with string entered in searchView as parameter
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called as and when we type even a single letter
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle other menu item clicks here
        if(item.getItemId() == R.id.action_settings){
            Toast.makeText(this, "DashBoard", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ListActivity.this, Dashboard.class); //change back to Bar code activity
            startActivity(intent);
        }
        if(item.getItemId() == R.id.action_logOut){
            Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent intent = new Intent(ListActivity.this, Login.class); //change back to Bar code activity
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}



