package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.ArrayList;

public class BasketViewModel extends AndroidViewModel {

    public SharedPreferences basketList;
    private MutableLiveData<ArrayList<Article>> articlesArrayList = new MutableLiveData<ArrayList<Article>>();


    public MutableLiveData<ArrayList<Article>> getArticlesArrayList() {
        return articlesArrayList;
    }

    public void setArticlesArrayList(ArrayList<Article> articleArrayList) {
        this.articlesArrayList.postValue(articleArrayList);
    }

    public void addItem (Article article){
        getArticlesArrayList().getValue().add(article);
        articlesArrayList.postValue(articlesArrayList.getValue());
    }

    public void deleteItem (Article article){
        getArticlesArrayList().getValue().remove(article);
        articlesArrayList.postValue(articlesArrayList.getValue());
    }

    public void emptyBasket()
    {
        setArticlesArrayList(new ArrayList<Article>());
        SharedPreferences.Editor prefsEditor = basketList.edit();
        prefsEditor.putString("Articles", "");
        prefsEditor.apply();
    }

    public BasketViewModel(Application app) {
        super(app);
        articlesArrayList.setValue(new ArrayList<Article>());
        basketList = getApplication().getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void saveBasket() {
        SharedPreferences.Editor prefsEditor = basketList.edit();
        String json = new Gson().toJson(getArticlesArrayList().getValue());
        prefsEditor.putString("Articles", json);
        prefsEditor.apply();
    }
}
