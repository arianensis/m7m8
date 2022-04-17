package com.example.fragments;

import static com.example.fragments.Config.DefaultConstants.ACCOUNT_ID;
import static com.example.fragments.Config.DefaultConstants.API_KEY;
import static com.example.fragments.Config.DefaultConstants.SESSION_ID;
import static com.example.fragments.Config.DefaultConstants.retrofit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fragments.Config.ApiCall;
import com.example.fragments.Config.Meridia;
import com.example.fragments.Model.Film.Film;
import com.example.fragments.Model.Film.FilmResults;
import com.example.fragments.Model.List.ListRequest;
import com.example.fragments.Model.List.ListResults;
import com.example.fragments.Model.List.TMDBList;
import com.example.fragments.Recyclers.AddMovieListsRecyclerViewAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListFragment extends Fragment {

    public ListFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView; // add recycler view reference


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Load the list of lists
        ApiCall apiCall = retrofit.create(ApiCall.class);
        // call (account, "lists" api_key, session) for lists
        Call<ListResults> call = apiCall.getData(ACCOUNT_ID, API_KEY, SESSION_ID);

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        call.enqueue(new Callback<ListResults>(){
            @Override
            public void onResponse(Call<ListResults> call, Response<ListResults> response) {
                if (response.code()<200 || response.code()>299) /* any 2xx means success */ {
                    Log.i("testApi", "Response code: " + response.code());
                } else {
                    ArrayList<TMDBList> arraySearch;
                    arraySearch = response.body().getResults();
                    callRecycler(arraySearch);

                    FloatingActionButton btnAdd = view.findViewById(R.id.btnAddList);

                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showDialog();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ListResults> call, Throwable t) {

            }
        });


        recyclerView = view.findViewById(R.id.recyclerLists);
        return view;
    }

    public void showDialog() {
        View alertCustomdialog = getLayoutInflater().inflate(R.layout.form_add_list, null);

        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //set our custom alert dialog to tha alertdialog builder
        alert.setView(alertCustomdialog);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        Button btnSaveList = alertCustomdialog.findViewById(R.id.btnSaveList);

        btnSaveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create the listRequest
                ListRequest listRequest = new ListRequest(
                        ((TextView) alertCustomdialog.findViewById(R.id.txtList)).getText().toString(),
                        ((TextView) alertCustomdialog.findViewById(R.id.txtDescription)).getText().toString(),
                        "en" /* this will be default by now */
                );

                // try to add list
                ApiCall apiCall = retrofit.create(ApiCall.class);
                // call (api_key, session, listRequest) to add list
                Call<ListResults> call = apiCall.getData(API_KEY, SESSION_ID, listRequest);

                call.enqueue(new Callback<ListResults>() {
                    @Override
                    public void onResponse(Call<ListResults> call, Response<ListResults> response) {
                        if (response.code() < 200 || response.code() > 299) /* any 2xx means success */ {
                            Log.i("testApi", "Response code: " + response.code());
                        } else {
                            Intent back = new Intent(view.getContext(), MainActivity.class);
                            MainActivity.mode = 1;
                            startActivity(back);
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResults> call, Throwable t) {
                        dialog.dismiss();
                    }


                });
            }
        });
    }

    public void callRecycler(ArrayList<TMDBList> arraySearch){
        AddMovieListsRecyclerViewAdapter adapter = new AddMovieListsRecyclerViewAdapter(arraySearch, getContext(), false, null);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        recyclerView.setAdapter(adapter);
        Log.i("testApi", "Found " +arraySearch.size()+ " lists");
    }
}