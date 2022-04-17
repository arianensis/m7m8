package com.example.fragments;

import static com.example.fragments.Config.DefaultConstants.API_KEY;
import static com.example.fragments.Config.DefaultConstants.retrofit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fragments.Config.ApiCall;
import com.example.fragments.Config.Meridia;
import com.example.fragments.Model.Film.Film;
import com.example.fragments.Model.Film.FilmListResults;
import com.example.fragments.Model.Film.FilmResults;
import com.example.fragments.Model.List.TMDBList;
import com.example.fragments.Recyclers.SearchMovieRecyclerViewAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesListFragment extends Fragment {

    TMDBList list;
    boolean isFavourites;

    RecyclerView recyclerView; // add recycler view reference

    public MoviesListFragment() {
        // Required empty public constructor
    }

    public MoviesListFragment(TMDBList clicked, boolean isFavourites) {
        this.list = clicked;
        this.isFavourites = isFavourites;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);

        TextView txtSectionTitle = view.findViewById(R.id.sectionTitle);
        txtSectionTitle.setText(list!=null ? list.getName() : "Favourite movies");

        // we use the same recycler view element that is used for a regular search
        recyclerView = view.findViewById(R.id.recyclerSearch);

        if (isFavourites) {
            // if trying to access the favourites list, just load the local list because it will already be updated
            callRecycler(Meridia.localFavs);
        } else {
            // otherwise we will have to do an API call to get the correct list
            ApiCall apiCall = retrofit.create(ApiCall.class);
            // call (listId, api_key) for list contents
            Call<FilmListResults> call = apiCall.getData(list.getId(), API_KEY);
            call.enqueue(new Callback<FilmListResults>() {

                @Override
                public void onResponse(Call<FilmListResults> call, Response<FilmListResults> response) {
                    if (response.code() < 200 || response.code() > 299) /* any 2xx means success */ {
                        Log.i("testApi", "Response code: " + response.code());
                    } else {
                        ArrayList<Film> arraySearch = new ArrayList<>();
                        arraySearch = response.body().getItems();
                        callRecycler(arraySearch);
                    }
                }

                @Override
                public void onFailure(Call<FilmListResults> call, Throwable t) {

                }
            });
        }

        return view;
    }

    public void callRecycler(ArrayList<Film> arraySearch){
        SearchMovieRecyclerViewAdapter adapter = new SearchMovieRecyclerViewAdapter(arraySearch, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setAdapter(adapter);
    }
}