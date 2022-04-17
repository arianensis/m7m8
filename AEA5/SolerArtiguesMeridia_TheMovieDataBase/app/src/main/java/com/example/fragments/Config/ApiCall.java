package com.example.fragments.Config;

import com.example.fragments.Model.Film.FavFilmRequest;
import com.example.fragments.Model.Film.FavFilmResponse;
import com.example.fragments.Model.Film.FilmListResults;
import com.example.fragments.Model.Film.FilmResults;
import com.example.fragments.Model.List.AddToListRequest;
import com.example.fragments.Model.List.EmptyResponse;
import com.example.fragments.Model.List.ListRequest;
import com.example.fragments.Model.List.ListResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Query;
import retrofit2.http.Path;
import retrofit2.http.Body;

public interface ApiCall {

    // search movies
    @GET("search/movie?")
    Call<FilmResults> getData(@Query("api_key") String api_key, @Query("query") String query);

    // get account info
    @GET("account/{account_id}/{option}")
    Call<FilmResults> getData(@Path("account_id") String account_id, @Path("option") String option,
                              @Query("api_key") String api_key, @Query("session_id") String query);

    // get lists
    @GET("account/{account_id}/lists")
    Call<ListResults> getData(@Path("account_id") String account_id, @Query("api_key") String api_key, @Query("session_id") String query);

    // set movie as favourite
    @POST("account/{account_id}/favorite")
    Call<FavFilmResponse> getData(@Path("account_id") String account_id, @Query("api_key") String api_key, @Query("session_id") String query,
                                  @Body FavFilmRequest request);

    // add list
    @POST("list")
    Call<ListResults> getData(@Query("api_key") String api_key, @Query("session_id") String session_id,
                              @Body ListRequest newListRequest);

    // get list contents
    @GET("list/{list_id}")
    Call<FilmListResults> getData(@Path("list_id") int listId, @Query("api_key") String apiKey);

    // add movie to list
    @POST("list/{list_id}/add_item")
    Call<EmptyResponse> getData(@Path("list_id") int listId, @Query("api_key") String apiKey, @Query("session_id") String query,
                                @Body AddToListRequest addRequest);

    // destroy list
    @DELETE("list/{list_id}")
    Call<EmptyResponse> getData(@Path("list_id") int listId, @Query("api_key") String apiKey, @Query("session_id") String query);

}
