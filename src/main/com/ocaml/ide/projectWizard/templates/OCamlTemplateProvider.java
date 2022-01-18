package com.ocaml.ide.projectWizard.templates;

import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.*;
import com.intellij.platform.*;
import com.ocaml.utils.psi.*;

import java.io.*;
import java.nio.file.*;
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
     * @see com.intellij.ide.util.projectWizard.WebProjectTemplate
     * @see com.intellij.openapi.module.WebModuleBuilder
     * @see DirectoryProjectConfigurator
     * @see ProjectTemplate
     * Files.createDirectories(dir);
     */
    private static class OCamlDefaultTemplateInstructions implements TemplateBuildInstructions {

        @Override public void createFiles(ModifiableRootModel rootModel, VirtualFile sourceRoot) {
            File sourceRootFile = VfsUtilCore.virtualToIoFile(sourceRoot);
            File mainFile = new File(sourceRootFile, "hello_world.ml");
            ArrayList<String> lines = new ArrayList<>();
            lines.add("let _ = Format.printf \"Hello, World!\"");
            try {
                Files.write(mainFile.toPath(), lines);
                PsiUtils.openFile(rootModel.getProject(), mainFile, true);
            } catch (IOException e) {
                System.out.println("error:"+e.getMessage());
            }
        }
    }
}
