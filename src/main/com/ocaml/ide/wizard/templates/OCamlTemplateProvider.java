package com.ocaml.ide.wizard.templates;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectTemplate;
import com.ocaml.utils.files.OCamlFileUtils;
import com.ocaml.utils.logs.OCamlLogger;

import java.io.File;
import java.util.ArrayList;

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
     * - create src/hello_world.ml
     */
    private static class OCamlDefaultTemplateInstructions implements TemplateBuildInstructions {

        private static final Logger LOG = OCamlLogger.getTemplateInstance("default");

        @Override
        public void createFiles(ModifiableRootModel rootModel, VirtualFile sourceRoot) {
            File sourceRootFile = VfsUtilCore.virtualToIoFile(sourceRoot);
            OCamlFileUtils.createFile(sourceRootFile, "hello_world.ml", "let _ = Format.printf \"Hello, World!\"", LOG);
        }
    }
}
