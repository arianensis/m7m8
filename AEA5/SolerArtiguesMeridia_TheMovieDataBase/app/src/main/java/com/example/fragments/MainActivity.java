package com.example.fragments;

import static com.example.fragments.Config.DefaultConstants.ACCOUNT_ID;
import static com.example.fragments.Config.DefaultConstants.API_KEY;
import static com.example.fragments.Config.DefaultConstants.SESSION_ID;
import static com.example.fragments.Config.DefaultConstants.retrofit;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.fragments.Config.ApiCall;
import com.example.fragments.Config.Meridia;
import com.example.fragments.Model.Film.Film;
import com.example.fragments.Model.Film.FilmResults;
import com.example.fragments.Model.List.TMDBList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    public BottomNavigationView bottomNav;
    public Fragment selectedFragment;

    public static byte mode; // 0 for default, 1 for "update lists", 2 for "show list"
    public static TMDBList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the favourites list before loading the app so that we can check later if a movie is in the list
        ApiCall apiCall = retrofit.create(ApiCall.class);
        // call (account, "favorite/movies", api_key, session) for favourites list
        Call<FilmResults> call = apiCall.getData(ACCOUNT_ID, "favorite/movies", API_KEY, SESSION_ID);

        call.enqueue(new Callback<FilmResults>(){
            @Override
            public void onResponse(Call<FilmResults> call, Response<FilmResults> response) {
                if (response.code()<200 || response.code()>299) /* any 2xx means success */ {
                    Log.i("testApi", "Response code: " + response.code());
                } else {
                    Log.i("request?", response.toString().split("url=")[1]);
                    ArrayList<Film> arraySearch = new ArrayList<>();
                    arraySearch = response.body().getResults();
                    Meridia.localFavs = arraySearch; // Update local list of favourites

                    // when the call is processed, then load the app

                    setContentView(R.layout.activity_main);
                    bottomNav = findViewById(R.id.main_menu);

                    if (mode == 0) getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment()).commit();
                    if (mode == 1) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ListFragment()).commit();
                        bottomNav.setSelectedItemId(R.id.nav_list);
                    }
                    if (mode == 2) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoviesListFragment(list, false)).commit();
                        bottomNav.setSelectedItemId(R.id.nav_list);
                    }


                    bottomNav.setOnItemSelectedListener(item -> {
                        selectedFragment = null;

                        switch (item.getItemId()){
                            case R.id.nav_search:
                                selectedFragment = new SearchFragment();
                                break;

                            case R.id.nav_list:
                                selectedFragment = new ListFragment();
                                break;

                            case R.id.nav_fav:
                                selectedFragment = new MoviesListFragment(null, true);
                                break;
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                        return true;
                    });

                }
            }

            @Override
            public void onFailure(Call<FilmResults> call, Throwable t) {

            }
        });
    }
}