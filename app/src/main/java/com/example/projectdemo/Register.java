package com.example.projectdemo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finalfoodagram-default-rtdb.firebaseio.com");
    private EditText fullNameEt;
    private EditText numberEt;
    private EditText emailEt;
    private EditText passwordEt;

    private Button register_btn;

    private TextView loginNowBtn;

    private FirebaseAuth mAuth;

    private FirebaseFirestore fStore;

    private String userID;

    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        fullNameEt = findViewById(R.id.registerName);
        numberEt = findViewById(R.id.registerNumber);
        emailEt = findViewById(R.id.registerEmail);
        passwordEt = findViewById(R.id.registerPassword);

        fStore = FirebaseFirestore.getInstance();

        loginNowBtn = findViewById(R.id.loginRedirectText);
        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);

                //startActivity(new Intent(Register.this, Login.class));
                //finish();
            }
        });


        register_btn = findViewById(R.id.registerBtn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();

            }
        });
    }


    private void registerUser() {
        String fullName = fullNameEt.getText().toString();
        String number = numberEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (fullName.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (number.length() < 10) {
            Toast.makeText(Register.this, "Phone number should be up to 10 characters", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(Register.this, "The minimum password length is 5 characters", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(Register.this, "Please provide a valid email address", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        //User user = new User(userID, fullName, number, email);

                        //sendEmailVerification();

                        if (task.isSuccessful()) {
                            userID = mAuth.getCurrentUser().getUid();
                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String, Object> userObject = new HashMap<>();
                            userObject.put("id", userID);
                            userObject.put("fullName", fullName);
                            userObject.put("number", number);
                            userObject.put("email", email);
                            userObject.put("imageURL", "default");
                            userObject.put("status", "offline");
                            userObject.put("search", fullName.toLowerCase());
                            documentReference.set(userObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //Toast.makeText(Register.this, "Successful!", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(Register.this, "User signed in successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Register.this, Dashboard.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }else{
                            Toast.makeText(Register.this, "Failed to register with Cloud!", Toast.LENGTH_LONG).show();
                        }

















                        //userCloudRegistration();




                        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userID);
                        hashMap.put("fullName", fullName);
                        hashMap.put("number", number);
                        hashMap.put("email", email);
                        hashMap.put("imageURL", "default");
                        hashMap.put("status", "offline");
                        hashMap.put("search", fullName.toLowerCase());

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Register.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(Register.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
























                        /*FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "User has been registered successfully", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(Register.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });*/

                    } else {
                        Toast.makeText(Register.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }

    }

    private void sendEmailVerification() {
        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
        user1.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "User registered successfully. Please verify your email id", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Register.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*private void userCloudRegistration() {

        String fullName = fullNameEt.getText().toString();
        String number = numberEt.getText().toString();
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        if (fullName.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else if (number.length() < 10) {
            Toast.makeText(Register.this, "Phone number should be up to 10 characters", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 5) {
            Toast.makeText(Register.this, "The minimum password length is 5 characters", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(Register.this, "Please provide a valid email address", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        userID = mAuth.getCurrentUser().getUid();
                        Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", fullName);
                        user.put("phone", number);
                        user.put("email", email);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Register.this, "Successful!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });

                }else{
                        Toast.makeText(Register.this, "Failed to register with Cloud!", Toast.LENGTH_LONG).show();
                    }


        }




});

}
    }*/



}