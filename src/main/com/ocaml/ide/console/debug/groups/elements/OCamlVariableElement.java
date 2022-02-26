package com.ocaml.ide.console.debug.groups.elements;

import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

public class OCamlVariableElement extends OCamlTreeElement {
    public OCamlVariableElement(@NotNull String name, @NotNull String value, @NotNull String type) {
        super(name, value, type, OCamlIcons.Nodes.VARIABLE);
    }
}
