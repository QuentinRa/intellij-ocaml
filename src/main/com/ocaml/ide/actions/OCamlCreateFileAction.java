package com.ocaml.ide.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.files.OCamlFileTemplates;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.ide.module.OCamlModuleType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Create a .ml
 * Create a .mli
 * Create both a .ml and a .mli
 */
public class OCamlCreateFileAction extends CreateFileFromTemplateAction {
    private static final String ACTION_NAME = "OCaml";
    /*
     * ISSUE: we can define ONLY one behavior per template, but for that
     * we need a valid template. As I need a third template to create .ml + .mli,
     * I'm using this dummy template, then doing the job with valid templates in "createFile".
     */
    private static final String DUMMY_TEMPLATE = "DummyTemplate";

    public OCamlCreateFileAction() {
        super(ACTION_NAME, "", OCamlIcons.FileTypes.OCAML_SOURCE_AND_INTERFACE);
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

        builder.setTitle(ACTION_NAME)
                // create .ml
                .addKind(ml, OCamlIcons.FileTypes.OCAML_SOURCE, OCamlFileTemplates.OCAML_FILE_TEMPLATE)
                // create .mli
                .addKind(mli, OCamlIcons.FileTypes.OCAML_INTERFACE, OCamlFileTemplates.OCAML_INTERFACE_TEMPLATE)
                // create .ml + .mli
                .addKind(mlAndMli, OCamlIcons.FileTypes.OCAML_SOURCE_AND_INTERFACE, DUMMY_TEMPLATE)
        ;
    }

    @Override protected PsiFile createFile(String name, String templateName, PsiDirectory dir) {
        if (templateName.equals(DUMMY_TEMPLATE)) {
            String ml = OCamlFileType.DOT_DEFAULT_EXTENSION;
            String mli = OCamlInterfaceFileType.DOT_DEFAULT_EXTENSION;
            PsiFile file = null;
            String message = "";
            // try creating .mli, if needed
            try {
                dir.checkCreateFile(name+mli);
                file = super.createFile(name, OCamlFileTemplates.OCAML_INTERFACE_TEMPLATE, dir);
            } catch (IncorrectOperationException e) {
                message = e.getLocalizedMessage();
            }
            // try creating .ml, if needed
            try {
                dir.checkCreateFile(name+ml);
                file = super.createFile(name, OCamlFileTemplates.OCAML_FILE_TEMPLATE, dir);
            } catch (IncorrectOperationException e) {
                message = e.getLocalizedMessage();
            }

            // return the last exception
            if (file == null) throw new IncorrectOperationException(message);

            return file;
        }
        return super.createFile(name, templateName, dir);
    }

    @SuppressWarnings("UnstableApiUsage") @Override
    protected @NlsContexts.Command String getActionName(PsiDirectory directory, @NonNls @NotNull String newName, @NonNls String templateName) {
        return ACTION_NAME;
    }
}
