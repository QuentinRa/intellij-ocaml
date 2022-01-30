package com.ocaml.ide.actions.editor;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.vfs.VirtualFile;
import com.ocaml.ide.files.OCamlFileType;
import org.jetbrains.annotations.NotNull;

public abstract class OCamlEditorActionBase extends DumbAwareAction implements OCamlPromotedAction{

    public OCamlEditorActionBase() {
    }

    @Override public void update(@NotNull AnActionEvent e) {
        VirtualFile data = e.getData(CommonDataKeys.VIRTUAL_FILE);
        boolean ok = data != null && data.getFileType() == OCamlFileType.INSTANCE;

        Presentation presentation = e.getPresentation();
        presentation.setEnabled(e.isFromActionToolbar() || ok);
        presentation.setVisible(ok);
    }
}
