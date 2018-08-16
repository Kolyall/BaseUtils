package by.kolyall.utils.spanable;

import android.content.res.Resources;
import android.graphics.Typeface;

public class AssetsTypefaceSpan extends CustomTypefaceSpan {
    public AssetsTypefaceSpan(Resources resources, String fontPath) {
        super(Typeface.createFromAsset(resources.getAssets(), fontPath));
    }
}