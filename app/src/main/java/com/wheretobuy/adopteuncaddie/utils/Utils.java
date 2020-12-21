package com.wheretobuy.adopteuncaddie.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.wheretobuy.adopteuncaddie.MainActivity;

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

    public static void setLocale(String lang, Activity activity) {
        Locale myLocale = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(activity, MainActivity.class);
        activity.finish();
        activity.startActivity(refresh);
    }
}
