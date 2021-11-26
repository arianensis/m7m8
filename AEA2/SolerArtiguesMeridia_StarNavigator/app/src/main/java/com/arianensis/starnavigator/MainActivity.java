package com.arianensis.starnavigator;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.arianensis.starnavigator.DB.StarsDBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static StarsDBHelper dbHelper;
    public static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemClock.sleep(500);
        setTheme(R.style.Theme_StarNavigator);

        super.onCreate(savedInstanceState);
        // Clear theme makes little sense in this app so we force it to be dark
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES);

        setContentView(R.layout.activity_main);

        if (SessionManager.loggedUsername.isEmpty()) {
            Intent askLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(askLogin);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        //Creation of the dbHelper
        dbHelper = new StarsDBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();
        // initialise the database with some "default" known stars
        SessionManager.loadDefaultStars();

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_list:
                    selectedFragment = new ListFragment();
                    break;
                case R.id.nav_form:
                    selectedFragment = new FormFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        });

    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        db.close();
        super.onDestroy();
    }
}