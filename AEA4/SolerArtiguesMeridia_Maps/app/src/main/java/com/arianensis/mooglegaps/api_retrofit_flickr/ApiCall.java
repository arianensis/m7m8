package com.arianensis.mooglegaps.api_retrofit_flickr;

import com.arianensis.mooglegaps.api_retrofit_flickr.Model.Photos;
import com.arianensis.mooglegaps.api_retrofit_flickr.Model.Result;

import java.util.Random;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {

    // these values don't change
    public String METHOD = "flickr.photos.search";
    public String APIKEY = "79d466885188b99d6762980d64029892";
    public String FORMAT = "json";
    public int NOJSONCALLBACK = 1;
    public int PERPAGE = 5;

    // the call takes the latitude and the longitude from the Activity and the rest of the values from this file
    @GET("?method=aaa&api_key=aaa&lat=1.0&lon=1.0&format=aaa&nojsoncallback=1&per_page=1")
    Call<Result> getData(
            @Query("method") String method,
            @Query("api_key") String apiKey,
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("format") String format,
            @Query("nojsoncallback") int noJsonCallback,
            @Query("per_page") int perPage,
            @Query("page") int page);

}
