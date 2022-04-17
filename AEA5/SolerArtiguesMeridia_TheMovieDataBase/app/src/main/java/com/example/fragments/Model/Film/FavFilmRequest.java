package com.example.fragments.Model.Film;

import java.io.Serializable;

public class FavFilmRequest implements Serializable {
    public String media_type = "movie";
    public int media_id;
    public boolean favorite;

    public FavFilmRequest(Film film, boolean setToFav) {
        this.favorite = setToFav;
        this.media_id = film.getId();
    }
}
