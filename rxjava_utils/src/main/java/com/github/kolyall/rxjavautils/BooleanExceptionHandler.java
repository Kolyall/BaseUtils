package com.github.kolyall.rxjavautils;

import rx.functions.Action1;

public class BooleanExceptionHandler implements Action1<Boolean> {

    private final boolean mAssert;
    private final RuntimeException mException;

    public BooleanExceptionHandler(final boolean mAssert, final RuntimeException exception) {
        this.mAssert = mAssert;
        this.mException = exception;
    }

    @Override
    public void call(final Boolean result) {
        if (result != null && (result != mAssert)) {
            throw mException;
        }
    }
}
