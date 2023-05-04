package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyProfile extends AppCompatActivity {

    private TextView name;
    private TextView number;
    private TextView email;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    String userID = currentUser.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //Initialising the variables from the layouts
        name = findViewById(R.id.realName_textview);
        number = findViewById(R.id.phone_textview);
        email = findViewById(R.id.email_textview);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                /* for(DataSnapshot data : snapshot.getChildren()){

                 */
                User user1 = snapshot.getValue(User.class);
                name.setTextColor(Color.BLACK);
                name.setText("Great to see you join foodagram " + user1.getFullName());
                number.setText(user1.getNumber());
                email.setText(user1.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}


