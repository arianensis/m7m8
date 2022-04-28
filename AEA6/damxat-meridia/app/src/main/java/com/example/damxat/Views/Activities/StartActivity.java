package com.example.damxat.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.damxat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// This activity is loaded when the app is started
public class StartActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        /*
         * this is executed after onCreate()
         * Tries to connect to the Firebase user to get access to the database.
         * If the connection is successful, loads the MainActivity. Otherwise it means
         * the user is not logged in, so it keeps the Login and Register buttons
         */
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Loads the start window with options to Login or Register. If there is already a
         * user logged in, the program will be redirected to the MainActivity
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button startLogin = findViewById(R.id.startLogin);
        Button startRegister = findViewById(R.id.startRegister);

        // Listener for the login button
        startLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creates a new intent to redirect the program to the LoginActivity and starts it
                Intent intentLogin = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intentLogin);
            }
        });

        // Listener for the register button
        startRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creates a new intent to redirect the program to the RegisterActivity and starts it
                Intent intentRegister = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });
    }
}