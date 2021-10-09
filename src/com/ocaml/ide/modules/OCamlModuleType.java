package com.ocaml.ide.modules;

import com.intellij.openapi.module.*;
import icons.*;
import org.jetbrains.annotations.*;

import javax.swing.*;

/**
 * Type of OCaml native project.
 */
public class OCamlModuleType extends ModuleType<OCamlModuleBuilder> {

    public static final String OCAML_MODULE_ID = "ocamlModuleType";
    public static final String OCAML_MODULE_NAME = "OCaml module";
    public static final String OCAML_MODULE_DESCRIPTION = "OCaml modules are used for native development";

    private static final OCamlModuleType INSTANCE = new OCamlModuleType();

    private OCamlModuleType() {
        super(OCAML_MODULE_ID);
    }

    @NotNull public static OCamlModuleType getInstance() {
        return INSTANCE;
    }

    @NotNull @Override public OCamlModuleBuilder createModuleBuilder() {
        return new OCamlModuleBuilder();
    }

    @NotNull @Override public String getName() {
        return OCAML_MODULE_NAME;
    }

    @NotNull @Override public String getDescription() {
        return OCAML_MODULE_DESCRIPTION;
    }

    @NotNull @Override public Icon getNodeIcon(boolean isOpened) {
        return ORIcons.OCL_MODULE;
    }
}