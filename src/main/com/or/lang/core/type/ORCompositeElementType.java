package com.or.lang.core.type;

import com.intellij.psi.tree.IElementType;
import com.ocaml.OCamlLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class ORCompositeElementType extends IElementType implements ORCompositeType {
    public ORCompositeElementType(@NotNull @NonNls String debugName) {
        super(debugName, OCamlLanguage.INSTANCE);
    }
}
