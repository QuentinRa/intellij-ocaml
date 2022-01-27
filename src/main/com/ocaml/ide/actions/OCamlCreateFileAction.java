package com.ocaml.ide.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.files.OCamlFileTemplates;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.ide.module.OCamlModuleType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Create a .ml
 * Create a .mli
 * Create both a .ml and a .mli
 */
public class OCamlCreateFileAction extends CreateFileFromTemplateAction {
    private static final Icon ICON = OCamlIcons.FileTypes.OCAML;
    private static final String ACTION_NAME = "OCaml";

    public OCamlCreateFileAction() {
        super(ACTION_NAME, "", ICON);
    }

    @Override protected boolean isAvailable(DataContext dataContext) {
        if(!super.isAvailable(dataContext)) return false;
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) return false;
        // if we did click on a file
        var v = CommonDataKeys.PSI_FILE.getData(dataContext);
        if (v != null) { // is the file we clicked on
            Module module = ModuleUtil.findModuleForFile(v);
            if (module != null) // inside an ocaml module?
                return ModuleType.get(module) instanceof OCamlModuleType;
        }
        // is there an ocaml module in the project?
        for (Module module : ModuleManager.getInstance(project).getModules()) {
            if (ModuleType.get(module) instanceof OCamlModuleType)
                return true;
        }
        return false;
    }

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.@NotNull Builder builder) {
        String ml = OCamlFileType.DOT_DEFAULT_EXTENSION;
        String mli = OCamlInterfaceFileType.DOT_DEFAULT_EXTENSION;
        String mlAndMli = ml+" + "+mli;
        String diff = mli.replace(ml, "");

        builder.setTitle(ACTION_NAME)
                // create .ml
                .addKind(ml, OCamlIcons.FileTypes.OCAML, OCamlFileTemplates.OCAML_FILE_TEMPLATE)
                // create .mli
                .addKind(mli, OCamlIcons.FileTypes.OCAML_INTERFACE, OCamlFileTemplates.OCAML_INTERFACE_TEMPLATE)
                // create .ml + .mli
                .addKind(mlAndMli, OCamlIcons.FileTypes.OCAML, OCamlFileTemplates.OCAML_FILE_TEMPLATE, new InputValidator() {
                    @Override public boolean checkInput(String inputString) {
                        try {
                            directory.checkCreateFile(inputString+diff);
                            return true;
                        } catch (IncorrectOperationException e) {
                            return false;
                        }
                    }

                    @Override public boolean canClose(String inputString) {
                        // mli created?
                        try {
                            final FileTemplate template =
                                    FileTemplateManager
                                            .getInstance(directory.getProject())
                                            .getInternalTemplate(OCamlFileTemplates.OCAML_INTERFACE_TEMPLATE);
                            createFileFromTemplate(inputString, template, directory);
                            return true;
                        } catch (Exception e) {
                            return false;
                        }
                    }
                })
        ;
    }

    @SuppressWarnings("UnstableApiUsage") @Override
    protected @NlsContexts.Command String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return ACTION_NAME;
    }
}
