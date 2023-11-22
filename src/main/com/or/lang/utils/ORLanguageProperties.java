package com.or.lang.utils;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ORLanguageProperties {
    static @Nullable ORLanguageProperties cast(@Nullable Language language) {
        return (language instanceof ORLanguageProperties) ? (ORLanguageProperties) language : null;
    }

    @NotNull String getParameterSeparator();

    @NotNull String getFunctionSeparator();

    @NotNull String getTemplateStart();

    @NotNull String getTemplateEnd();
}
