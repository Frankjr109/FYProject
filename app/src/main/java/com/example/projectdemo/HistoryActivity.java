package com.example.projectdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.projectdemo.Adapter.DonationsListAdapter;
import com.example.projectdemo.Adapter.HistoryListAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<FoodItem> foodItemArrayList;
    ArrayList<String> weeksList;
    RecyclerView historyRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerview = findViewById(R.id.HistoryRecyclerView);

        weeksList = new ArrayList<>();
        foodItemArrayList= new ArrayList<>();

        for(int i=0; i<52;i++) {
            weeksList.add(i + "");
        }

         getItemsFromDB();
    }

    void getItemsFromDB(){
        foodItemArrayList.clear();
        Utility.getCollectionReferenceForFoods().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : querySnapshots) {

                    FoodItem item = new FoodItem(
                            documentSnapshot.getData().get("foodName").toString(),
                            documentSnapshot.getData().get("foodCategory").toString(),
                            documentSnapshot.getData().get("foodQuantity").toString(),
                            documentSnapshot.getData().get("foodExpiryDate").toString()
                    );

                    foodItemArrayList.add(item);

                }

                HistoryListAdapter adapter = new HistoryListAdapter(HistoryActivity.this, weeksList, foodItemArrayList);
                historyRecyclerview.setAdapter(adapter);
                LinearLayoutManager llm = new LinearLayoutManager(HistoryActivity.this);
                historyRecyclerview.setLayoutManager(llm);


            }
        });


    }
}