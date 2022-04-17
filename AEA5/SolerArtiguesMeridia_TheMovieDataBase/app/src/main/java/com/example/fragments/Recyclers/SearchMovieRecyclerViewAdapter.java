package com.example.fragments.Recyclers;


import static com.example.fragments.Config.DefaultConstants.BASE_IMG_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragments.Config.GlideApp;
import com.example.fragments.DetailFragment;
import com.example.fragments.Model.Film.Film;
import com.example.fragments.R;

import java.util.ArrayList;

public class SearchMovieRecyclerViewAdapter extends RecyclerView.Adapter<SearchMovieRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Film> arrayMovies;
    private Context context;

    public SearchMovieRecyclerViewAdapter(ArrayList<Film> arrN, Context c){
        this.arrayMovies = arrN;
        this.context = c;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {

        holder.titleMovie.setText(arrayMovies.get(i).getOriginal_title());
        String imageURL = BASE_IMG_URL + arrayMovies.get(i).getPoster_path();
        GlideApp.with(context)
                    .load(imageURL)
                    .centerCrop()
                    .into(holder.imgMovie);


        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                Film film = arrayMovies.get(i);
                bundle.putSerializable("Film", film);

                AppCompatActivity app = (AppCompatActivity) view.getContext();

                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);

                app.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).commit();

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayMovies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgMovie;
        TextView titleMovie;
        ImageView btnFav;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMovie = itemView.findViewById(R.id.imgMovie);
            titleMovie = itemView.findViewById(R.id.titleMovie);
            cardview = itemView.findViewById(R.id.cardview);
        }

    }
}

