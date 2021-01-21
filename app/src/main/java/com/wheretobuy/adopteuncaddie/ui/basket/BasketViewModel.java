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
    private MutableLiveData<ArrayList<Article>> articlesArrayList;


    public MutableLiveData<ArrayList<Article>> getArticlesArrayList() {
        return articlesArrayList;
    }

    public void setArticlesArrayList(ArrayList<Article> articleArrayList) {
        this.articlesArrayList.postValue(articleArrayList);
    }

    public void addItem (Article article){
        getArticlesArrayList().getValue().add(article);
        articlesArrayList.postValue(articlesArrayList.getValue());
        saveBasket();
    }

    public void deleteItem (Article article){
        getArticlesArrayList().getValue().remove(article);
        articlesArrayList.postValue(articlesArrayList.getValue());
        saveBasket();
    }

    public float getTotalPrice(ArrayList<Article> articlesArrayList)
    {
        ArrayList<Float> prices = getArticlesPrice(articlesArrayList);
        return (float)prices.stream().mapToDouble(a -> a).sum();
    }

    public ArrayList<Float> getArticlesPrice(ArrayList<Article> articlesArrayList) {
        ArrayList<Float> articlesPrice = new ArrayList<>();
        for (int i = 0; i < articlesArrayList.size(); i++) {
            articlesPrice.add(articlesArrayList.get(i).getPrice());
        }
        return articlesPrice;
    }

    public void emptyBasket()
    {
        setArticlesArrayList(new ArrayList<Article>());
        saveBasket();
    }

    public BasketViewModel(Application app) {
        super(app);
        articlesArrayList = new MutableLiveData<>();
        articlesArrayList.setValue(new ArrayList<Article>());
        basketList = getApplication().getSharedPreferences("Articles", Context.MODE_PRIVATE);
    }

    private void saveBasket() {
        SharedPreferences.Editor prefsEditor = basketList.edit();
        String json = new Gson().toJson(getArticlesArrayList().getValue());
        prefsEditor.putString("Articles", json);
        prefsEditor.apply();
    }
}
