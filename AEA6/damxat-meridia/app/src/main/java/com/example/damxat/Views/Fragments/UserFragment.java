package com.example.damxat.Views.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.damxat.Adapter.RecyclerUserAdapter;
import com.example.damxat.Model.User;
import com.example.damxat.R;
import com.example.damxat.Views.Activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class UserFragment extends Fragment {

    RecyclerView recyclerUsers;
    FirebaseUser firebaseUser;
    DatabaseReference ref;
    View viewUsers;


    public UserFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewUsers = inflater.inflate(R.layout.fragment_user, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Users");

        recyclerUsers = viewUsers.findViewById(R.id.recyclerMyXats);
        recyclerUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        // Search all the users in the database to show the list
        getUsers();



        return viewUsers;
    }

    public void getUsers(){
        // Get the list of users from the database
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");

        ArrayList<User> arrayUsers = new ArrayList<>();

        // Listener to detect changes (if user state changes or new users appear)
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayUsers.clear();
                // Display all the users from the database except the current one because it doesn't make sense to see yourself in the list
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    try {
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            arrayUsers.add(user);
                        }
                    } catch (Exception e) {
                        arrayUsers.add(user);
                    }
                }

                try {
                    RecyclerUserAdapter adapter = new RecyclerUserAdapter(arrayUsers, getContext());
                    recyclerUsers.setAdapter(adapter);
                    recyclerUsers.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                } catch (Exception e) {}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}