package com.wheretobuy.adopteuncaddie.module.openfoodfacts;

import android.util.Log;

import com.wheretobuy.adopteuncaddie.model.openfoodfacts.Product;
import com.wheretobuy.adopteuncaddie.model.openfoodfacts.ProductState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public interface RetrofitCall {
    String OFF_BASE_URL = "https://world.openfoodfacts.org/api/";

    static void callProductById(final BarcodeCallback callback, String barcode)
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(OFF_BASE_URL).addConverterFactory(JacksonConverterFactory.create()).build();
        OpenFoodFactsService service = retrofit.create(OpenFoodFactsService.class);
        service.getProductById(barcode).enqueue(new Callback<ProductState>() {
            @Override
            public void onResponse(Call<ProductState> call, Response<ProductState> response) {
                Log.d("Retrofit", call.request().url().toString());
                callback.onSuccess(response.body());
            }
            @Override
            public void onFailure(Call<ProductState> call, Throwable t) {
                Log.e("Retrofit error", String.format("%s threw an error: %s", call.request().url(), t.getMessage()));
            }
        });
    }
}
