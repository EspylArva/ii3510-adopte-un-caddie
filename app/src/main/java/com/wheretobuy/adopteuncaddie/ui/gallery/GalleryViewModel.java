package com.wheretobuy.adopteuncaddie.ui.gallery;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.wheretobuy.adopteuncaddie.MainActivity;
//import com.wheretobuy.adopteuncaddie.module.geolocation.SupermarketLocationListener;

import java.io.Serializable;

import timber.log.Timber;

public class GalleryViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    public MutableLiveData<Location> mLastLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private int FAILED_GEOLOCATION_ATTEMPTS = 0;

    public MutableLiveData<Location> getLastLocation() {
        return this.mLastLocation;
    }

    public GalleryViewModel(Application app) {
        super(app);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(app);
        this.mText = new MutableLiveData<>();

        this.mLastLocation = new MutableLiveData<>();
        this.mText.setValue("No position yet");
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressWarnings("MissingPermission")
    public void fetchLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this.getApplication().getMainExecutor(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation.postValue(task.getResult());
                            FAILED_GEOLOCATION_ATTEMPTS = 0;
                        } else {
                            if(FAILED_GEOLOCATION_ATTEMPTS < 100)
                            {
                                Timber.w(task.getException(), "getLastLocation:message");
                                FAILED_GEOLOCATION_ATTEMPTS += 1;
                                initLocation();
                            }
                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initLocation() {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(getApplication(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(getApplication()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            fetchLocation();
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}