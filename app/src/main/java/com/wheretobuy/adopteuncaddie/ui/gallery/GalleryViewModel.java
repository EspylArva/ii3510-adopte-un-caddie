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

import com.wheretobuy.adopteuncaddie.MainActivity;
import com.wheretobuy.adopteuncaddie.module.geolocation.SupermarketLocationListener;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;
    private Application context;
    private MutableLiveData<Location> location;
    private MutableLiveData<Double> latitude, longitude;
    public SupermarketLocationListener gps;


    public GalleryViewModel(Application app) {
        super(app);
        this.context = app;
        this.mText = new MutableLiveData<>();
        this.location = new MutableLiveData<>();
        this.latitude = new MutableLiveData<>();
        this.longitude = new MutableLiveData<>();
        this.mText.setValue("No position yet");
    }

    public void getGeoLocation() {
        Location location = null;
        if(ContextCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this.context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            Log.e("Permissions", "Neither GPS nor Internet permissions were granted");
        } // No permissions
        else
        {
            gps = new SupermarketLocationListener(this.context);
            location = gps.refreshLocation();
            this.location.setValue(location);

            if(location != null){
                Toast.makeText(this.context, "Your Location is: Lat: " + location.getLatitude() + " - Long: " + location.getLongitude(), Toast.LENGTH_LONG).show();
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