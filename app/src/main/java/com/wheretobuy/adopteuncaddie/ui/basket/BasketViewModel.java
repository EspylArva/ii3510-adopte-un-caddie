package com.wheretobuy.adopteuncaddie.ui.basket;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import java.util.ArrayList;

import timber.log.Timber;

public class BasketViewModel extends AndroidViewModel {

    public SharedPreferences basketList;
    private MutableLiveData<ArrayList<Article>> articlesArrayList;
    private MutableLiveData<Float> totalPrice;

    public MutableLiveData<ArrayList<Article>> getArticlesArrayList() {
        return articlesArrayList;
    }
    public MutableLiveData<Float> getTotalPrice() { return totalPrice; }

    public void setArticlesArrayList(ArrayList<Article> articleArrayList) {
        this.articlesArrayList.setValue(articleArrayList);
    }

    public void addItem (Article article){
        getArticlesArrayList().getValue().add(article);
        this.setArticlesArrayList(this.getArticlesArrayList().getValue());
        saveBasket();
    }

    public void deleteItem (Article article){
        getArticlesArrayList().getValue().remove(article);
        this.setArticlesArrayList(this.getArticlesArrayList().getValue());
        saveBasket();
    }

    // Lucas
//    public float getTotalPrice(ArrayList<Article> articlesArrayList)
    public float computeTotalPrice()
    {
        float price = 0;
        for(Article art : this.articlesArrayList.getValue())
        {
            price += art.getPrice() * art.getQuantity();
        }
        totalPrice.setValue(price);
        return price;
//        ArrayList<Float> prices = getArticlesPrice(articlesArrayList);
//        return (float)prices.stream().mapToDouble(a -> a).sum();
    }

    // Antoine
//    public ArrayList<Float> getArticlesPrice(ArrayList<Article> articlesArrayList) {
//        ArrayList<Float> articlesPrice = new ArrayList<>();
//        for (int i = 0; i < articlesArrayList.size(); i++) {
//            articlesPrice.add(articlesArrayList.get(i).getPrice());
//        }
//        return articlesPrice;
//    }

    public void emptyBasket()
    {
        setArticlesArrayList(new ArrayList<Article>());
        saveBasket();
    }

    public void saveBasket() {
        SharedPreferences.Editor prefsEditor = basketList.edit();
        String json = new Gson().toJson(getArticlesArrayList().getValue());
        prefsEditor.putString("Articles", json);
        prefsEditor.apply();
    }

    public BasketViewModel(Application app) {
        super(app);
        articlesArrayList = new MutableLiveData<ArrayList<Article>>();
        totalPrice = new MutableLiveData<Float>();

        totalPrice.setValue(0f);
        articlesArrayList.setValue(new ArrayList<Article>());

        basketList = getApplication().getSharedPreferences("Articles", Context.MODE_PRIVATE);
    }

    public void setNewQuantityForArticle(int position, int newQuantity) {
        if(printBasket())
        {
            this.articlesArrayList.getValue().get(position).setQuantity(newQuantity);
            saveBasket();
        }
//        articles.get(position).setQuantity(articles.get(position).getQuantity() - 1);
//                            vm.setArticlesArrayList(articles);
//                            vm.saveBasket();
    }

    private boolean printBasket()
    {
        if(this.articlesArrayList != null && this.articlesArrayList.getValue() != null)
        {
            if(this.articlesArrayList.getValue().size() == 0)
            {
                Timber.d("Panier vide!");
                return false;
            }
            else{
                for(Article art : this.articlesArrayList.getValue()) { Timber.d(art.toString()); }
                return true;
            }
        }
        else
        {
            Timber.d("ERROR: PANIER NULL");
            return false;
        }
    }
}
