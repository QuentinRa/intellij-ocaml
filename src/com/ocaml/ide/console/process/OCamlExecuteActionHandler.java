package com.ocaml.ide.console.process;

import com.intellij.execution.console.*;
import com.intellij.execution.process.*;
import org.jetbrains.annotations.*;

/**
 * OCaml command need ";;" at the end of each expression
 */
public class OCamlExecuteActionHandler extends ProcessBackedConsoleExecuteActionHandler {

    public OCamlExecuteActionHandler(ProcessHandler processHandler, boolean preserveMarkup) {
        super(processHandler, preserveMarkup);
    }

    @Override protected void execute(@NotNull String text, @NotNull LanguageConsoleView console) {
        if (!text.endsWith(";;")) text += ";;";
        super.execute(text, console);
    }
}
