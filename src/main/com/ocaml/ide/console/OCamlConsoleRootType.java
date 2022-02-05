package com.ocaml.ide.console;

import com.intellij.execution.console.ConsoleRootType;
import com.intellij.ide.scratch.RootType;
import org.jetbrains.annotations.NotNull;

public class OCamlConsoleRootType extends ConsoleRootType {

    public OCamlConsoleRootType() {
        super("OCAML", "OCaml console");
    }

    public static @NotNull OCamlConsoleRootType getInstance() {
        return RootType.findByClass(OCamlConsoleRootType.class);
    }

}
