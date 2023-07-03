package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectdemo.Notifications.NotificationScheduler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FoodDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    private String userID;

    private TextView deleteFoodTextViewBtn;

    private Button deleteFoodButton;
    private Button exitButton;

    private EditText textName;
    private EditText textQuantity;
    private EditText textExpiryDate;

    private Button addButton;

    private DatePickerDialog picker;

    AutoCompleteTextView autoCompleteTxt;

    TextView pageTitleTextView;
    String foodName;
    String category;
    String quantity;
    String expiryDate;
    String docId;

    String appropriateDaysLeft;
    String originalProgressBar;

    boolean isEditMode = false;
    long mil = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        fStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        deleteFoodTextViewBtn = findViewById(R.id.delete_food_text_view_btn);
        exitButton = findViewById(R.id.buttonCancel);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FoodDetailsActivity.this, "Welcome back to your Profile!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FoodDetailsActivity.this, Dashboard.class);
                startActivity(intent);
            }
        });

        textName = findViewById(R.id.textName);
        textQuantity = findViewById(R.id.textQuantity);
        textExpiryDate = findViewById(R.id.textExpiryDate);

        pageTitleTextView = findViewById(R.id.page_title);


        //receive data
        /*foodName = getIntent().getStringExtra("foodName");
        category = getIntent().getStringExtra("category");
        quantity = getIntent().getStringExtra("quantity");
        expiryDate = getIntent().getStringExtra("expiryDate");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && docId.isEmpty()){
            isEditMode = true;
        }

        textName.setText(foodName);
        autoCompleteTxt.setText(category);
        textQuantity.setText(quantity);
        textExpiryDate.setText(expiryDate);
        if(isEditMode){
            pageTitleTextView.setText("Edit your food");
        }*/



        addButton = findViewById(R.id.buttonAdd1);
        addButton.setOnClickListener((v) -> saveFood());


        //This is for drop down list of categories

        String[] items = {"Dairy", "Fats", "Fruits", "Vegetables", "Grains", "Protein"};


        ArrayAdapter<String> adapterItems;

        autoCompleteTxt = findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, items);

        autoCompleteTxt.setAdapter(adapterItems);

        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        textExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date Picker dialog
                picker = new DatePickerDialog(FoodDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        final Calendar calendar = Calendar.getInstance();
                        final Calendar currentTime = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE));
                        mil = calendar.getTimeInMillis();

                        textExpiryDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        //receive data
        foodName = getIntent().getStringExtra("foodName");
        category = getIntent().getStringExtra("category");
        quantity = getIntent().getStringExtra("quantity");
        expiryDate = getIntent().getStringExtra("expiryDate");
        appropriateDaysLeft = getIntent().getStringExtra("daysLeft");
        originalProgressBar = getIntent().getStringExtra("updatedProgressBar");


        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        textName.setText(foodName);
        autoCompleteTxt.setText(category);
        textQuantity.setText(quantity);
        textExpiryDate.setText(expiryDate);

        if(isEditMode){
            pageTitleTextView.setText("Edit your food");
            deleteFoodTextViewBtn.setVisibility(View.VISIBLE);
        }

        deleteFoodTextViewBtn.setOnClickListener((v)-> deleteFoodFromFirebase());



    }

    //All the private methods Go Here
    public void saveFood() {
        String foodName = textName.getText().toString();
        String foodCategory = autoCompleteTxt.getText().toString();
        String foodQuantity = textQuantity.getText().toString();
        String foodExpiryDate = textExpiryDate.getText().toString();

        if (foodName.isEmpty() || foodCategory.isEmpty() || foodQuantity.isEmpty() || foodExpiryDate.isEmpty()) {
            Toast.makeText(FoodDetailsActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }

        FoodItem foodItem = new FoodItem();
        foodItem.setFoodName(foodName);
        foodItem.setFoodCategory(foodCategory);
        foodItem.setFoodQuantity(foodQuantity);
        foodItem.setFoodExpiryDate(foodExpiryDate);

        NotificationScheduler.scheduleNotification(FoodDetailsActivity.this,
                foodItem.getFoodName(), "Your item is going to expire in 2 days! Donate it or use it in our suggested recipes!",
                mil);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(FoodDetailsActivity.this, "My notification");
        builder.setContentTitle("My title");
        builder.setContentText("This food expires on the " + foodItem.getFoodExpiryDate());
        builder.setSmallIcon(R.drawable.notification);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(FoodDetailsActivity.this);
        managerCompat.notify(1, builder.build());

        saveFoodToFirebase(foodItem);

        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(FoodDetailsActivity.this, "My notification");
        builder.setContentTitle("My title");
        builder.setContentText("This food expires on the " + foodItem.getFoodExpiryDate());
        builder.setSmallIcon(R.drawable.notification);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(FoodDetailsActivity.this);
        managerCompat.notify(1, builder.build());*/




    }

    public void saveFoodToFirebase(FoodItem foodItem){


        DocumentReference documentReference;
        if(isEditMode){

            //if it is editMode it will update the food item
            documentReference = Utility.getCollectionReferenceForFoods().document(docId);
        }else{
            //if it is not editMode it will create a new Food item
            documentReference = Utility.getCollectionReferenceForFoods().document(); //Inside the Foods collection we are creating one document
        }
        //documentReference = Utility.getCollectionReferenceForFoods().document(); //Inside the Foods collection we are creating one document

        documentReference.set(foodItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Food is added
                    Utility.showToast(FoodDetailsActivity.this, "food added successfully");
                    finish();
                }else{
                    Utility.showToast(FoodDetailsActivity.this, "Failed while adding food");
                }
            }
        });






        /*userID = mAuth.getCurrentUser().getUid();
        //Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
        DocumentReference documentReference = fStore.collection("foods").document(userID).collection("my_foods").document();
        HashMap<String, Object> foodItem = new HashMap<>();
        foodItem.put("foodName", .);
        foodObject.put("category", category);
        foodObject.put("quantity", quantity);
        foodObject.put("expiryDate", expiryDate);
        documentReference.set(foodItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(FoodDetailsActivity.this, "Successful!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FoodDetailsActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });*/
    }


    public void deleteFoodFromFirebase(){

        DocumentReference documentReference;

            documentReference = Utility.getCollectionReferenceForFoods().document(docId);

        //documentReference = Utility.getCollectionReferenceForFoods().document(); //Inside the Foods collection we are creating one document

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Food is deleted
                    Utility.showToast(FoodDetailsActivity.this, "food deleted successfully");
                    finish();
                }else{
                    Utility.showToast(FoodDetailsActivity.this, "Failed while deleting food");
                }
            }
        });





    }

}