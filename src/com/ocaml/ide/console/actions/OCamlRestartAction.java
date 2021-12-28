package com.ocaml.ide.console.actions;

import com.intellij.icons.*;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.ex.*;
import com.intellij.openapi.project.*;
import com.ocaml.ide.console.*;
import org.jetbrains.annotations.*;

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