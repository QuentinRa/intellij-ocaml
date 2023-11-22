package com.or.lang.core.type;

import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ORTokenElementType extends IElementType {
    private final String debugName;

    public ORTokenElementType(@NotNull @NonNls String debugName) {
        super(debugName, OCamlLanguage.INSTANCE);
        this.debugName = debugName;
    }

    public String getSymbol() {
        return debugName;
    }
}
