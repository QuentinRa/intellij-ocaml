package com.ocaml.ide.wizard.templates;

import com.intellij.ide.util.projectWizard.AbstractModuleBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.ProjectTemplate;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.utils.files.OCamlFileUtils;
import com.ocaml.utils.logs.OCamlLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;

/**
 * Allow the creation of a dune project, which includes the following
 * - src
 * - src/hello_world.ml
 * - src/hello_world.mli
 * - src/test_hello_world.ml
 * - src/dune
 * - dune-project
 */
class OCamlDuneTemplate implements ProjectTemplate, TemplateBuildInstructions {

    private static final Logger LOG = OCamlLogger.getTemplateInstance("makefile");

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull @NlsContexts.Label String getName() {
        return OCamlBundle.message("template.dune.title");
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @Nullable @NlsContexts.DetailedDescription String getDescription() {
        return OCamlBundle.message("template.dune.description");
    }

    @Override
    public Icon getIcon() {
        return OCamlIcons.External.DUNE;
    }

    @Override
    public @NotNull AbstractModuleBuilder createModuleBuilder() {
        throw new UnsupportedOperationException("OCamlDuneTemplate#createModuleBuilder should not be called");
    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    @Override
    public @Nullable ValidationInfo validateSettings() {
        return null;
    }

    @Override
    public void createFiles(ModifiableRootModel rootModel, VirtualFile sourceRoot) {
        File sourceFolder = VfsUtilCore.virtualToIoFile(sourceRoot);
        // Files
        OCamlFileUtils.createFile(sourceFolder, "hello_world.mli", "val hello_world : unit -> unit", LOG);
        OCamlFileUtils.createFile(sourceFolder, "hello_world.ml", "let hello_world () = Format.printf \"Hello, World!@.\"", LOG);
        OCamlFileUtils.createFile(sourceFolder, "test_hello_world.ml", "open Hello_world\n\nlet _ = hello_world ()", LOG);
        // Dune
        File rootFolder = sourceFolder.getParentFile();
        OCamlFileUtils.createFile(sourceFolder, "dune", "(executable\n (name test_hello_world))", LOG);
        OCamlFileUtils.createFile(rootFolder, "dune-project", "(lang dune 2.9)", LOG);
    }
}
