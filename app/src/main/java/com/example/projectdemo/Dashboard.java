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
    private CardView myProfile2Card;
    private CardView myChatGBTCard;
    private CardView myDonationsCard;
    private CardView myPersonalChatCard;

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

        myProfile2Card = findViewById(R.id.myProfile2); //Then change it back to what it was supposed to be
        myProfile2Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Dashboard.this, "Welcome to your Profile!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Dashboard.this, RecipeRecommendation.class);
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
                Intent intent = new Intent(Dashboard.this, MyChatActivity.class);
                startActivity(intent);
            }
        });
    }
}