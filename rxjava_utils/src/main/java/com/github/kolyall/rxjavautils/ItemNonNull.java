package com.github.kolyall.rxjavautils;

import rx.functions.Func1;

/**
 * Created by User on 10.04.2017.
 */

public class ItemNonNull<T> implements Func1<T, Boolean> {
    public static final String TAG = ItemNonNull.class.getSimpleName();

    @Override
    public Boolean call(T item) {
        ThreadUtils.printThread(TAG);
        return item != null;
    }
}
