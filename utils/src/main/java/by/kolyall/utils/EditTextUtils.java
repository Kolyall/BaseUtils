package by.kolyall.utils;

import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.StringRes;

/**
 * Created by Nick Unuchek on 30.08.2017.
 */

public class EditTextUtils {
    public static void setError(EditText editText, @StringRes int stringId) {
        if (stringId != Constants.NONE){
            editText.setError(editText.getContext().getString(stringId));
        }else{
            editText.setError(null);
        }
    }

    public static void setError(TextInputLayout inputLayout, @StringRes int stringId) {
        if (stringId != Constants.NONE) {
            inputLayout.setError(inputLayout.getContext().getString(stringId));
        } else {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        }
    }
}
