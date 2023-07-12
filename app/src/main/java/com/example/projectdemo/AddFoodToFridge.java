package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectdemo.recipes.RecipeActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.internal.Util;

public class AddFoodToFridge extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String userID;

    FoodAdapter foodAdapter;

    private DatePickerDialog picker;

    RecyclerView recyclerView;
    ArrayList<FoodItem> foodItemArrayList;
    FoodRecyclerAdapter adapter;

    Button buttonAdd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_to_fridge);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener((v)-> startActivity(new Intent(AddFoodToFridge.this,FoodDetailsActivity.class)));

        setupRecyclerView();


        /*buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                //viewDialogAdd.showDialog(AddFoodToFridge.this);
            }
        });*/

        //readData();

    }

    public void setupRecyclerView(){

        Query query = Utility.getCollectionReferenceForFoods().orderBy("foodExpiryDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FoodItem> options = new FirestoreRecyclerOptions.Builder<FoodItem>()
                .setQuery(query,FoodItem.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter = new FoodAdapter(options,this);
        recyclerView.setAdapter(foodAdapter);


        Button showRecipesButton = findViewById(R.id.recipesButton);
        showRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodItemArrayList = new ArrayList<>();

                // add all items from the FirestoreRecyclerOptions to the foodItemArrayList
                foodItemArrayList.addAll(foodAdapter.getSnapshots());

                // start collecting the ingredients with the first item in the arraylist
                StringBuilder allIngredients = new StringBuilder(foodItemArrayList.get(0).getFoodName());

                // loop over the ingredients and append it to the allIngredients string
                for(int i = 1; i<foodItemArrayList.size();i++){
                    allIngredients.append(",").append(foodItemArrayList.get(i).getFoodName());
                }

                // send allIngrendints (comma separated) to the RecipeActivity search through spoonacular api
                Intent i = new Intent(AddFoodToFridge.this, RecipeActivity.class);
                i.putExtra("search", allIngredients.toString());
                startActivity(i);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        foodAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        foodAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        foodAdapter.notifyDataSetChanged();
    }

    /*private void readData(){


        //DocumentReference documentReference = fStore.collection("foods").document(userID);


        databaseReference.child("USERS").orderByChild("userName").addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersItemArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    UsersItem users = dataSnapshot.getValue(UsersItem.class);
                    usersItemArrayList.add(users);
                }
                adapter = new UsersRecyclerAdapter(MainActivity.this, usersItemArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/


    /*public class ViewDialogAdd{
        public void showDialog(Context context){

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_add_new_food);

            EditText textName = dialog.findViewById(R.id.textName);
            EditText textQuantity = dialog.findViewById(R.id.textQuantity);
            EditText textExpiryDate = dialog.findViewById(R.id.textExpiryDate);

            //This is for drop down list of categories

            String[] items = {"Dairy", "Fats", "Fruits", "Vegetables", "Grains", "Protein"};

            AutoCompleteTextView autoCompleteTxt;

            ArrayAdapter<String> adapterItems;

            autoCompleteTxt = dialog.findViewById(R.id.auto_complete_txt);
            adapterItems = new ArrayAdapter<String>(getApplicationContext(),R.layout.list_item,items);

            autoCompleteTxt.setAdapter(adapterItems);

            autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(),"Item: " + item,Toast.LENGTH_SHORT).show();
                }
            });

            //Setting up now the Date picker for the expiry Date edit text
            textExpiryDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int month = calendar.get(Calendar.MONTH);
                    int year = calendar.get(Calendar.YEAR);

                    //Date Picker dialog
                     picker = new DatePickerDialog(AddFoodToFridge.this, new DatePickerDialog.OnDateSetListener() {
                         @Override
                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                             textExpiryDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                         }
                     },year, month, day);
                     picker.show();
                }
            });


            Button buttonAdd = dialog.findViewById(R.id.buttonAdd1);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);


            buttonAdd.setText("ADD");
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String name = textName.getText().toString();
                    String category = autoCompleteTxt.getText().toString();
                    String quantity = textQuantity.getText().toString();
                    String expiryDate = textExpiryDate.getText().toString();


                    if (name.isEmpty() || category.isEmpty() || quantity.isEmpty() || expiryDate.isEmpty()) {
                        Toast.makeText(context, "Please Enter All data...", Toast.LENGTH_SHORT).show();
                    } else {


                        userID = mAuth.getCurrentUser().getUid();
                        //Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                        DocumentReference documentReference = fStore.collection("foods").document(userID);
                        Map<String, Object> foodObject = new HashMap<>();
                        foodObject.put("foodName", name);
                        foodObject.put("category", category);
                        foodObject.put("quantity", quantity);
                        foodObject.put("expiryDate", expiryDate);
                        documentReference.set(foodObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddFoodToFridge.this, "Successful!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddFoodToFridge.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });


        }
    }*/


}
