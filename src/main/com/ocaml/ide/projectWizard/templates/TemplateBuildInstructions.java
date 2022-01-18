package com.ocaml.ide.projectWizard.templates;

import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.NotNull;

/**
 * Instructions to build the template.
 */
public interface TemplateBuildInstructions {

    /** Name of the source folder that will be created. **/
    default @NotNull String getSourceFolderName() {
        return "src";
    }

    /**
     * Create files used by the template
     *
     * @param rootModel see setupRootModel
     * @param sourceRoot the folder that was created using {@link #getSourceFolderName}
     *
     * @see com.intellij.ide.util.projectWizard.ModuleBuilder#setupRootModel
     **/
    void createFiles(ModifiableRootModel rootModel, VirtualFile sourceRoot);
}