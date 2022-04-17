package com.example.fragments.Model.Film;

import java.io.Serializable;

public class Film implements Serializable {
    public String original_title;
    public double vote_average;
    public String poster_path;
    public int id;
    public String overview;
    public String release_date;

    public String getOriginal_title() {
        return original_title;
    }

    public double getVote_average() {
        return vote_average;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public int getId() {
        return id;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }
}