package com.example.damxat.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.example.damxat.R;
import com.example.damxat.Views.Fragments.GroupsFragment;
import com.example.damxat.Views.Fragments.MyXatsFragment;
import com.example.damxat.Views.Fragments.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// This activity is where the user can receive and send messages
public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    Toolbar toolbar;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity=this;

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyXatsFragment()).commit();

        // Sets the reference to the current logged in user (when this activity is loaded, there will already be an user)
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.top_menu);


        BottomNavigationView bottomNav = findViewById(R.id.main_menu);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.nav_xats:
                    selectedFragment = new MyXatsFragment();
                    break;

                case R.id.nav_group:
                    selectedFragment = new GroupsFragment();
                    break;

                case R.id.nav_users:
                    selectedFragment = new UserFragment();

                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        });
    }

    private void status(String status){
        // This method notifies if the local user is active or not to the database so others can know it
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        ref.updateChildren(hashMap);
    }

    // Notifies that the user is online when they resume the app
    @Override
    protected void onResume(){
        super.onResume();
        status("online");
    }

    // Notifies that the user is offline when the app is minimized
    @Override
    protected void onPause(){
        super.onPause();
        status("offline");
    }

    // Creates the menu when the menu button is clicked
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    // Calls the corresponding method to the option selected (in this case only one: log out)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("TESTMENU", "hola" + item.getItemId());
        switch(item.getItemId()){
            case R.id.nav_logout:
                // Logs the user out and goes back to the Start screen
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;
        }
        return false;
    }
}