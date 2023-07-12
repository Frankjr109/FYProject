package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {

    private TextView name;
    private EditText number;
    private EditText username;
    private TextView email;
    private ImageView userImg;
    private Button updateBtn;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    String userID = currentUser.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //Initialising the variables from the layouts
        name = findViewById(R.id.realName_textview);
        number = findViewById(R.id.phone_edittextview);
        username = findViewById(R.id.usernameEditText);
        email = findViewById(R.id.email_textview);
        userImg = findViewById(R.id.user_imageview);
        updateBtn = findViewById(R.id.buttonUpdate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                /* for(DataSnapshot data : snapshot.getChildren()){

                 */
                User user1 = snapshot.getValue(User.class);

                //Added my bit here by changing the error
                assert user1 != null;
                Glide.with(MyProfile.this).load(user1.getImageURL())
                        .apply(new RequestOptions()).into(userImg);

                name.setTextColor(Color.BLACK);
                name.setText("Great to see you join foodagram " + user1.getFullName());
                number.setText(user1.getNumber());
                email.setText(user1.getEmail());
                username.setText(user1.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("id", userID);
                hashMap.put("fullName", username.getText().toString());
                hashMap.put("number", number.getText().toString());
                hashMap.put("email", email.getText().toString());
                hashMap.put("imageURL", "default");
                hashMap.put("status", "offline");

                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText( MyProfile.this, "User data been updated successfully", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(MyProfile.this, "Failed to update! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });



    }
}


