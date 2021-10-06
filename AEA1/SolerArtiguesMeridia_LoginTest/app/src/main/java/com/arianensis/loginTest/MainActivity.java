package com.arianensis.loginTest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Creating the Java objects corresponding to the text fields
        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        // Creating the Java object for the login button
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // Creating the Java object for the bottom text (this will show if the login is correct)
        TextView lblLoginResult = findViewById(R.id.lblLoginResult);

        // Creating an event listener for "onClick" event in the button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v) {
                /* When clicked, this will log the result of the login attempt (in the Log.i and also in the bottom text):
                 * We will use the user "test" and the password "1234"
                 * If the fields are filled with the correct data, the login will be correct
                 * Otherwise the log will say what failed
                 */

                if (! txtUsername.getText().toString().equals("test")) {
                    Log.i("ERROR", "Unknown user.");
                    lblLoginResult.setText("User not found on the system");
                } else if (! txtPassword.getText().toString().equals("1234")) {
                    Log.i("ERROR", "Wrong password.");
                    lblLoginResult.setText("Your password is incorrect. Please check again");
                } else {
                    Log.i("TEST", "Login correct.");
                    lblLoginResult.setText("Login correct. Welcome!");
                }
            }
        });
    }
}