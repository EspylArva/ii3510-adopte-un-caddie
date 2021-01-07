package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BasketViewModel extends AndroidViewModel {

//    public ArrayList<String> getmItemNames() {
//        return mItemNames;
//    }
//
//    public ArrayList<String> getmImageUrls() {
//        return mImageUrls;
//    }
//
//    private ArrayList<String> mItemNames = new ArrayList<>();
//    private ArrayList<String> mImageUrls = new ArrayList<>();

    public ArrayList<Articles> getArticlesArrayList() {
        return articlesArrayList;
    }

    public void setArticlesArrayList(ArrayList<Articles> articlesArrayList) {
        this.articlesArrayList = articlesArrayList;
    }

    public ArrayList<String> getArticlesName(ArrayList<Articles> articlesArrayList) {
        ArrayList<String> articlesName = new ArrayList<>();
        for (int i = 0; i < articlesArrayList.size(); i++) {
            articlesName.add(articlesArrayList.get(i).getName());
        }
        return articlesName;
    }

    public ArrayList<String> getArticlesUrl(ArrayList<Articles> articlesArrayList) {
        ArrayList<String> articlesUrl = new ArrayList<>();
        for (int i = 0; i < articlesArrayList.size(); i++) {
            articlesUrl.add(articlesArrayList.get(i).getUrl());
        }
        return articlesUrl;
    }

    public ArrayList<Float> getArticlesPrice(ArrayList<Articles> articlesArrayList) {
        ArrayList<Float> articlesPrice = new ArrayList<>();
        for (int i = 0; i < articlesArrayList.size(); i++) {
            articlesPrice.add(articlesArrayList.get(i).getPrice());
        }
        return articlesPrice;
    }

    public ArrayList<Integer> getArticlesQuantity(ArrayList<Articles> articlesArrayList) {
        ArrayList<Integer> articlesQuantity = new ArrayList<>();
        for (int i = 0; i < articlesArrayList.size(); i++) {
            articlesQuantity.add(articlesArrayList.get(i).getQuantity());
        }
        return articlesQuantity;
    }

    private ArrayList<Articles> articlesArrayList = new ArrayList<Articles>();


    private MutableLiveData<String> mText;

    public BasketViewModel(Application app) {
        super(app);
        mText = new MutableLiveData<>();
        mText.setValue("This is basket fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}
