package com.wheretobuy.adopteuncaddie.utils;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public interface ManualBarcodeListener {
    void onEditorAction(TextView v, int actionId, KeyEvent event);
    void onTouch(View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event);
}
