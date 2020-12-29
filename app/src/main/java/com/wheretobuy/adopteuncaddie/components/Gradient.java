package com.wheretobuy.adopteuncaddie.components;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class Gradient
{
    public static class GradientColor
    {


        private int startColor;
        private int endColor;

        public GradientColor(int start, int end){
            startColor = start;
            endColor = end;
        }

        public int getStartColor() {
            return startColor;
        }
        public void setStartColor(int startColor) {
            this.startColor = startColor;
        }
        public int getEndColor() {
            return endColor;
        }
        public void setEndColor(int endColor) {
            this.endColor = endColor;
        }
    }

    /**
     * Use: view.setBackground(Gradient.NewGradient(new Gradient.GradientColor(0xFFababab, 0xFF888888)));
     * @param gc
     * @return
     */
    public static GradientDrawable NewGradient(GradientColor gc) {
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {gc.getStartColor(), gc.getEndColor() });
        gd.setCornerRadius(0f);
        gd.setStroke(2, gc.getStartColor());
        gd.setCornerRadius(3f);
        return gd;
    }


}