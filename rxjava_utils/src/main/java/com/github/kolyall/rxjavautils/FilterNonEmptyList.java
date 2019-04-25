package com.github.kolyall.rxjavautils;

import java.util.Collection;

import rx.functions.Func1;

/**
 * Created by Nick Unuchek on 24.10.2017.
 */

public class FilterNonEmptyList<T> implements Func1<Collection<T>, Boolean> {
    public static final String TAG = FilterNonEmptyList.class.getSimpleName();

    @Override
    public Boolean call(Collection<T> list) {
        ThreadUtils.printThread(TAG);
        return list != null && !list.isEmpty();
    }
}