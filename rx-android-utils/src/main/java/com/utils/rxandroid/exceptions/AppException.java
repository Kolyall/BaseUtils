package com.utils.rxandroid.exceptions;

import android.support.annotation.StringRes;

/**
 * Created by Nick Unuchek on 21.12.2017.
 */

public class AppException extends RuntimeException {
    @StringRes private int messageResId;
    public AppException() {
        super();
    }
    public AppException(@StringRes int messageResId) {
        super();
        this.messageResId = messageResId;
    }

    public int getMessageResId() {
        return messageResId;
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }
}
