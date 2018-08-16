package by.kolyall.utils;

import android.util.Log;

/**
 * Created by Nikolay Unuchek on 15.03.2017.
 */

public class DLog {
    public static void i(String tag, String message) {
        Log.i(tag,message);
    }

    public static void e(String tag, Throwable throwable, String message) {
//        Crashlytics.logException(new Exception(message,throwable));
        Log.e(tag, message, throwable);
    }
}
