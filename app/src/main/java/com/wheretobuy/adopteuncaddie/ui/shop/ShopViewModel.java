package com.wheretobuy.adopteuncaddie.ui.shop;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wheretobuy.adopteuncaddie.ui.basket.Article;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShopViewModel extends AndroidViewModel
{
    private MutableLiveData<ArrayList<Shop>> shopsArrayList;
    public SharedPreferences shopList;

    public ShopViewModel(Application app)
    {
        super(app);
        shopsArrayList = new MutableLiveData<>();
        shopList = getApplication().getSharedPreferences("Shops", Context.MODE_PRIVATE);
    }

    public List<String> getSavedShopsName()
    {
        ArrayList<Shop> shops = getFromStorage();
        if(shops == null) return null;

        return shops.stream()
                .map(Shop::getName)
                .collect(Collectors.toList());
    }

    public void createFromAPIFetch(JSONArray response)
    {
        try
        {
            ArrayList<Shop> shops = new ArrayList<>();

            for (int i = 0; i < response.length(); i++)
            {
                shops.add(new Shop(response.getJSONObject(i)));
            }

            shopsArrayList.setValue(shops);
            saveShops();
        }
        catch (Exception e)
        {
            Log.d("onVolleyResponse: ", "Error :"+e.getMessage());
        }
    }

    public void setShopsArrayList(ArrayList<Shop> shopsArrayList)
    {
        this.shopsArrayList.setValue(shopsArrayList);
    }

    public ArrayList<Shop> getShopsArrayList()
    {
        return this.shopsArrayList.getValue();
    }

    public ArrayList<Shop> getFromStorage()
    {
        String json = shopList.getString("Shops", "");
        Type listType = new TypeToken<ArrayList<Shop>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

    public void emptyShops()
    {
        setShopsArrayList(new ArrayList<Shop>());
        saveShops();
    }

    public void saveShops()
    {
        SharedPreferences.Editor prefsEditor = shopList.edit();
        String json = new Gson().toJson(shopsArrayList.getValue());
        prefsEditor.putString("Shops", json);
        prefsEditor.apply();
    }

    public void saveShopUID(Integer uid)
    {
        SharedPreferences.Editor prefsEditor = shopList.edit();
        prefsEditor.putInt("Shop_UID", uid);
        prefsEditor.apply();
    }
}
