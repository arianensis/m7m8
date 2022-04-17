package com.example.fragments.Config;

import com.example.fragments.Model.Film.Film;
import com.example.fragments.Model.List.TMDBList;

import java.util.ArrayList;

public class Meridia {
    // manage favourites
    public static ArrayList<Film> localFavs;
    // checks if a given movie is in favourites list
    public static boolean isFavourite(Film toCheck) {

        // check all movies in the list and see if the id matches the argument film
        for (Film inList : localFavs) {
            if (inList.getId() == toCheck.getId()) return true;
        }
        return false;
    }
    // add or remove from favourites
    public static void setFavourite(Film film, boolean favourite) {
        if (favourite) localFavs.add(film);
        else localFavs.remove(film);
    }

    public static Film currentlyViewing;
}
