package com.github.aleksandermielczarek.contactpicker.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.MenuItem;

/**
 * Created by Aleksander Mielczarek on 19.03.2017.
 */

public final class Utils {

    private Utils() {

    }

    public static void tintMenuIcon(Context context, MenuItem item, @ColorRes int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(context, color));
        item.setIcon(wrapDrawable);
    }

    @ColorRes
    public static int colorFromAttr(Context context, @AttrRes int colorAttr, @ColorRes int defaultColor) {
        int[] attrs = {colorAttr};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int color = ta.getResourceId(0, defaultColor);
        ta.recycle();
        return color;
    }
}
