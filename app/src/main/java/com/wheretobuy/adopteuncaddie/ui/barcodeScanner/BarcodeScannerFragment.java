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
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.vision.barcode.Barcode;
import com.wheretobuy.adopteuncaddie.R;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.Product;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;
import com.wheretobuy.adopteuncaddie.module.openfoodfacts.BarcodeCallback;
import com.wheretobuy.adopteuncaddie.module.openfoodfacts.OpenFoodFactsService;
import com.wheretobuy.adopteuncaddie.module.openfoodfacts.RetrofitCall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class BarcodeScannerFragment extends Fragment implements CaptureFragment.BarcodeReaderListener {

    private static boolean REQUIRE_CONFIRMATION = true;
    private static final List<Integer> ACCEPTED_BARCODE_FORMATS = new ArrayList<Integer>(Arrays.asList(1, 2, 4, 8, 32, 64, 128, 512, 1024, 5));
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
        Log.d(TAG, "onScanned: " + barcode.displayValue + " (format: " + barcode.format + ")");
        if(ACCEPTED_BARCODE_FORMATS.contains(barcode.format))
        {
            Log.d("Calling retrofit", "Barcode: " + barcode.displayValue);
            RetrofitCall.callProductById(productState -> {
                if(productState.getStatus() == 1) // status_verbose: product found
                {
                    NavController navController = NavHostFragment.findNavController(this);
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
                            // TODO
                        }
                    } // else: a navRequest has already been posted, we're just waiting for the transition.
                      // Avoid the following code from being ran twice, as the fragment has technically already been changed
                } // else: (status == 0) -> status_verbose: product not found
            }, barcode.displayValue);
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Log.d(TAG, "onScannedMultiple: " + barcodes.size());
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