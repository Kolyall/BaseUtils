package com.github.kolyall.javautils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUtils {
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    public static <T> boolean isEquals(List<T> first, List<T> second) {
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

    public static <T> boolean listEqualsNoOrder(List<T> l1, List<T> l2) {
        final Set<T> s1 = new HashSet<>(l1);
        final Set<T> s2 = new HashSet<>(l2);

        return s1.equals(s2);
    }
}
