package com.ocaml.lang.core;

import com.intellij.psi.tree.IElementType;
import com.reason.lang.ocaml.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OclTokenType extends IElementType {

    public OclTokenType(@NotNull @NonNls String debugName) {
        super(debugName, OclLanguage.INSTANCE);
    }

}