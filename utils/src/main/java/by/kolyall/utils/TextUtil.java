package by.kolyall.utils;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.PluralsRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SuperscriptSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import by.kolyall.utils.spanable.AssetsTypefaceSpan;


public class TextUtil {

    public static String capitalizeFirstLetter(String original) {
        return original.length() == 0 ? original : original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }

    /**
     * Returns a CharSequence that applies boldface to the concatenation of the specified CharSequence objects.
     */
    public static CharSequence bold(CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.BOLD));
    }

    /**
     * Returns a CharSequence that applies italics to the concatenation of the specified CharSequence objects.
     */
    public static CharSequence italic(CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.ITALIC));
    }

    /**
     * Returns a CharSequence that applies a color to the concatenation of the specified CharSequence objects.
     */
    public static CharSequence color(int color, CharSequence... content) {
        return apply(content, new ForegroundColorSpan(color));
    }

    /**
     * Returns a CharSequence that concatenates the specified array of CharSequence objects and then applies a list of zero or more tags to
     * the entire range.
     *
     * @param content an array of character sequences to apply a style to
     * @param tags    the styled span objects to apply to the content such as android.text.style.StyleSpan
     */
    private static CharSequence apply(CharSequence[] content, Object... tags) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        openTags(text, tags);
        for (CharSequence item : content) {
            text.append(item);
        }
        closeTags(text, tags);
        return text;
    }

    /**
     * Iterates over an array of tags and applies them to the beginning of the specified Spannable object so that future text appended to
     * the text will have the styling applied to it. Do not call this method directly.
     */
    private static void openTags(Spannable text, Object[] tags) {
        for (Object tag : tags) {
            text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
        }
    }

    /**
     * "Closes" the specified tags on a Spannable by updating the spans to be endpoint-exclusive so that future text appended to the end
     * will not take on the same styling. Do not call this method directly.
     */
    private static void closeTags(Spannable text, Object[] tags) {
        int len = text.length();
        for (Object tag : tags) {
            if (len > 0) {
                text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                text.removeSpan(tag);
            }
        }
    }

    /**
     * change price text
     */
    public static SpannableStringBuilder makePriceString(String price) {
        SpannableStringBuilder cs = new SpannableStringBuilder(price);
        if (price != null && !price.equalsIgnoreCase("") && price.length() > 3) {
            cs.setSpan(new SuperscriptSpan(), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(1.5f), 1, price.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return cs;
        } else
            return null;
    }

    /**
     * copy to clipboard
     */
    public static String copyToClipboard(Context context,String text) {
        if (text != null && !text.equalsIgnoreCase("")) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        return text;
    }

    public static SpannableStringBuilder decreaseSecondLine(CharSequence title, CharSequence details, float scale) {

        String text = title + "\n" + details;
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        try {
            Pattern p = Pattern.compile("\n", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(text);
            while (m.find())
                sb.setSpan(new RelativeSizeSpan(scale), m.start(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }

    public static SpannableString createColoredText(Context context, SpannableString text, @ColorRes int colorId) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colorId)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString createColoredText(Resources resources, SpannableString text, @ColorRes int colorId) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(resources.getColor(colorId)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString createColoredText(SpannableString text, @ColorInt int color) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString createSizeText(SpannableString text, float proportion){
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new RelativeSizeSpan(proportion), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString customFont(Resources resources, SpannableString text, String fontPath) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AssetsTypefaceSpan(resources, fontPath), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    /**
     * Use it to avoid IllegalArgumentException("Path must not be empty.”) in Picasso
     */
    public static String checkForEmpty(String text){
        return TextUtils.isEmpty(text) ? null : text;
    }

    /**
     * Use it to avoid IllegalArgumentException("Path must not be empty.”) in Picasso
     */
    public static String checkEmptyUri(String text){
        return TextUtils.isEmpty(text) ? "" : text;
    }

    /**
     * Get string resources
     */
    public static String string(Context context, @StringRes int resId){
        return context.getString(resId);
    }

    public static String string(Context context, @StringRes int resId, Object... formatArgs){
        return context.getString(resId, formatArgs);
    }

    public static String quantityString(Context context, @PluralsRes int id, int quantity, Object... formatArgs){
        return context.getResources().getQuantityString(id, quantity, formatArgs);
    }

    public static String[] stringArray(Context context, @ArrayRes int id){
        return context.getResources().getStringArray(id);
    }
}
