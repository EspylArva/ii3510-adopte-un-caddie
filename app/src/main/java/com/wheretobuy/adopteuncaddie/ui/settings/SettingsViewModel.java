package com.wheretobuy.adopteuncaddie.ui.settings;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;

import java.util.ArrayList;

public class SettingsViewModel extends AndroidViewModel {

    public SharedPreferences cbList;
    public ArrayList<String> cbArrayList;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        cbList = getApplication().getSharedPreferences("CB", Context.MODE_PRIVATE);
        cbArrayList = new ArrayList<>();
    }

    public ArrayList<String> getGetCbArrayList() { return cbArrayList; }

    public void saveCB() {
        SharedPreferences.Editor prefsEditor = cbList.edit();
        String json = new Gson().toJson(getGetCbArrayList());
        prefsEditor.putString("CB", json);
        prefsEditor.apply();
    }

}
