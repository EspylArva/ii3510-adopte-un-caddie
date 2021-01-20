package com.wheretobuy.adopteuncaddie.ui.gallery;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.wheretobuy.adopteuncaddie.R;

import timber.log.Timber;

public class GalleryFragment extends Fragment {



    // ERR: For testing purpose. Should not appear in release version.

    private GalleryViewModel galleryViewModel;
    private TextView lbl_geolocation;
    private Button btn_refreshGeolocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);

        View root = viewsInit(inflater, container);
        setViewModelObservers();
        setClickListeners();

        return root;
    }


    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Timber.i("Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    getView(),
                    "Location permission is needed for core functionality",
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    34);
                        }
                    })
                    .show();
        } else {
            Timber.i("Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    34);
        }
    }


    private void setClickListeners() {
        btn_refreshGeolocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkPermissions()) {
                    requestPermissions();
                } else { // add null checker
                    Timber.d("Requesting position");
                    // Permission granted. Now check if module is activated
                    // Checking if GPS module is activated
                    LocationRequest locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                    Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity()).checkLocationSettings(builder.build());

                    result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.P)
                        @Override
                        public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                            try {
                                LocationSettingsResponse response = task.getResult(ApiException.class);
//                                GeolocationService serv = new GeolocationService(getActivity());
//                                serv.getLastLocation();
                                galleryViewModel.fetchLocation();
                            }
                            catch (ApiException exception) {
                                if(exception.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED){
                                        try {
                                            // Cast to a resolvable exception.
                                            ResolvableApiException resolvable = (ResolvableApiException) exception;
                                            // Show the dialog by calling startResolutionForResult(),
                                            // and check the result in onActivityResult().
                                            resolvable.startResolutionForResult(
                                                    getActivity(),
                                                    LocationRequest.PRIORITY_HIGH_ACCURACY);
                                        } catch (Exception e) {
                                            // Ignore the error.
                                        }
                                }
                            }
                        }
                    });


                }
            }
        });
    }

    private void setViewModelObservers() {
        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lbl_geolocation.setText(s);
            }
        });

        galleryViewModel.getLastLocation().observe(getViewLifecycleOwner(), new Observer<Location>()
        {
            @Override
            public void onChanged(Location location) {
                if(location != null){
                    lbl_geolocation.setText(String.format("Lat: %s - Lng: %s", location.getLatitude(), location.getLongitude()));
                }
            }
        });
    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        lbl_geolocation = root.findViewById(R.id.lbl_geolocation);
        btn_refreshGeolocation = root.findViewById(R.id.btn_callGeo);
        return root;
    }
}