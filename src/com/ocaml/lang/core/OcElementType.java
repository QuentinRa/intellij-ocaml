package com.ocaml.lang.core;

import com.intellij.psi.tree.*;
import com.ocaml.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class OcElementType extends IElementType {

    public OcElementType(@NotNull @NonNls String debugName) {
        super(debugName, OclLanguage.INSTANCE);
    }
}