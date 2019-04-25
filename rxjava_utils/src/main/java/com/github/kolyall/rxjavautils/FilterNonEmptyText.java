package com.github.kolyall.rxjavautils;

import com.github.kolyall.javautils.StringUtils;

import rx.functions.Func1;

/**
 * Created by User on 10.04.2017.
 */

public class FilterNonEmptyText implements Func1<CharSequence, Boolean> {
    public static final String TAG = FilterNonEmptyText.class.getSimpleName();

    @Override
    public Boolean call(CharSequence text) {
        ThreadUtils.printThread(TAG);
        return !StringUtils.isEmpty(text);
    }
}
