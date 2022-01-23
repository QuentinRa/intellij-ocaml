package com.ocaml.ide.console.actions;

import com.intellij.execution.console.LanguageConsoleView;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.ProcessHandler;
import org.jetbrains.annotations.NotNull;

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

    // runExecuteAction -> fetch the command
    // -> add to history -> execute command
    // -> call doExecute -> call execute
}
