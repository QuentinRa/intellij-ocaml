package com.ocaml.ide.actions.create;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;
import com.ocaml.icons.OCamlIcons;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class OCamlCreateFileAction extends CreateFileFromTemplateAction {
    protected static final String TEXT = "OCaml File";

    public OCamlCreateFileAction() {
        super(TEXT, "", OCamlIcons.FileTypes.OCAML);
    }

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        builder.setTitle(TEXT).addKind(
                "Empty file",
                OCamlIcons.FileTypes.OCAML,
                TEXT
        );
    }

    @SuppressWarnings("UnstableApiUsage") @Override
    protected @NlsContexts.Command String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return TEXT;
    }
}
