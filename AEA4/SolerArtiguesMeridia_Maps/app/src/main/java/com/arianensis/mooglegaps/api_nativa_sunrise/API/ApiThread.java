package com.arianensis.mooglegaps.api_nativa_sunrise.API;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.arianensis.mooglegaps.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiThread extends AsyncTask<Void,Void,String> {

    private double lati, longi;
    private Activity activity;
    // Constructor
    public ApiThread(double lati, double longi, Activity activity) {
        this.lati = lati;
        this.longi = longi;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute()
    {

    }

    @Override
    protected String doInBackground(Void... values){

        URL url = null;
        try {
            // construct the url with the data
            url = new URL("https://api.sunrise-sunset.org/json?lat="+lati+"&lng="+longi);

            // open connection
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            // Read API results
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Result is a single line
            String data = bufferedReader.readLine();

            // Close
            bufferedReader.close();

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String data){
        // The string in the argument is the JSON object
        try {
            JSONObject json = new JSONObject(data);
            JSONObject result = json.getJSONObject("results");
            // get the sunrise and sunset times
            String sunrise = result.getString("sunrise") + " UTC";
            String sunset = result.getString("sunset") + " UTC";
            Log.i("LOG","Sun rises at " + sunrise + " and sets at " + sunset);
            // modify the fields in the info activity
            TextView valueSunrise = activity.findViewById(R.id.value_sunrise);
            TextView valueSunset = activity.findViewById(R.id.value_sunset);
            valueSunrise.setText(sunrise);
            valueSunset.setText(sunset);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}