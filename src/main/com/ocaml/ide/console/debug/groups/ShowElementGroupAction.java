package com.ocaml.ide.console.debug.groups;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareToggleAction;
import com.ocaml.OCamlBundle;
import com.ocaml.ide.console.debug.OCamlStupidTreeModel;
import com.ocaml.ide.console.debug.OCamlVariablesView;
import org.jetbrains.annotations.NotNull;

/**
 * Allow the user to hide/show a group of elements
 */
public class ShowElementGroupAction extends DumbAwareToggleAction {

    private final TreeElementGroup target;
    private final OCamlVariablesView view;

    public ShowElementGroupAction(@NotNull TreeElementGroupKind kind, @NotNull OCamlVariablesView view) {
        super(OCamlBundle.message("repl.variable.view.show.group", kind.displayName));
        this.view = view;

        OCamlStupidTreeModel treeModel = view.getTreeModel();
        switch (kind) {
            case EXCEPTIONS: target = treeModel.exceptions; break;
            case MODULES: target = treeModel.modules; break;
            case TYPES: target = treeModel.types; break;
            case FUNCTIONS: target = treeModel.functions; break;
            case VARIABLES: target = treeModel.variables; break;
            default: throw new IllegalStateException("Invalid kind:'"+kind.displayName+"'.");
        }
    }

    @Override public boolean isSelected(@NotNull AnActionEvent e) {
        return target.isVisible();
    }

    @Override public void setSelected(@NotNull AnActionEvent e, boolean state) {
        target.setVisible(state);
        view.invalidateTree();
    }
}
