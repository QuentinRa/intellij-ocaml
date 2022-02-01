package com.ocaml.ide.console.debug.groups.elements;

import com.intellij.icons.AllIcons;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;

public class OCamlFunctionElement extends OCamlTreeElement {
    public OCamlFunctionElement(@NotNull String name, @NotNull String type) {
        super(name, OCamlREPLConstants.FUN, type, AllIcons.Nodes.Method);
    }
}
