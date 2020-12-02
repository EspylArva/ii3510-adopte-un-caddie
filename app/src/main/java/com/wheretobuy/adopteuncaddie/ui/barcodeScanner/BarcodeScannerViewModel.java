package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BarcodeScannerViewModel extends AndroidViewModel {

    private Integer barcode;

    public BarcodeScannerViewModel(Application app) {
        super(app);
//        barcode = new MutableLiveData<Integer>();
    }


    public Integer getBarcode() {
        return barcode;
    }

    public void setBarcode(Integer barcode) {
        this.barcode = (barcode);
        Toast.makeText(getApplication(), "BARCODE: " + barcode, Toast.LENGTH_SHORT).show();
        Log.e("DATA BINDING", String.valueOf(barcode));
    }
}