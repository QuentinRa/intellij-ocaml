package com.ocaml.ide.projectWizard.templates;

import com.intellij.platform.*;

import java.util.*;

public final class OCamlTemplateProvider {
    /**
     * Returns the templates available for OCaml.
     * This may not be the way to do, but there is no indications for now,
     * so I will use this.
     */
    public static ArrayList<ProjectTemplate> getAvailableTemplates() {
        ArrayList<ProjectTemplate> availableTemplates = new ArrayList<>();
        availableTemplates.add(new OCamlMakefileTemplate());
        return availableTemplates;
    }

    public static TemplateBuildInstructions getDefaultInstructions() {
        return new OCamlDefaultTemplateInstructions();
    }

    /**
     * Default instructions.
     * - create src
     */
    private static class OCamlDefaultTemplateInstructions implements TemplateBuildInstructions {
    }
}
