package by.kolyall.utils;

import android.os.Bundle;

/**
 * Created by Nick Unuchek on 18.05.2017.
 */

public class BundleUtils {
    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";\n";
        }
        string += " }Bundle";
        return string;
    }
}
