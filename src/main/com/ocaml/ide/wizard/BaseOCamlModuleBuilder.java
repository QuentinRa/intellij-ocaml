package com.ocaml.ide.wizard;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectTemplate;
import com.ocaml.ide.module.OCamlModuleType;
import com.ocaml.ide.wizard.templates.OCamlTemplateProvider;
import com.ocaml.ide.wizard.templates.TemplateBuildInstructions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.function.Supplier;

/**
 * Convenient implementation to use the code in
 * the builder of minor IDEs.
 */
public class BaseOCamlModuleBuilder extends ModuleBuilder {

    private Supplier<String> contentEntryPath;

    @Override
    public ModuleType<OCamlModuleBuilder> getModuleType() {
        return new OCamlModuleType();
    }

    @Nullable @Override public String getContentEntryPath() {
        return contentEntryPath.get();
    }

    public void setupRootModel(@NotNull ModifiableRootModel rootModel,
                               Supplier<Sdk> sdkSupplier, Supplier<String> contentEntryPath,
                               ProjectTemplate template) {
        this.contentEntryPath = contentEntryPath;
        // Create the files/folders
        ContentEntry contentEntry = doAddContentEntry(rootModel);
        System.out.println("ce:"+contentEntry);
        if (contentEntry != null) {
            // Get instructions
            TemplateBuildInstructions instructions;
            if (template instanceof TemplateBuildInstructions)
                instructions = (TemplateBuildInstructions) template;
            else instructions = OCamlTemplateProvider.getDefaultInstructions();

            // create the source folder
            final String sourcePath = getContentEntryPath() + File.separator + instructions.getSourceFolderName();
            //noinspection ResultOfMethodCallIgnored
            new File(sourcePath).mkdirs();
            final VirtualFile sourceRoot = LocalFileSystem.getInstance()
                    .refreshAndFindFileByPath(FileUtil.toSystemIndependentName(sourcePath));
            if (sourceRoot != null) {
                contentEntry.addSourceFolder(sourceRoot, false, "");
                // create the files
                Sdk sdk = sdkSupplier.get();
                instructions.createFiles(rootModel, sourceRoot, sdk == null ? null : sdk.getHomePath());
                // refresh
                ApplicationManager.getApplication().runWriteAction(() -> sourceRoot.refresh(true, true));
            }
        }
    }
}
