package com.arianensis.mooglegaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.RestrictionsManager;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.arianensis.mooglegaps.databinding.ActivityMapsBinding;

import java.util.List;
import java.util.Locale;

/**
 * This demo shows how GMS Location can be used to check for changes to the users location.  The
 * "My Location" button uses GMS Location to set the blue dot representing the users location.
 * Permission for {@link android.Manifest.permission#ACCESS_FINE_LOCATION} is requested at run
 * time. If the permission has not been granted, the Activity is finished with an error message.
 */
public class MapsActivity extends AppCompatActivity
        implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;

    private GoogleMap mMap;
    private int decimalPositions = 4;
    private final MapsActivity activity = this;
    Marker mapMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.arianensis.mooglegaps.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.5,2.3), 10));

        // Listener for when the map is clicked
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                Toast.makeText(getApplicationContext(), "Map clicked at" + coordinates(latLng) + "\nClick marker to see info", Toast.LENGTH_LONG).show();
                Log.i("Meridia", "Map clicked at"+coordinates(latLng));

                // remove previous marker and create new one
                if (mapMarker != null) mapMarker.remove();
                mapMarker = mMap.addMarker(new MarkerOptions().position(latLng));
                // save raw coordinates for later use
                InfoActivity.lati = latLng.latitude;
                InfoActivity.longi = latLng.longitude;
                // get and save address info
                InfoActivity.address = getAddress(latLng.latitude, latLng.longitude);

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        // open info activity
                        Intent showInfo = new Intent (getApplicationContext(), InfoActivity.class);
                        startActivity(showInfo);
                        return false;
                    }
                });
            }
        });
        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        // [START maps_check_location_permission]
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
        // [END maps_check_location_permission]
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Going to location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    // [START maps_check_location_permission_result]
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // [START_EXCLUDE]
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
            // [END_EXCLUDE]
        }
    }
    // [END maps_check_location_permission_result]

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            permissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    public String coordinates(LatLng latLng) {
        double lati = Math.round(latLng.latitude*Math.pow(10, decimalPositions))/Math.pow(10, decimalPositions);
        double longi = Math.round(latLng.longitude*Math.pow(10, decimalPositions))/Math.pow(10, decimalPositions);

        // format the coordinates
        InfoActivity.latitudF = ""+ Math.abs(lati) + "º" + (lati >= 0 ? "N" : "S");
        InfoActivity.longitudF = "" + Math.abs(longi) + "º" + (longi >= 0 ? "E" : "W");

        return "\n -"+InfoActivity.latitudF + "\n -"+InfoActivity.longitudF;
    }

    public String getAddress(double lat, double lng) {
        String message;
        try {
            // the Geocoder class uses coordinates to search information of a place (for example street number)
            // or a text (for example an address) to search the location in the map
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault()); // sets the language and the context

            // the method can get various locations in an address, so we can limit how many it gets (in this case 1)
            List<Address> addresses = geo.getFromLocation(lat, lng, 1);
            if (addresses.isEmpty()) {
                message = "No info on the clicked coordinates";
            } else {
                // if the list is not empty, it will have just one item, so we display the information from this item
                Address a = addresses.get(0);
                message = "Address found: " + a.getAddressLine(0);
            }
        }
        catch(Exception e){
            message = "No Location Name Found";
        }

        Log.i("Meridia",  message);
        return message;
    }

}