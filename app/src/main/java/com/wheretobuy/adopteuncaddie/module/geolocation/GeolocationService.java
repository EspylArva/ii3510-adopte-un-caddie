//package com.wheretobuy.adopteuncaddie.module.geolocation;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//
//import androidx.annotation.NonNull;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.wheretobuy.adopteuncaddie.R;
//
//import timber.log.Timber;
//
//public class GeolocationService {
//
//    public GeolocationService(Activity c)
//    {
//        this.activity = c;
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity);
//    }
//
//    private Activity activity;
//    public Location mLastLocation;
//    private FusedLocationProviderClient mFusedLocationClient;
//
//    /**
//     * Provides a simple way of getting a device's location and is well suited for
//     * applications that do not require a fine-grained location and that do not need location
//     * updates. Gets the best and most recent location currently available, which may be null
//     * in rare cases when a location is not available.
//     * <p>
//     * Note: this method should be called after location permission has been granted.
//     */
//    @SuppressWarnings("MissingPermission")
//    public void getLastLocation() {
//        mFusedLocationClient.getLastLocation()
//            .addOnCompleteListener(this.activity, new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        mLastLocation = task.getResult();
//                        Timber.d("Long: %s - Lat: %s", mLastLocation.getLongitude(), mLastLocation.getLatitude());
//                    } else {
//                        Timber.w(task.getException(), "getLastLocation:exception");
//                    }
//                }
//            });
//    }
//
//
//}
