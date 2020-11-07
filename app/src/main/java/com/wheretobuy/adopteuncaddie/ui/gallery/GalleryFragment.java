package com.wheretobuy.adopteuncaddie.ui.gallery;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wheretobuy.adopteuncaddie.MainActivity;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.module.geolocation.SupermarketLocationListener;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        final TextView textView = root.findViewById(R.id.text_gallery);
        final Button btn_geo = root.findViewById(R.id.btn_callGeo);

        btn_geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryViewModel.getGeoLocation();
            }
        });

        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        galleryViewModel.getLatitude().observe(getViewLifecycleOwner(), new Observer<Double>()
        {
            @Override
            public void onChanged(Double location) {
                if(location == null)
                {
//                    galleryViewModel.gps.showSettingsAlert((MainActivity)getActivity());
                    ActivityCompat.requestPermissions((MainActivity)getActivity(),new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET },1);
                }
                else
                {
                    textView.setText(String.format("Lat: %s - Lng: %s", galleryViewModel.getLatitude().getValue(), galleryViewModel.getLongitude().getValue()));
                }
            }
        });

        galleryViewModel.getLongitude().observe(getViewLifecycleOwner(), new Observer<Double>()
        {
            @Override
            public void onChanged(Double location) {
                if(location == null)
                {
//                    galleryViewModel.gps.showSettingsAlert((MainActivity)getActivity());
//                    ActivityCompat.requestPermissions(
//                            (MainActivity)getActivity(),
//                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET },
//                            1);
                }
                else
                {
                    textView.setText(String.format("Lat: %s - Lng: %s", galleryViewModel.getLatitude().getValue(), galleryViewModel.getLongitude().getValue()));
                }
            }
        });

        return root;

    }

    public String getGeoLocation() {
        try{
//            SupermarketLocationListener gps = new SupermarketLocationListener((MainActivity)this.getActivity());
//            Location location = gps.getLocation();
//            if(location != null){
//                Toast.makeText(getContext(), "Your Location is: Lat: " + location.getLatitude() + " - Long: " + location.getLongitude(), Toast.LENGTH_LONG).show();
//            }
//            else {
//                Log.e("Geolocation", "Could not fetch position. Location is null");
//                gps.showSettingsAlert();
//            }

        }
        catch (SecurityException e) { e.printStackTrace(); }


        return null;
    }
}