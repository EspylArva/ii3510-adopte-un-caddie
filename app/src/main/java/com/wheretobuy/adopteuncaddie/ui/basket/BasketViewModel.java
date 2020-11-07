package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BasketViewModel extends AndroidViewModel {

    private MutableLiveData<String> mText;

    public BasketViewModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("This is basket fragment");
    }

    public LiveData<String> getText() { return mText;
    }
}
