package com.ocaml.ide.module;

import com.intellij.openapi.module.ModuleType;
import com.ocaml.OCamlBundle;
import com.ocaml.icons.OCamlIcons;
import com.ocaml.ide.wizard.OCamlModuleBuilder;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public OCamlModuleBuilder createModuleBuilder() {
        return new OCamlModuleBuilder();
    }

    //
    // Name, description, icon
    //

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Title) String getName() {
        return OCamlBundle.message("ocaml.module");
    }

    @Override
    public @NotNull @Nls(capitalization = Nls.Capitalization.Sentence) String getDescription() {
        return OCamlBundle.message("ocaml.module.description");
    }

    @Override
    public @NotNull Icon getNodeIcon(boolean isOpened) {
        return OCamlIcons.Nodes.OCAML_MODULE;
    }
}