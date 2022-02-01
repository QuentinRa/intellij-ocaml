package com.or.lang.core.psi;

import com.or.lang.utils.ORLanguageProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PsiLanguageConverter {
    @NotNull
    String asText(@Nullable ORLanguageProperties language);
}
