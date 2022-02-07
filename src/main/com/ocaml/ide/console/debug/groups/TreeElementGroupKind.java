package com.ocaml.ide.console.debug.groups;

import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public enum TreeElementGroupKind {
    EXCEPTION("repl.variable.view.exceptions"),
    MODULE("repl.variable.view.modules"),
    TYPES("repl.variable.view.types"),
    FUNCTIONS("repl.variable.view.functions"),
    VARIABLES("repl.variable.view.variables");

    public final String displayName;

    TreeElementGroupKind(@NotNull @PropertyKey(resourceBundle = OCamlBundle.BUNDLE) String key) {
        this.displayName = OCamlBundle.message(key);
    }
}
