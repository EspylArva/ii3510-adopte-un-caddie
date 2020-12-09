package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasketViewModel extends AndroidViewModel {

    public ArrayList<String> getmItemNames() {
        return mItemNames;
    }

    public ArrayList<String> getmImageUrls() {
        return mImageUrls;
    }

    // FIXME Fix temporaire pour garder un seul panier
    private static ArrayList<String> mItemNames = new ArrayList<>();
    private static ArrayList<String> mImageUrls = new ArrayList<>();

    private MutableLiveData<String> mText;

    public BasketViewModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("This is basket fragment");
    }

    public LiveData<String> getText() { return mText;
    }





}
