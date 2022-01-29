package com.dune.lang.core.psi;

import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class DuneTokenType extends IElementType {

    public DuneTokenType(@NotNull @NonNls String debugName) {
        super(debugName, OCamlLanguage.INSTANCE);
    }

}