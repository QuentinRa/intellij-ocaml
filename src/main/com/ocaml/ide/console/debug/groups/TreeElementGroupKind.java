package com.ocaml.ide.console.debug.groups;

import com.ocaml.OCamlBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

// it's not safe to add a value here
// ensure that you know were they are used before adding one
// - removeElement
// - parser + tests
// - create an element in elements/
// - ShowElementGroupAction
// - OCamlVariablesView#rebuild
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
