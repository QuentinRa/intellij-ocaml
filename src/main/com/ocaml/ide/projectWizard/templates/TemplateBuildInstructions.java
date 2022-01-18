package com.ocaml.ide.projectWizard.templates;

import org.jetbrains.annotations.NotNull;

/**
 * Instructions to build the template.
 */
public interface TemplateBuildInstructions {

    /** Name of the source folder that will be created. **/
    default @NotNull String getSourceFolderName() {
        return "src";
    }
}