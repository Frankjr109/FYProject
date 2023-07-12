package com.example.projectdemo;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScanProductActivity extends AppCompatActivity {

    private DatePickerDialog picker;

    //Retrieving the barCode using TextView
    private TextView barCodeTV;

    private EditText foodName2ET;
    private EditText knownAs2ET;
    private EditText brand2ET;
    private EditText category2ET;
    private EditText expiryDate2ET;

    private Button mSaveBtn;
    private Button mListBtn;


    private RequestQueue mQueue;

     ActionBar actionBar;

    //creating progress dialog
    ProgressDialog pd;

    //Firestore instance
    FirebaseFirestore db;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    String pId;
    String pName;
    String pKnownAs;
    String pBrand;
    String pCategory;
    String pExpiryDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_product);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Add Data");
        }
        //actionbar and its title
        //ActionBar actionBar = getSupportActionBar();
        //assert actionBar != null;
        //actionBar.setTitle("Add Data");

        //progress dialog
        pd = new ProgressDialog(this);

        //firestore
        db = FirebaseFirestore.getInstance();

        //Creating instance of request queue
        mQueue = Volley.newRequestQueue(this);


        foodName2ET = findViewById(R.id.foodName2);
        knownAs2ET = findViewById(R.id.foodKnownAs2);
        brand2ET = findViewById(R.id.foodBrand2);
        category2ET = findViewById(R.id.foodCategory2);
        expiryDate2ET = findViewById(R.id.foodExpiryDate2);
        expiryDate2ET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance(); //retrieving the current date and time from our system
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker dialog
                picker = new DatePickerDialog(ScanProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        expiryDate2ET.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mSaveBtn = findViewById(R.id.saveFoodBtn2);
        mListBtn = findViewById(R.id.listBtn);




        /*
        If we came here after clicking Update option(from AlertDialog of ListActivity)
        then get the data(name, knownas, brand, category and expirydate) from intent, and
        set to EditText
        Change title of Actionbar and save btn
         */

        /*final Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            //Update data
            actionBar.setTitle("Update");
            mSaveBtn.setText("Update");
            //get data
            pId = bundle.getString("pId");
            pName = bundle.getString("pName");
            pKnownAs = bundle.getString("pKnownAs");
            pBrand = bundle.getString("pBrand");
            pCategory = bundle.getString("pCategory");
            pExpiryDate = bundle.getString("pExpiryDate");
            //set data
            foodName2ET.setText(pName);
            knownAs2ET.setText(pKnownAs);
            brand2ET.setText(pBrand);
            category2ET.setText(pCategory);
            expiryDate2ET.setText(pExpiryDate);
        }
        else{
            //New Data
            actionBar.setTitle("Add Data");
            mSaveBtn.setText("Save");
        }*/


        //Retrieving bar code from BarCodeActivity
        String barCode1 = getIntent().getStringExtra("barName");
        barCodeTV = findViewById(R.id.header_textview);
        barCodeTV.setText("Details for bar code: " + barCode1);
        if(barCodeTV!=null){
            getDetails(barCode1); //parameter as(myBarCode)
        }

        //Everything else comes after retrieving the bar code from BarCodeActivity
        /*
        If we came here after clicking Update option(from AlertDialog of ListActivity)
        then get the data(foodName, knownas, brand, category and expiryDate) from Edit Text,
        id from intent and update existing data on the basis of id.
         */
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //adding new
                    //inputting data
                    String foodName = foodName2ET.getText().toString();
                    String knownAs = knownAs2ET.getText().toString();
                    String brand = brand2ET.getText().toString();
                    String category = category2ET.getText().toString();
                    String expiryDate = expiryDate2ET.getText().toString();

                    if(foodName.isEmpty() || knownAs.isEmpty() || brand.isEmpty() || category.isEmpty() || expiryDate.isEmpty()){
                        Toast.makeText(getApplicationContext(), "All fields are required",Toast.LENGTH_SHORT).show();
                    }else{
                        //function call to upload data
                        //function to upload the data
                        uploadData(foodName, knownAs, brand, category, expiryDate);
                    }

            }
        });

        mListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScanProductActivity.this, AddFoodToFridge.class));
                finish();
            }
        });


    }


    private void getDetails(String barCode){
        String url = "https://api.edamam.com/api/food-database/v2/parser?app_id=c0dccf80&app_key=afbfe7f98e06228fe0e17b8e13af8fef&upc="+barCode+"&nutrition-type=cooking";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("hints");

                            for(int i = 0; i < jsonArray.length(); i++){

                                JSONObject jo1 = jsonArray.getJSONObject(i);
                                JSONObject foodObject = jsonArray.getJSONObject(0).getJSONObject("food");
                                JSONObject nutrientsObject = foodObject.getJSONObject("nutrients");

                                //JSONObject foodObject = (JSONObject)jo1.get("food");
                                //JSONObject nutrientObject = (JSONObject) jo1.get("nutrients");

                                String name = foodObject.getString("label");
                                foodName2ET.setText(name);

                                String known = foodObject.getString("knownAs");
                                knownAs2ET.setText(known);

                                String brand = foodObject.getString("brand");
                                brand2ET.setText(brand);

                                String category = foodObject.getString("category");
                                category2ET.setText(category);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }

    private void updateData(String id, String foodName, String knownAs, String brand, String category, String expiryDate){
        //set title of progress bar
        pd.setTitle("Updating data......");

        //show progress bar when user clicks save button
        pd.show();


        db.collection("mainFoods").document(firebaseUser.getUid()).collection("myMain_foods").document(id)
                .update("foodName", foodName, "knownAs", knownAs, "brand", brand, "category", category, "expiryDate", expiryDate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //called when updated successfully
                        pd.dismiss();
                        Toast.makeText(ScanProductActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //called when there is any error
                        pd.dismiss();

                        //get and show error message
                        Toast.makeText(ScanProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void uploadData(String foodName, String knownAs, String brand, String category, String expiryDate){
        //set title of progress bar
        pd.setTitle("Adding data to Firestore");

        //show progress bar when user clicks save button
        pd.show();
        //addition of new ID's to try to fix problem YESSSSSSSSSSSSSSSSSSSSSSSSSSSSSS

        //random id for each data to be stored
        String id = UUID.randomUUID().toString();

        Map<String, Object> food = new HashMap<>();
        //food.put("id", id);
        food.put("foodName", foodName);
        //food.put("search", foodName.toLowerCase());
        //food.put("knownAs", knownAs);
        //food.put("brand", brand);
        food.put("foodCategory", category);
        food.put("foodExpiryDate", expiryDate);
        food.put("foodQuantity", "1");



        //calling gpt 3 to generate a recipe with the thing that's been scanned
        /*OpenAiService service = new OpenAiService("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");
        CompletionRequest completionRequest = CompletionRequest
                .builder()
                .prompt("Create a simple waste free recipe using " + foodName)
                .model("gpt-3.5-turbo")
                .echo(true)
                .build();
        String result = service.createCompletion(completionRequest).toString();

        Log.d("ResultTest", "This is the result" + result);*/


        //add this data
        db.collection("foods").document(firebaseUser.getUid()).collection("my_foods").document(id).set(food)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //this will be called when data is added successfully

                        pd.dismiss();
                        Toast.makeText(ScanProductActivity.this, "Uploaded.....", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //will be called if there is any error while uploading

                        pd.dismiss();

                        Toast.makeText(ScanProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //GBT CODE


}