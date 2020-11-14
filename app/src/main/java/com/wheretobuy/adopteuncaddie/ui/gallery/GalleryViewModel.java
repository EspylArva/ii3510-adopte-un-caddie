package com.wheretobuy.adopteuncaddie.ui.gallery;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wheretobuy.adopteuncaddie.MainActivity;
import com.wheretobuy.adopteuncaddie.module.geolocation.SupermarketLocationListener;

import java.io.Serializable;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<Location> location;
    private MutableLiveData<Double> latitude, longitude;
    public SupermarketLocationListener gps;
    private FusedLocationProviderClient client;


    public GalleryViewModel(Application app) {
        super(app);
        this.client = LocationServices.getFusedLocationProviderClient(app);
        this.mText = new MutableLiveData<>();
        this.location = new MutableLiveData<>();
        this.latitude = new MutableLiveData<>();
        this.longitude = new MutableLiveData<>();
        this.mText.setValue("No position yet");
    }

    public void getGeoLocation() {
        Location location = null;

        if(ContextCompat.checkSelfPermission(this.getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.getApplication(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            Log.e("Permissions", "Neither GPS nor Internet permissions were granted");
        } // No permissions
        else
        {
//            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location currentLocation) {
//                    location[0] = currentLocation;
//                }
//            });
            gps = new SupermarketLocationListener(this.getApplication());
            location = gps.refreshLocation();
            this.location.setValue(location);
//
            if(location != null){
                Toast.makeText(this.getApplication(), "Your Location is: Lat: " + location.getLatitude() + " - Long: " + location.getLongitude(), Toast.LENGTH_LONG).show();
                this.latitude.setValue(location.getLatitude());
                this.longitude.setValue(location.getLongitude());
            }
            else {
                Log.e("Geolocation", "Could not fetch position. Location is null");
                this.latitude.setValue(null);
                this.longitude.setValue(null);
            }
        }
    }

    public MutableLiveData<Double> getLatitude()
    {
        return this.latitude;
    }
    public MutableLiveData<Double> getLongitude()
    {
        return this.longitude;
    }
    public LiveData<String> getText() {
        return mText;
    }
}