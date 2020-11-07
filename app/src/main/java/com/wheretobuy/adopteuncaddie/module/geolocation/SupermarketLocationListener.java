package com.wheretobuy.adopteuncaddie.module.geolocation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SupermarketLocationListener implements LocationListener {

    private final Context context;
    // Flags for GPS and Network status
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
//    private boolean canGetLocation = false;

    // Location
    private Location location;
    private double latitude, longitude;

    protected LocationManager locationManager;

    public SupermarketLocationListener(Context application) {
        this.context = application;
    }


    /** ACQUISITION **/

    // TODO

    public Location refreshLocation()
    {
        System.out.println(context);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        System.out.println("" + (locationManager == null));
        // Setting status flags
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) { /*ERR: Cannot access position. Neither GPS nor Network are authorized by user */
            Log.e("Location getter", "Cannot access position. Neither GPS nor Network are authorized by user");}
        else{
            Log.d("Location getter", String.format("Seems like we should be able to get a position! GPS enabled: %s - Network enabled: %s", isGPSEnabled, isNetworkEnabled ));
            try{
                // TODO: we might want to switch GPS first / network second to network first / GPS second?
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, this);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            Log.d("GPS Enabled", "Fetched position: " + location.getLatitude() + " - " + location.getLongitude());
                            refreshPosition(location);
                        } // refreshing the positions so we can pull
                        else {
                            Log.e("GPS failure", "Could not get a valid location using GPS");
                        }
                    }
                } // Checking with GPS first
                else if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,10, this);
                    if (locationManager != null) {
                        Log.d("Using Network", "Using Network");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) { refreshPosition(location); } // refreshing the positions so we can pull
                    }
                } // Checking with network if we can't access GPS
            } // for security purposes
            catch (SecurityException e) { e.printStackTrace(); }
        } // We can get location

        stopUsingGPS(); // We only want to pull position once
        return location;
    }

    private void refreshPosition(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    public void showSettingsAlert(final Activity activity){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    /** GETTER & SETTERS **/

    public double getLatitude(){
        if(location != null){ latitude = location.getLatitude(); }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){ longitude = location.getLongitude(); }
        return longitude;
    }

//    public boolean canGetLocation() {
//        return this.canGetLocation;
//    }



    /** CLASS OVERRIDES **/

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)
        {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
