package com.ocaml.ide.actions.create;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsActions;
import com.ocaml.ide.module.OCamlModuleType;

import javax.swing.*;

/**
 * We could have factorized more in this class,
 * but if that was needed, I could use #getPresentation instead of
 * static variables, but I think it's better to use static variables.
 */
public abstract class AbstractCreateFileAction extends CreateFileFromTemplateAction {

    public AbstractCreateFileAction(@NlsActions.ActionText String text, Icon icon) {
        super(text, "", icon);
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

}
