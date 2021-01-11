package com.wheretobuy.adopteuncaddie.utils;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.InverseBindingAdapter;

// https://stackoverflow.com/questions/44066770/not-working-in-databinding-edittext-integer-value
public class BindingUtils {

//    @BindingAdapter("android:text")
//    public static void setText(EditText view, String value) {
//        view.setText(String.valueOf(value));
//    }

//    @InverseBindingAdapter(attribute = "android:text")
//    public static Long getText(EditText view) {
//        if(!view.getText().toString().isEmpty() && view.getText().toString() == "null")
//        {
//            return Long.parseLong(view.getText().toString());
//        }
//        else
//        {
//            view.setText("");
//            return null;
//        }
//    }


//    @BindingAdapter("app:customOnEditorActionListener")
//    public static void setCustomOnEditorActionListener(EditText editText, ManualBarcodeListener listener) {
//        if (listener == null) {
//            editText.setOnEditorActionListener(null);
//        } else {
//            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView editText, int actionId, KeyEvent event){
//                    listener.onEditorAction(editText, actionId, event);
//                    return false;
//                }
//            });
//        }
//    }

//    @BindingAdapter("app:customOnTouchListener")
//    public static void setCustomOnTouchActionListener(EditText editText, ManualBarcodeListener listener) {
//        if (listener == null) {
//            editText.setOnTouchListener(null);
//        } else {
//            editText.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event)
//                {
//                    listener.onTouch(v, event);
//                    return false;
//                }
//            });
//        }
//    }

}