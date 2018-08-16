package by.kolyall.utils;

import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Nick Unuchek (skype: kolyall) on 25.03.2017.
 */

public class NumberUtils {
    public static int randInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static String formatDouble(Double number) {
        if (number == null) return "0";
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);
        df.setMinimumIntegerDigits(1);
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
        return df.format(number);
    }

    @Nullable
    public static String formatDoubleToKm(Float distance) {
        if (distance == null) return null;
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);
        df.setMinimumIntegerDigits(1);
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator(',');
        df.setDecimalFormatSymbols(sym);
        return df.format(distance);
    }

    public static String formatToInt(Double number) {
        if (number == null) return "0";
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(0);
        df.setMinimumIntegerDigits(1);
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
        return df.format(number);
    }

    @Nullable
    public static String formatToInt(Float number) {
        if (number == null) return null;
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(0);
        df.setMinimumIntegerDigits(1);
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(sym);
        return df.format(number);
    }
}
