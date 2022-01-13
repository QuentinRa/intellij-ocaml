package com.ocaml.ide.module;

import com.intellij.openapi.module.*;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.*;
import com.ocaml.*;
import com.ocaml.icons.*;
import com.ocaml.ide.projectWizard.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * Defines the name, description, and icon of the module.
 */
public class OCamlModuleType extends ModuleType<OCamlModuleBuilder> {

    private static final String OCAML_MODULE = "OCAML_MODULE";

    public OCamlModuleType() {
        super(OCAML_MODULE);
    }

    //
    // Builder
    //

    @NotNull @Override public OCamlModuleBuilder createModuleBuilder() {
        return new OCamlModuleBuilder();
    }

    //
    // Name, description, icon
    //

    @Override public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getName() {
        return OCamlBundle.message("module.name");
    }

    @Override public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return OCamlBundle.message("module.description");
    }

    @Override public @NotNull Icon getNodeIcon(boolean isOpened) {
        return OCamlIcons.Nodes.OCAML_MODULE;
    }

    //
    // Check if the SDK is valid
    // todo: may do something a bit more
    //
    @Override public boolean isValidSdk(@NotNull Module module, @Nullable Sdk projectSdk) {
        return true;
    }
}