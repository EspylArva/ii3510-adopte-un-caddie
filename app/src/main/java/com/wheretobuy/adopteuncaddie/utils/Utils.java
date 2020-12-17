package com.wheretobuy.adopteuncaddie.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Utils {

    public static String countryCodeToEmoji(String code) {
        // offset between uppercase ascii and regional indicator symbols
        int OFFSET = 127397;
        // validate code
        if (code == null || code.length() != 2) {
            return "";
        }
        //fix for uk -> gb
        if (code.equalsIgnoreCase("uk")) {
            code = "gb";
        }
        // convert code to uppercase
        code = code.toUpperCase();
        StringBuilder emojiStr = new StringBuilder();
        //loop all characters
        for (int i = 0; i < code.length(); i++) {
            emojiStr.appendCodePoint(code.charAt(i) + OFFSET);
        }
        // return emoji
        return emojiStr.toString();
    }

    public static void setLocale(String lang, Context context) {
        Locale myLocale = new Locale(lang);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        Configuration conf = context.getResources().getConfiguration();
        conf.locale = myLocale;
        context.getResources().updateConfiguration(conf, dm);
    }
}
