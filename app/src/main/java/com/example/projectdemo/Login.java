package com.example.projectdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    private EditText emailTxt;
    private EditText passwordTxt;

    private Button login_Btn;

    private TextView registerNowBtn;

    private TextView forgot_Password;

    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://finalfoodagram-default-rtdb.firebaseio.com");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.loginEmail);
        passwordTxt = findViewById(R.id.loginPassword);

        login_Btn = findViewById(R.id.loginBtn);

        registerNowBtn = findViewById(R.id.registerRedirectText);
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

                //startActivity(new Intent(Login.this, Register.class));
                //finish();
            }
        });

        forgot_Password = findViewById(R.id.forgotPasswordPage);
        forgot_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else if(password.length() < 5){
                    Toast.makeText(Login.this, "Password length must be greater than 5", Toast.LENGTH_SHORT).show();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Login.this, "Valid email address is required", Toast.LENGTH_SHORT).show();
                }else{

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                if(mAuth.getCurrentUser().isEmailVerified()){
                                    startActivity(new Intent(Login.this, Dashboard.class));
                                    finish();
                                }else{
                                    Toast.makeText(Login.this, "Please verify your email ID", Toast.LENGTH_SHORT).show();
                                }

                                Toast.makeText(Login.this, "User signed in successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this, Dashboard.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });












                }
            }


        });




    }
}
