package com.arianensis.starnavigator;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;

import com.arianensis.starnavigator.DB.StarsDBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static StarsDBHelper dbHelper;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemClock.sleep(500);
        setTheme(R.style.Theme_StarNavigator);

        // Passes the shared preferences to the auxiliar class Meridia so it can manage the settings from anywhere
        Meridia.loadSharedPrefs(getSharedPreferences("SharedP", Context.MODE_PRIVATE));

        super.onCreate(savedInstanceState);
        // Clear theme makes little sense in this app so we force it to be dark
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        // load the application in the language that is stored in the sharedPreferences
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration config = res.getConfiguration();
        config.setLocale(new Locale(Meridia.getLanguage()));
        res.updateConfiguration(config, dm);
        // and load the view
        setContentView(R.layout.activity_main);

        // if there is no user logged in, jump to the Login screen
        if (Meridia.getLoggedUsername().isEmpty()) {
            Intent askLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(askLogin);
        }

        // initialize the bottom menu
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        // if the activity is loaded from the setings, the selected butotn will be settings. Otherwise it will be home
        if (Meridia.changingSettings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
            bottomNav.setSelectedItemId(R.id.nav_settings);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }

        //Creation of the dbHelper
        dbHelper = new StarsDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        // initialise the database with some "default" known stars
        Meridia.loadDefaultStars();

        // Listen to the bottom buttons and show corresponding fragment
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    Meridia.changingSettings = false;
                    break;
                case R.id.nav_list:
                    selectedFragment = new ListFragment();
                    Meridia.changingSettings = false;
                    break;
                case R.id.nav_form:
                    selectedFragment = new FormFragment();
                    Meridia.changingSettings = false;
                    break;
                case R.id.nav_settings:
                    selectedFragment = new SettingsFragment();
                    Meridia.changingSettings = true;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });

        AppCompatActivity a = new AppCompatActivity();

    }

    // The database will be closed when we exit the application
    @Override
    protected void onDestroy() {
        dbHelper.close();
        db.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Overriding the back button to prevent it from reverting the settings. Instead, it will just go to the home fragment again
        Meridia.changingSettings = false;
        Intent back = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(back);
    }
}