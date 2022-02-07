package com.ocaml.ide.console.debug.groups.elements;

import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NotNull;

public class OCamlModuleElement extends OCamlTreeElement {
    public OCamlModuleElement(@NotNull String name, @NotNull String type) {
        super(name, "<module>", type, OCamlIcons.Nodes.INNER_MODULE);
    }
}
