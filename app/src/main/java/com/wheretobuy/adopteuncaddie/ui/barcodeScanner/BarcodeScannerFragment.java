package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.wheretobuy.adopteuncaddie.R;

import org.w3c.dom.Text;

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
//        vm.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//        @Override
//        public void onChanged(@Nullable String variable) {
        //TODO: DO STUFF
//        }
//    });

    }

    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        return root;
    }
}