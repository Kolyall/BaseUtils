package by.kolyall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by mpeterson on 8/21/16.
 */

public class KeyboardUtil {
    public static void hideKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus !=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            currentFocus.clearFocus();
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context,View view) {
        view.postDelayed(new Runnable(){
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                view.requestFocus();
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(view, 0);
                }
            }
        },200);
    }
}
