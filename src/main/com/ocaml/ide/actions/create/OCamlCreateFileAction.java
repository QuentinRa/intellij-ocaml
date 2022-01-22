package com.ocaml.ide.actions.create;

import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class OCamlCreateFileAction extends AbstractCreateFileAction {
    private static final String TEXT = "OCaml File";
    private static final Icon ICON = OCamlIcons.FileTypes.OCAML;

    public OCamlCreateFileAction() {
        super(TEXT, ICON);
    }

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder.setTitle(TEXT)
                // new option : empty file
                .addKind(OCamlBundle.message("filetype.new.empty.file"), ICON, TEXT);
    }

    @SuppressWarnings("UnstableApiUsage") @Override
    protected @NlsContexts.Command String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return TEXT;
    }
}
