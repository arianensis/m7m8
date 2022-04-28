package com.example.damxat.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.damxat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// This activity is loaded if an user tries to log in from the StartActivity or if they just registered
public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Sets the reference of the firebase connection
        auth = FirebaseAuth.getInstance();

        Button loginButton = findViewById(R.id.groupAdd);
        EditText loginEmail = findViewById(R.id.groupName);
        EditText loginPassword = findViewById(R.id.loginPassword);

        // Listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                // Checks if user and password are filled in. Otherwise shows a toast to notify it
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                }else{
                    // If the fields are filled in, tries to login on firebase
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // Waits for petition to be completed
                            if(task.isSuccessful()){
                                // If it works, redirects the program to the MainActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                // If the login doesn't work, show a toast to notify it
                                Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}