package com.dune.lang.core.psi;

import com.dune.DuneLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DuneTokenType extends IElementType {

    public DuneTokenType(@NotNull @NonNls String debugName) {
        super(debugName, DuneLanguage.INSTANCE);
    }

}