package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;

import com.wheretobuy.adopteuncaddie.utils.ManualBarcodeListener;


public class BarcodeScannerViewModel extends AndroidViewModel{ // implements ManualBarcodeListener {

    private String barcode;

    public BarcodeScannerViewModel(Application app) {
        super(app);
        setBarcode(null);
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        if(this.barcode != barcode)
        {
            this.barcode = (barcode);
        }
        Log.e("DATA BINDING", String.valueOf(barcode));
    }

//    @Override
//    public void onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//    }
//
//    @Override
//    public void onTouch(View v, MotionEvent event) {
//
//    }

}