package com.ocaml.ide.wizard.templates;

import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.*;
import com.intellij.platform.*;
import com.ocaml.utils.files.*;
import com.ocaml.utils.psi.*;

import java.io.*;
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
     * - create src/hello_world.ml
     */
    private static class OCamlDefaultTemplateInstructions implements TemplateBuildInstructions {

        @Override public void createFiles(ModifiableRootModel rootModel, VirtualFile sourceRoot) {
            File sourceRootFile = VfsUtilCore.virtualToIoFile(sourceRoot);
            File mainFile = OCamlFileUtils.createFile(sourceRootFile, "hello_world.ml", "let _ = Format.printf \"Hello, World!\"");
            if (mainFile != null) PsiUtils.openFile(rootModel.getProject(), mainFile, true);
        }
    }
}
