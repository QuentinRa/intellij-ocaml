package com.ocaml.ide.console.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.ocaml.ide.console.OCamlConsoleToolWindowFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Restart the console
 */
public class OCamlRestartAction extends DumbAwareAction {

    private final Project myProject;

    public OCamlRestartAction(Project project) {
        myProject = project;

        // set as a RestartAction
        ActionUtil.copyFrom(this, IdeActions.ACTION_RERUN);
        getTemplatePresentation().setIcon(AllIcons.Actions.Restart);
    }

    @Override public void actionPerformed(@NotNull AnActionEvent e) {
        OCamlConsoleToolWindowFactory.restartConsole(myProject);
    }
}