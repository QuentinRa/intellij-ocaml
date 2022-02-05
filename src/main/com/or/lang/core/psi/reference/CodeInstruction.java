package com.or.lang.core.psi.reference;

import com.intellij.psi.PsiElement;
import com.or.lang.core.psi.PsiInclude;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class CodeInstruction {
    final PsiElement mySource;
    final String[] myValues;
    Set<String> myAlternateValues = null;

    public CodeInstruction(@NotNull PsiElement source, @Nullable String value, @Nullable Collection<PsiInclude> includes) {
        mySource = source;
        myValues = value == null ? null : new String[]{value};
        if (includes != null && !includes.isEmpty()) {
            myAlternateValues = includes.stream().map(PsiInclude::getIncludePath).collect(Collectors.toSet());
        }
    }

    public CodeInstruction(@NotNull PsiElement source, @Nullable String value) {
        this(source, value, null);
    }

    public @Nullable String getFirstValue() {
        return myValues == null ? null : myValues[0];
    }

    @Override
    public @NotNull String toString() {
        return "[" + mySource.getClass().getSimpleName() + ":" + Joiner.join("/", myValues) +
                (myAlternateValues == null ? "" : " (" + Joiner.join(", ", myAlternateValues) + ")")
                + "]";
    }
}
