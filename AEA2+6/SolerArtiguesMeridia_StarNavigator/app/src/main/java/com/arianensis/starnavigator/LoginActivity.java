package com.arianensis.starnavigator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* This was used to debug the coordinates transformation methods
        double[] testCoords = new double[] {1.23, 4.56, 7.89};
        Log.i("DEBUG", "ORIGINAL X:" + testCoords[0] + ", Y:" + testCoords[1] + ", Z:" + testCoords[2]);
        testCoords = Star.cubicToSpherical(testCoords);
        Log.i("DEBUG", "CONVERTED RA:" + testCoords[0] + ", DEC:" + testCoords[1] + ", DIST:" + testCoords[2]);
        testCoords = Star.sphericalToCubic(testCoords);
        Log.i("DEBUG", "REVERTED X:" + testCoords[0] + ", Y:" + testCoords[1] + ", Z:" + testCoords[2]);
        */

        // Creating the Java objects corresponding to the text fields
        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        // Creating the Java object for the login button
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // Creating the Java object for the bottom text (this will show if the login is correct)
        TextView lblLoginResult = findViewById(R.id.lblLoginResult);
        lblLoginResult.setText("");

        // Creating an event listener for "onClick" event in the button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Currently this simulates a login but does not correspond to real users stored
                 * in a database. You can type whatever user so that the app will then greet you
                 * with the given name, and you can type whatever password. Only requisite is
                 * that none of the fields is empty when clicking Login
                 */

                if (txtUsername.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()) {
                    Log.i("ERROR", "Data missing");
                    SessionManager.loggedUsername = "";
                    lblLoginResult.setText(R.string.login_err_pass);
                } else {
                    Log.i("TEST", "Login correct.");
                    SessionManager.loggedUsername = txtUsername.getText().toString();
                    lblLoginResult.setText(R.string.login_log_ok);

                    // "Jump" to the home screen
                    Intent loginOK = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(loginOK);
                }
            }
        });
    }
}