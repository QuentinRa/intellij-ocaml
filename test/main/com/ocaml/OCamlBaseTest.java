package com.ocaml;

import com.intellij.testFramework.fixtures.*;

public class OCamlBaseTest extends BasePlatformTestCase {

    // assuming that ocaml IS installed on this path
    protected static final String V_OCAML_BINARY_WSL = "\\\\wsl$\\Debian\\bin\\ocaml";
    protected static final String V_OCAML_COMPILER_WSL = "\\\\wsl$\\Debian\\bin\\ocamlc";
    protected static final String V_OCAML_VERSION_WSL = "4.12.0";
    protected static final String V_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Debian\\usr\\lib\\ocaml";

    // assuming that ocaml IS NOT installed on this path
    protected static final String I_NAME_WSL = "Fedora";
    protected static final String I_OCAML_BINARY_WSL = "\\\\wsl$\\Fedora\\bin\\ocaml";
    protected static final String I_OCAML_COMPILER_WSL = "\\\\wsl$\\Fedora\\bin\\ocamlc";
    protected static final String I_OCAML_VERSION_WSL = "4.12.0";
    protected static final String I_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Fedora\\usr\\lib\\ocaml";

}
