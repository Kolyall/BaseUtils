package com.github.kolyall.androidutils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by Nick Unuchek on 22.01.2018.
 */

public class DrawableUtils {
    public static Drawable getDrawable(Context context, @DrawableRes int drawableResId, @ColorRes int colorResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId).mutate();
        drawable.setColorFilter(ContextCompat.getColor(context, colorResId), PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }
}
