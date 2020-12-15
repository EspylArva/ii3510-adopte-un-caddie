package com.wheretobuy.adopteuncaddie.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.components.RecyclerViewMargin;
import com.wheretobuy.adopteuncaddie.databinding.FragmentBarcodeScannerBinding;
import com.wheretobuy.adopteuncaddie.databinding.FragmentSettingsBinding;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.BarcodeScannerViewModel;
import com.wheretobuy.adopteuncaddie.ui.barcodeScanner.CaptureFragment;
import com.wheretobuy.adopteuncaddie.ui.gallery.GalleryViewModel;

import java.util.ArrayList;
import java.util.Arrays;

import timber.log.Timber;

public class SettingsFragment extends Fragment {
    private GalleryViewModel galleryViewModel;
    private TextView lbl_geolocation;
    private Button btn_refreshGeolocation;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        FragmentSettingsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        binding.setViewmodel(ViewModelProviders.of(this).get(SettingsViewModel.class));

        View root = binding.getRoot();
//        navController = NavHostFragment.findNavController(this);
//        setViewModelObservers(binding);

        initViews(binding);
        setClickListeners(binding);


        /**
         * Handle back arrow
         */
//        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                // Handle the back button event
//                Timber.d("Pressed back from Settings windows");
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);



        return root;
    }

    private void initViews(FragmentSettingsBinding binding) {
        setupRecycler(binding.recyclerShops);
        setupRecycler(binding.recyclerPayment);
        setupRecycler(binding.recyclerLanguages);
        setupRecycler(binding.recyclerGcu);

        binding.recyclerShops.setAdapter(new PaymentAdapter(new ArrayList<String>(Arrays.asList("Auchan", "Carrefour", "Leclerc"))));
        binding.recyclerPayment.setAdapter(new PaymentAdapter(new ArrayList<String>(Arrays.asList("1234-5678-ABCD", "1234-5678-EFGH", "1234-5678-IJKL"))));
        binding.recyclerLanguages.setAdapter(new PaymentAdapter(new ArrayList<String>(Arrays.asList("Français", "English", "Tching tchong le chinois"))));
        binding.recyclerGcu.setAdapter(new PaymentAdapter(new ArrayList<String>(Arrays.asList("Art. 1", "Art. 2", "Art. 3", "Art. 4"))));
    }

    private void setupRecycler(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerViewMargin decoration = new RecyclerViewMargin(4, 1);
        recyclerView.addItemDecoration(decoration);
    }

    public void setClickListeners(FragmentSettingsBinding binding)
    {
        binding.lblFavoriteShops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerShops;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
        binding.lblPaymentList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerPayment;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
        binding.lblLanguages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerLanguages;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
        binding.lblGcu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View lbl) {
                RecyclerView v = binding.recyclerGcu;
                if(v.getVisibility() == View.VISIBLE) { v.setVisibility(View.GONE); }
                else if(v.getVisibility() == View.GONE) { v.setVisibility(View.VISIBLE); }
            }
        });
    }

}
