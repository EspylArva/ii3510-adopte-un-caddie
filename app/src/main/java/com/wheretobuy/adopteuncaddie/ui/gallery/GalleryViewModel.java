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

    public MutableLiveData<Location> getLastLocation(){return this.mLastLocation;}

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
//                        Timber.d("Long: %s - Lat: %s", mLastLocation.getValue().getLongitude(), mLastLocation.getValue().getLatitude());
                    } else {
                        Timber.w(task.getException(), "getLastLocation:exception");
                    }
                }
            });
    }
    public LiveData<String> getText() {
        return mText;
    }
}