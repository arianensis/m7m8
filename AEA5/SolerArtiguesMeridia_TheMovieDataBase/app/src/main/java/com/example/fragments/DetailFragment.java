package com.example.fragments;

import static com.example.fragments.Config.DefaultConstants.ACCOUNT_ID;
import static com.example.fragments.Config.DefaultConstants.API_KEY;
import static com.example.fragments.Config.DefaultConstants.BASE_IMG_URL;
import static com.example.fragments.Config.DefaultConstants.SESSION_ID;
import static com.example.fragments.Config.DefaultConstants.retrofit;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragments.Config.ApiCall;
import com.example.fragments.Config.GlideApp;
import com.example.fragments.Config.Meridia;
import com.example.fragments.Model.Film.FavFilmRequest;
import com.example.fragments.Model.Film.FavFilmResponse;
import com.example.fragments.Model.Film.Film;
import com.example.fragments.Model.List.ListResults;
import com.example.fragments.Model.List.TMDBList;
import com.example.fragments.Recyclers.AddMovieListsRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Bundle bundle = getArguments();
        Film film = (Film) bundle.getSerializable("Film");
        Meridia.currentlyViewing = film;

        Log.i("FILM ID",""+film.getId());

        TextView txtDetailTitle = view.findViewById(R.id.txtDetailTitle);
        TextView txtDetailDesc = view.findViewById(R.id.txtDetailDesc);
        ImageView imgDetail = view.findViewById(R.id.imgDetail);

        ImageButton btnFav = view.findViewById(R.id.btnFav);

        // Change the favourite button according to whether the movie is in favourites list
        btnFav.setImageResource(Meridia.isFavourite(film) ? R.drawable.ic_fav_on : R.drawable.ic_fav_off);

        ImageButton btnAddtoList = view.findViewById(R.id.btnAddtoList);


        txtDetailTitle.setText(film.getOriginal_title());
        txtDetailDesc.setText(film.getOverview());

        GlideApp.with(getContext())
                .load(BASE_IMG_URL + film.getPoster_path())
                .centerCrop()
                .into(imgDetail);

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change favourite status

                ApiCall apiCall = retrofit.create(ApiCall.class);
                // call (account, api_key, session, favRequest) for setting favourite
                Call<FavFilmResponse> call = apiCall.getData(ACCOUNT_ID, API_KEY, SESSION_ID, new FavFilmRequest(film, !Meridia.isFavourite(film)));

                call.enqueue(new Callback<FavFilmResponse>() {
                    @Override
                    public void onResponse(Call<FavFilmResponse> call, Response<FavFilmResponse> response) {
                        if (response.code()<200 || response.code()>299) /* any 2xx means success */ {
                            Log.i("testApi", "Response code: " + response.code());
                            Log.i("request?", response.toString().split("url=")[1]);
                        } else {


                            // remove the movie from favourites in the local list
                            Meridia.setFavourite(film, !Meridia.isFavourite(film));
                            // change the button
                            btnFav.setImageResource(Meridia.isFavourite(film) ? R.drawable.ic_fav_on : R.drawable.ic_fav_off);
                        }
                    }

                    @Override
                    public void onFailure(Call<FavFilmResponse> call, Throwable t) {}
                });
            }
        });

        btnAddtoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiCall apiCall = retrofit.create(ApiCall.class);
                // call (account, "lists" api_key, session) for lists
                Call<ListResults> call = apiCall.getData(ACCOUNT_ID, API_KEY, SESSION_ID);

                call.enqueue(new Callback<ListResults>(){
                    @Override
                    public void onResponse(Call<ListResults> call, Response<ListResults> response) {
                        if (response.code()<200 || response.code()>299) /* any 2xx means success */ {
                            Log.i("testApi", "Response code: " + response.code());
                        } else {
                            showDialog(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<ListResults> call, Throwable t) {

                    }
                });
            }
        });


        return view;

    }

    public void showDialog(ArrayList<TMDBList> arrayTMDBList){
        View alertCustomdialog = getLayoutInflater().inflate( R.layout.form_movie_to_list, null);

        //initialize alert builder.
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //set our custom alert dialog to the alertdialog builder
        alert.setView(alertCustomdialog);

        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        RecyclerView recyclerView = alertCustomdialog.findViewById(R.id.recyclerList);
        AddMovieListsRecyclerViewAdapter adapter = new AddMovieListsRecyclerViewAdapter(arrayTMDBList, getContext(), true, dialog);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}