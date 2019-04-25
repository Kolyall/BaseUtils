package com.github.kolyall.rxjavautils;

import rx.functions.Func1;

/**
 * Created by Nick Unuchek on 24.10.2017.
 */

public class FilterNonEmptyArray<T> implements Func1<T[], Boolean> {
    public static final String TAG = FilterNonEmptyArray.class.getSimpleName();

    @Override
    public Boolean call(T[] list) {
        ThreadUtils.printThread(TAG);
        return list != null && list.length!=0;
    }
}
