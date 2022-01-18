package com.ocaml.ide.projectWizard.templates;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.*;
import com.intellij.platform.*;
import com.ocaml.*;
import com.ocaml.icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * Allow the creation of a project with
 * - src
 * - src/hello_world.ml
 * - src/hello_world.mli
 * - src/test_hello_world.ml
 * - Makefile
 */
class OCamlMakefileTemplate implements ProjectTemplate, TemplateBuildInstructions {

    @SuppressWarnings("UnstableApiUsage") @Override public @NotNull @NlsContexts.Label String getName() {
        return OCamlBundle.message("template.makefile.title");
    }

    @SuppressWarnings("UnstableApiUsage") @Override public @Nullable @NlsContexts.DetailedDescription String getDescription() {
        return OCamlBundle.message("template.makefile.description");
    }

    @Override public Icon getIcon() {
        return OCamlIcons.External.Markdown;
    }

    @Override public @NotNull AbstractModuleBuilder createModuleBuilder() {
        throw new UnsupportedOperationException("OCamlMakefileTemplate#createModuleBuilder should not be called");
    }

    @SuppressWarnings({"deprecation", "RedundantSuppression"})
    @Override public @Nullable ValidationInfo validateSettings() {
        return null;
    }

    @Override public void createFiles(ModifiableRootModel rootModel, VirtualFile sourceRoot) {

    }
}
