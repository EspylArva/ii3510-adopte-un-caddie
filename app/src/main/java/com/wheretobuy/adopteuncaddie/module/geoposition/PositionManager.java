//package com.wheretobuy.adopteuncaddie.module.geoposition;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.Build;
//import android.view.View;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//import androidx.lifecycle.MutableLiveData;
//
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.api.ResolvableApiException;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResponse;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.material.snackbar.Snackbar;
//import com.wheretobuy.adopteuncaddie.R;
//
//import timber.log.Timber;
//
//public class PositionManager {
//
//    private static FusedLocationProviderClient mFusedLocationClient;
//    public static MutableLiveData<Location> mLastLocation;
//    private static int FAILED_GEOLOCATION_ATTEMPTS = 0;
//
//    public static void getUserPosition()
//    {
//        if (!checkPermissions()) {
//            requestPermissions();
//        } else { // add null checker
//            Timber.d("Requesting position");
//            // Permission granted. Now check if module is activated
//            // Checking if GPS module is activated
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
//            Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());
//
//            result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
//                @RequiresApi(api = Build.VERSION_CODES.P)
//                @Override
//                public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
//                    try {
//                        LocationSettingsResponse response = task.getResult(ApiException.class);
////                                GeolocationService serv = new GeolocationService(getActivity());
////                                serv.getLastLocation();
//                        fetchLocation();
//                    }
//                    catch (ApiException exception) {
//                        if(exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
//                            try {
//                                // Cast to a resolvable exception.
//                                ResolvableApiException resolvable = (ResolvableApiException) exception;
//                                // Show the dialog by calling startResolutionForResult(),
//                                // and check the result in onActivityResult().
//                                resolvable.startResolutionForResult(
//                                        getActivity(),
//                                        LocationRequest.PRIORITY_HIGH_ACCURACY);
//                            } catch (Exception e) {
//                                // Ignore the error.
//                            }
//                        }
//                    }
//                }
//            });
//
//
//        }
//    }
//
//    private static boolean checkPermissions() {
//        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION);
//    }
//
//    private static void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Timber.i("Displaying permission rationale to provide additional context.");
//            Snackbar.make(
//                    getView(),
//                    "Location permission is needed for core functionality",
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(getActivity(),
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    34);
//                        }
//                    })
//                    .show();
//        } else {
//            Timber.i("Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    34);
//        }
//    }
//
//    /**
//     * Provides a simple way of getting a device's location and is well suited for
//     * applications that do not require a fine-grained location and that do not need location
//     * updates. Gets the best and most recent location currently available, which may be null
//     * in rare cases when a location is not available.
//     * <p>
//     * Note: this method should be called after location permission has been granted.
//     */
//    @RequiresApi(api = Build.VERSION_CODES.P)
//    @SuppressWarnings("MissingPermission")
//    public static void fetchLocation() {
//        mFusedLocationClient.getLastLocation()
//                .addOnCompleteListener(getApplication().getMainExecutor(), new OnCompleteListener<Location>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Location> task) {
//                        if (task.isSuccessful() && task.getResult() != null) {
//                            mLastLocation.postValue(task.getResult());
//                            FAILED_GEOLOCATION_ATTEMPTS = 0;
//                        } else {
//                            if(FAILED_GEOLOCATION_ATTEMPTS < 100)
//                            {
//                                Timber.w(task.getException(), "getLastLocation:message");
//                                FAILED_GEOLOCATION_ATTEMPTS += 1;
//                                initLocation();
//                            }
//                        }
//                    }
//                });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.P)
//    private static void initLocation() {
//        LocationRequest mLocationRequest = LocationRequest.create();
//        mLocationRequest.setInterval(60000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        LocationCallback mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    if (location != null) {
//                        //TODO: UI updates.
//                    }
//                }
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.getFusedLocationProviderClient(getApplication()).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
//            fetchLocation();
//        }
//    }
//
//}
