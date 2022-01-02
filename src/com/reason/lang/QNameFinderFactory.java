package com.reason.lang;

import com.intellij.lang.*;
import com.reason.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class QNameFinderFactory {
    private QNameFinderFactory() {
    }

    @NotNull
    public static QNameFinder getQNameFinder(@NotNull Language language) {
        return OclQNameFinder.INSTANCE;
    }
}
