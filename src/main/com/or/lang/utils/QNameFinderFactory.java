package com.or.lang.utils;

import org.jetbrains.annotations.NotNull;

public class QNameFinderFactory {
    private QNameFinderFactory() {
    }

    @NotNull
    public static QNameFinder getQNameFinder() {
        return OclQNameFinder.INSTANCE;
    }
}
