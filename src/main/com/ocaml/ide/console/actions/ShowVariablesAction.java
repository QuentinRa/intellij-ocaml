package com.ocaml.ide.console.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.console.OCamlConsoleRunner;
import org.jetbrains.annotations.NotNull;

/**
 * Action to show/hide the variable view
 */
public class ShowVariablesAction extends ToggleAction {

    private final OCamlConsoleRunner consoleRunner;

    public ShowVariablesAction(OCamlConsoleRunner consoleRunner) {
        super(OCamlBundle.message("repl.variable.view.show.title"), OCamlBundle.message("repl.variable.view.show.desc"), AllIcons.Debugger.Watch);
        this.consoleRunner = consoleRunner;
    }

    @Override public boolean isSelected(@NotNull AnActionEvent e) {
        return consoleRunner.isVariableViewEnabled();
    }

    @Override public void setSelected(@NotNull AnActionEvent e, boolean state) {
        consoleRunner.showVariableView(state);
    }
}
