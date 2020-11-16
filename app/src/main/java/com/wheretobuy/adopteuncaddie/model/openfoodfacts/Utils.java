package com.wheretobuy.adopteuncaddie.model.openfoodfacts;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Utils {


    public static String getRoundNumber(String value) {
        if ("0".equals(value)) {
            return value;
        }

        if (TextUtils.isEmpty(value)) {
            return "?";
        }

        String[] strings = value.split("\\.");
        if (strings.length == 1 || (strings.length == 2 && strings[1].length() <= 2)) {
            return value;
        }

        return String.format(Locale.getDefault(), "%.2f", Double.valueOf(value));
    }

    /**
     * @see Utils#getRoundNumber(String)
     */
    public static String getRoundNumber(float value) {
        return getRoundNumber(Float.toString(value));
    }

    @NonNull
    public static String getModifierNonDefault(String modifier) {
        return modifier.equals(Modifier.DEFAULT_MODIFIER) ? "" : modifier;
    }

    public static class Modifier {
        public static final String GREATER_THAN = ">";
        public static final String LESS_THAN = "<";
        public static final String EQUALS_TO = "=";
        public static final String DEFAULT_MODIFIER = EQUALS_TO;
        public static final String[] MODIFIERS = {EQUALS_TO, LESS_THAN, GREATER_THAN};

        private Modifier() {

        }
    }
}
