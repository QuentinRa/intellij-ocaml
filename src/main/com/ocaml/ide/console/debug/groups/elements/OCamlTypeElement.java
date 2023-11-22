package com.ocaml.ide.console.debug.groups.elements;

import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OCamlTypeElement extends OCamlTreeElement {
    public OCamlTypeElement(@NotNull String name, @Nullable String value) {
        super(name, value, null, OCamlIcons.Nodes.TYPE);
    }
}
