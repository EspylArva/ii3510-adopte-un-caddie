package com.wheretobuy.adopteuncaddie.module.openfoodfacts;

import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;

public interface BarcodeCallback {
    void onSuccess(ProductState productState);
}
