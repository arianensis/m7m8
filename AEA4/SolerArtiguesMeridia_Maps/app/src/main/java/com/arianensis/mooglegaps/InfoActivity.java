package com.arianensis.mooglegaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arianensis.mooglegaps.api_nativa_sunrise.API.ApiThread;
import com.arianensis.mooglegaps.api_retrofit_flickr.ApiCall;
import com.arianensis.mooglegaps.api_retrofit_flickr.Model.Photo;
import com.arianensis.mooglegaps.api_retrofit_flickr.Model.Result;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InfoActivity extends AppCompatActivity {

    public static double longi, lati;
    public static String longitudF, latitudF, address; // to save data of the clicked place
    public static ArrayList<String> urls;

    private final InfoActivity activity = this;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // Initialize fields
        TextView valueLongitud = findViewById(R.id.value_longitud);
        TextView valueLatitud = findViewById(R.id.value_latitud);
        TextView valueAddress = findViewById(R.id.value_address);

        // get the data
        valueLongitud.setText(longitudF);
        valueLatitud.setText(latitudF);
        ApiThread conn = new ApiThread(lati, longi, activity);
        conn.execute();
        valueAddress.setText(address);

        ///////// IMAGES
        // Create the retrofit builder with the base url (flickr)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.flickr.com/services/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create the call object
        ApiCall apiCall = retrofit.create(ApiCall.class);
        // do the call with the coordinates and the rest of predefined values
        Call<Result> call = apiCall.getData(
                ApiCall.METHOD,
                ApiCall.APIKEY,
                lati, longi, // the variables
                ApiCall.FORMAT,
                ApiCall.NOJSONCALLBACK,
                ApiCall.PERPAGE,
                new Random().nextInt(100)+1); // a random page number so we don't always get the same pictures

        // Initialise slider object
        ViewPager mPager = findViewById(R.id.vpager);

        call.enqueue(new Callback<Result>(){
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if(response.code()!=200){
                    Log.e("Meridia", "ERROR: Call wasn't successful");
                    return;
                }
                // if we get here, the code is 200 so the result is valid
                Log.i("Meridia", "Found " + response.body().getPhotos().getPhoto().size() + " photos");
                String list = "";
                for (Photo photo : response.body().getPhotos().getPhoto()) {
                    Log.i("Meridia", "  - " + photo.generateURL());
                    list = list + photo.generateLocalURL() + "\n";
                }
                urls = response.body().getPhotos().generateListOfURL();
                // once the pictures are found, load the adapter
                mPager.setAdapter(new SlidingAdapter(getApplicationContext(), urls));
                // if no pictures, show a text
                if (urls.size() == 0) findViewById(R.id.no_pictures).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("Meridia", "ERROR: Couldn't make the call to the API");
            }
        });

    }

}