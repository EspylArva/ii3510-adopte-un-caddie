package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.wheretobuy.adopteuncaddie.R;

public class BarcodeScannerFragment extends Fragment {

    private BarcodeScannerViewModel vm;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(BarcodeScannerViewModel.class);

        View root = viewsInit(inflater, container);
        setViewModelObservers();
        setClickListeners();

        return root;
    }

    private void setClickListeners() {
//        btn_<ID_HERE>.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO: DO STUFF
//            }
//        });
    }

    private void setViewModelObservers()
    {
//        vm.<MUTABLE_LIVE_DATA_GETTER>().observe(getViewLifecycleOwner(), new Observer<TYPE_OF_ATTRIBUTE>() {
//            @Override
//            public void onChanged(@Nullable TYPE_OF_ATTRIBUTE variable) {
//                //TODO: DO STUFF
//            }
//        });
    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);


        return root;
    }
}