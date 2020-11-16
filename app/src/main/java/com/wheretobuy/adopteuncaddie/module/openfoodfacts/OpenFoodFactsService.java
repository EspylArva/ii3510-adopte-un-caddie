package com.wheretobuy.adopteuncaddie.module.openfoodfacts;


import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface OpenFoodFactsService {
    // https://world.openfoodfacts.org/api/v0/product/3596710414222.json

    @GET("v0/product/{id}.json")
    Call<ProductState> getProductById(@Path("id") String barcodeId);

}