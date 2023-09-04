package com.example.projectdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    private CardView logOutCard;
    private CardView notesCard;
    private CardView foodCard;
    private CardView profileCard;
    private CardView barCodeScannerCard;
    private CardView foodReportBtn;
    private CardView myChatGBTCard;
    private CardView myDonationsCard;
    private CardView myPersonalChatCard;
    private CardView myReviewsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        logOutCard = findViewById(R.id.logOut);

        logOutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Dashboard.this, "You have signed out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Dashboard.this, Login.class));
                finish();
            }
        });

        notesCard = findViewById(R.id.Notes);
        notesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your personal notes!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, AddNote.class);
                startActivity(intent);
            }
        });

        foodCard = findViewById(R.id.addToYourFridge);
        foodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your food!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, AddFoodToFridge.class);
                startActivity(intent);
            }
        });

        profileCard = findViewById(R.id.Profile);
        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your Profile!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, MyProfile.class);
                startActivity(intent);
            }
        });

        barCodeScannerCard = findViewById(R.id.barCodeScanner);
        barCodeScannerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to the Bar Code Activity!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, BarCodeActivity.class); //change back to Bar code activity
                startActivity(intent);
            }
        });

        foodReportBtn = findViewById(R.id.foodReportButton); //Then change it back to what it was supposed to be
        foodReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ReportActivity.class);
                startActivity(intent);
            }
        });



        myPersonalChatCard = findViewById(R.id.myPersonalChat);
        myPersonalChatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your Personal Chats!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, MyChatActivity.class);
                startActivity(intent);
            }
        });

        myDonationsCard = findViewById(R.id.myDonations);
        myDonationsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your Donations Page!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, MyDonationsActivity.class);
                startActivity(intent);
            }
        });

        myReviewsCard = findViewById(R.id.myReviews);
        myReviewsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your Reviews Page!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, RecipeReviews.class);
                startActivity(intent);
            }
        });
    }
}