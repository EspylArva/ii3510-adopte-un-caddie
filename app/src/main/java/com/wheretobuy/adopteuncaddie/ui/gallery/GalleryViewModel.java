package com.wheretobuy.adopteuncaddie.ui.gallery;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wheretobuy.adopteuncaddie.module.geolocation.SupermarketLocationListener;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    public GalleryViewModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public String getGeoLocation() {
        LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new SupermarketLocationListener();
        try
        {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
        catch (SecurityException e) { e.printStackTrace(); }

        return null;
    }


    public LiveData<String> getText() {
        return mText;
    }
}