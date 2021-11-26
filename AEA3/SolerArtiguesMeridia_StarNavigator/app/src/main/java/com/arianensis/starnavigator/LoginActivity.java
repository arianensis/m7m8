package com.arianensis.starnavigator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    // Variables for the fingerprint login
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


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

        // Create a prompt to ask for biometric login
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.i("finguerprín", "error");
                //Error
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result); // if the fingerprint is correctly read, log in
                Meridia.login("Mr. Finger");
                // "Jump" to the home screen
                Intent loginOK = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(loginOK);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.i("finguerprín", "failed");
                //Failed
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.finger_prompt_title))
                .setNegativeButtonText(getString(R.string.finger_prompt_cancel))
                .build();
        biometricPrompt.authenticate(promptInfo);


        // The text fields
        EditText txtUsername = findViewById(R.id.txtUsername);
        EditText txtPassword = findViewById(R.id.txtPassword);

        // the login button
        Button btnSignIn = findViewById(R.id.btnSignIn);

        // the bottom text (this will show if the login is correct)
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
                    lblLoginResult.setText(R.string.login_err_pass);
                } else {
                    Log.i("TEST", "Login correct.");
                    Meridia.login(txtUsername.getText().toString());
                    lblLoginResult.setText(R.string.login_log_ok);

                    // "Jump" to the home screen
                    Intent loginOK = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(loginOK);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Overriding the back button to prevent going to Main view after logging out
        // doNothing();
    }
}