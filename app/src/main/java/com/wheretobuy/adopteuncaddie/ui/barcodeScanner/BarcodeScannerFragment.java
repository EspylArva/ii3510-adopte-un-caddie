package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.vision.barcode.Barcode;
import com.wheretobuy.adopteuncaddie.R;

import java.util.List;

public class BarcodeScannerFragment extends Fragment implements CaptureFragment.BarcodeReaderListener {

    private BarcodeScannerViewModel vm;

    private CaptureFragment barcodeReader;
    private Guideline guideline_horizontal, guideline_vertical;

    public static int SCREEN_WIDTH_PX;
    public static int SCREEN_HEIGHT_PX;


    private static final String TAG = "Barcode-reader";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(BarcodeScannerViewModel.class);

        View root = viewsInit(inflater, container);
        setViewModelObservers();
        setClickListeners();

        return root;
    }

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.e(TAG, "onScannedMultiple: " + barcodes.size());

        String codes = "";
        for (Barcode barcode : barcodes) {
            codes += barcode.displayValue + ", ";
        }

        final String finalCodes = codes;
        Toast.makeText(getActivity(), "Barcodes: " + finalCodes, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(TAG, "onScanError: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }


    private View viewsInit(LayoutInflater inflater, ViewGroup container) {
        View root = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        guideline_horizontal = root.findViewById(R.id.horizontalLine);
        guideline_vertical = root.findViewById(R.id.verticalLine);

        SCREEN_HEIGHT_PX = guideline_vertical.getTop();
        SCREEN_WIDTH_PX = guideline_horizontal.getLeft();

        barcodeReader = (CaptureFragment) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);


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

}