package com.ocaml.lang.core;

import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OCamlTokenType extends IElementType {

    public OCamlTokenType(@NotNull @NonNls String debugName) {
        super(debugName, OCamlLanguage.INSTANCE);
    }

}