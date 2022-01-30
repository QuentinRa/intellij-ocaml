package com.dune.lang.core.psi;

import com.dune.DuneLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DuneElement extends IElementType implements DuneElementType {
    public DuneElement(@NonNls @NotNull String debugName) {
        super(debugName, DuneLanguage.INSTANCE);
    }
}
