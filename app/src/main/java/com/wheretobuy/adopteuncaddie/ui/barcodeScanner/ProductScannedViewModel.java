package com.wheretobuy.adopteuncaddie.ui.barcodeScanner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ProductScannedViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> quantity;

    public ProductScannedViewModel(@NonNull Application application) {
        super(application);

        quantity = new MutableLiveData<Integer>();
        quantity.setValue(1);
    }

    public void incrementQuantity() { quantity.setValue(quantity.getValue()+1); }
    public void decrementQuantity() { quantity.setValue(quantity.getValue()-1); }
    public MutableLiveData<Integer> getQuantity() {return quantity;}

}
