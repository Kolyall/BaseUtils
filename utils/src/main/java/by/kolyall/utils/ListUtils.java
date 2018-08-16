package by.kolyall.utils;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ListUtils {
    public static <T> boolean isEmpty(@Nullable List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isEquals(@Nullable List<T> first, @Nullable List<T> second) {
        if (first == null && second == null) return true;
        if (first == null) return false;
        if (second == null) return false;

        Collection<T> similar = new HashSet<T>(first);
        Collection<T> different = new HashSet<T>();
        different.addAll(first);
        different.addAll(second);

        similar.retainAll(second);
        different.removeAll(similar);
        return different.size() == 0;
    }
}
