package com.ocaml.ide.console.debug.groups.elements;

import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

public class OCamlTypeElement extends OCamlTreeElement {
    public OCamlTypeElement(@NotNull String name, @NotNull String type) {
        super(name, "<type>", type, OCamlIcons.Nodes.TYPE);
    }
}
