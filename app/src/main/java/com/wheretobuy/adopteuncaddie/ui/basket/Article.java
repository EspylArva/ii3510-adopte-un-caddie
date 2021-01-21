package com.wheretobuy.adopteuncaddie.ui.basket;

public class Article {

    String url = "";
    String name = "";
    int quantity = 0;
    float price = 0;

    public Article(String url, String name, int quantity, float price) {
        this.url = url;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }


    @Override
    public String toString()
    {
        return String.format("%s (%sx%s)", getName(), getPrice(), getQuantity());
    }


}
