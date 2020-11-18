package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.annotation.SuppressLint;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.vision.barcode.Barcode;
import com.wheretobuy.adopteuncaddie.R;

import java.util.List;

public class BarcodeScannerFragment extends Fragment implements CaptureFragment.BarcodeReaderListener {

    private static boolean REQUIRE_CONFIRMATION = true;
    private BarcodeScannerViewModel vm;

    private CaptureFragment barcodeReader;

    private static final String TAG = "Barcode-reader";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        vm = ViewModelProviders.of(this).get(BarcodeScannerViewModel.class);

        View root = viewsInit(inflater, container);
        setViewModelObservers();
        setClickListeners();

        return root;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onScanned(final Barcode barcode) {
        Log.d(TAG, "onScanned: " + barcode.displayValue + " (format: " + barcode.valueFormat + ")");
        try {
            barcodeReader.playBeep();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();

        if(BarcodeScannerFragment.REQUIRE_CONFIRMATION)
        {
            try {
                NavController navController = NavHostFragment.findNavController(this);
                navController.navigate(R.id.action_nav_barcodeScanner_to_nav_productScanned);
            }
            catch (Exception e){e.printStackTrace();}
        }

        // FIXME

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