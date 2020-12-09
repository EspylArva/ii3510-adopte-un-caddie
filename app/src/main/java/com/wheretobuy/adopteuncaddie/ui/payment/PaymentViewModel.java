package com.wheretobuy.adopteuncaddie.ui.payment;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class PaymentViewModel extends AndroidViewModel {
    private MutableLiveData<String> mText;

    public PaymentViewModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() { return mText;
    }
}
