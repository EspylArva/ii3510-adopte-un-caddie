package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.vision.barcode.Barcode;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.databinding.FragmentBarcodeScannerBinding;
import com.wheretobuy.adopteuncaddie.module.openfoodfacts.RetrofitCall;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

public class BarcodeScannerFragment extends Fragment implements CaptureFragment.BarcodeReaderListener {

    private static boolean REQUIRE_CONFIRMATION = true;
    private static final List<Integer> ACCEPTED_BARCODE_FORMATS = new ArrayList<Integer>(Arrays.asList(1, 2, 4, 8, 32, 64, 128, 512, 1024, 5));
//    private BarcodeScannerViewModel vm;

    NavController navController;


    private CaptureFragment barcodeReader;
//    private TextView txt_manualBarcode;

    private static final String TAG = "Barcode-reader";
    private String beepSoundFile;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FragmentBarcodeScannerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_barcode_scanner, container, false);
        binding.setViewmodel(ViewModelProviders.of(this).get(BarcodeScannerViewModel.class));

        View root = binding.getRoot();

        navController = NavHostFragment.findNavController(this);

//        barcodeReader = (CaptureFragment)binding.barcodeFragment;
//        txt_manualBarcode = binding.txtManualBarcode;

        barcodeReader = (CaptureFragment) getChildFragmentManager().findFragmentById(R.id.barcode_fragment);
        barcodeReader.setListener(this);
        setClickListeners(binding);



        return root;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onScanned(final Barcode barcode) {
        Timber.d("onScanned: %s (format: %s)", barcode.displayValue, barcode.format);
        if(ACCEPTED_BARCODE_FORMATS.contains(barcode.format))
        {
            Timber.d("Barcode: %s", barcode.displayValue);
            playBeep();
            processBarcode(barcode.displayValue);
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Timber.d("onScannedMultiple: %s", barcodes.size());
        for(Barcode barcode : barcodes)
        {
            onScanned(barcode);
        }
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Timber.e("onScanError: %s", errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setClickListeners(FragmentBarcodeScannerBinding binding) {
        binding.btnReturnToBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(navController.getCurrentDestination().getId() == R.id.nav_barcodeScanner)
                {
                    NavDirections action = BarcodeScannerFragmentDirections.returnNavBarcodeScannerToNavBasket();
                    navController.navigate(action);
                } // else: a navRequest has already been posted, we're just waiting for the transition.
                // Avoid the following code from being ran twice, as the fragment has technically already been changed
            }
        });

        // Remove the caret bar & try to get the product page
        binding.txtManualBarcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Timber.d(binding.getViewmodel().getBarcode());
                processBarcode(binding.getViewmodel().getBarcode());
                binding.txtManualBarcode.setFocusable(false);
                return false;
            }
        });
        // Get the caret bar back
        binding.txtManualBarcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                binding.txtManualBarcode.setFocusableInTouchMode(true);
                return false;
            }
        });
    }

    private void processBarcode(String barcode)
    {
        RetrofitCall.callProductById(productState -> {
            if(productState.getStatus() == 1) // status_verbose: product found
            {
                if(navController.getCurrentDestination().getId() == R.id.nav_barcodeScanner)
                {
                    if(BarcodeScannerFragment.REQUIRE_CONFIRMATION) // user setting: need confirmation to add to basket. Redirect to ProductScannedFragment
                    {
                        BarcodeScannerFragmentDirections.ActionNavBarcodeScannerToNavProductScanned action = BarcodeScannerFragmentDirections.actionNavBarcodeScannerToNavProductScanned(productState);
                        navController.navigate(action);
                    }
                    else
                    {
                        // Add 1 element of product to basket
                        // TODO: Add 1 to basket static list of products
                        BarcodeScannerFragmentDirections.ActionNavBarcodeScannerToNavProductScanned action = BarcodeScannerFragmentDirections.actionNavBarcodeScannerToNavProductScanned(productState);
                        navController.navigate(action);
                    }
                } // else: a navRequest has already been posted, we're just waiting for the transition.
                // Avoid the following code from being ran twice, as the fragment has technically already been changed
            }
            else
            {
                Toast.makeText(getContext(), getResources().getText(R.string.productNotFound).toString(), Toast.LENGTH_SHORT).show();
            }// else: (status == 0) -> status_verbose: product not found
        }, barcode);

    }

    public void playBeep() {
        MediaPlayer m = new MediaPlayer();
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }


            AssetFileDescriptor descriptor = getActivity().getAssets().openFd(beepSoundFile != null ? beepSoundFile : "beep.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}