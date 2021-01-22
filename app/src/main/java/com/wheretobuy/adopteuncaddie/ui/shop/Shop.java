package com.wheretobuy.adopteuncaddie.ui.shop;

import org.json.JSONException;
import org.json.JSONObject;

public class Shop
{
    private String name;
    private int shopUID;
    private String address;

    public Shop(String name, int shopUID) {
        this.name = name;
        this.shopUID = shopUID;
        this.address = "";
    }

    public Shop(JSONObject obj) throws JSONException
    {
        this.name = obj.getString("name");
        this.shopUID = Integer.parseInt(obj.getString("shop_uid"));
        this.address = obj.getString("adress");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getShopUID() {
        return shopUID;
    }

    public void setShopUID(int shopUID) {
        this.shopUID = shopUID;
    }
}
