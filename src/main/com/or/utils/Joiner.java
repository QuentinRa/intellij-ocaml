package com.or.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class Joiner {

    private Joiner() {
    }

    @NotNull
    public static <T> String join(@NotNull String separator, @Nullable Iterable<T> items) {
        return join(separator, items, Object::toString);
    }

    @NotNull
    public static <T> String join(
            @NotNull String separator, @Nullable Iterable<T> items, @NotNull Function<T, String> fn) {
        if (items == null) {
            return "<null>";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (T item : items) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(fn.apply(item));
            first = false;
        }
        return sb.toString();
    }

    public static @NotNull String join(@NotNull String separator, Object [] items) {
        if (items == null) {
            return "<null>";
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object item : items) {
            if (!first) {
                sb.append(separator);
            }
            sb.append(item);
            first = false;
        }
        return sb.toString();
    }

    public static @NotNull String joinFrom(@NotNull String separator, Object [] items, int from) {
        if (items == null) {
            return "<null>";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = from; i < items.length; i++) {
            if (i != from) {
                sb.append(separator);
            }
            sb.append(items[i]);
        }
        return sb.toString();
    }
}
