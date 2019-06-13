package by.kolyall.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

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
