package com.ocaml;

import com.intellij.testFramework.fixtures.*;

public class OCamlBaseTest extends BasePlatformTestCase {

    protected static final String V_OCAML_HOME_WSL = "\\\\wsl$\\Debian\\home\\calistro\\.opam\\4.07.0";
    protected static final String I_OCAML_HOME_WSL = "\\\\wsl$\\Debian\\home\\calistro\\.opam\\0.00.0";
    protected static final String V_OCAML_HOME_WIN = "C:\\cygwin64\\home\\quent\\.opam\\4.08.0";
    protected static final String I_OCAML_HOME_WIN = "C:\\cygwin64\\home\\quent\\.opam\\0.00.0";

    // assuming that ocaml IS installed on this path
    protected static final String V_OCAML_BINARY_WSL = "\\\\wsl$\\Debian\\bin\\ocaml";
    protected static final String V_OCAML_COMPILER_WSL = "\\\\wsl$\\Debian\\bin\\ocamlc";
    protected static final String V_OCAML_VERSION_WSL = "4.12.0";
    protected static final String V_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Debian\\usr\\lib\\ocaml";

    // assuming that ocaml IS NOT installed on this path
    protected static final String I_OCAML_BINARY_WSL = "\\\\wsl$\\Debian\\home\\bin\\ocaml";
    protected static final String I_OCAML_COMPILER_WSL = "\\\\wsl$\\Debian\\home\\bin\\ocamlc";
    protected static final String I_OCAML_VERSION_WSL = "4.12.0";
    protected static final String I_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Debian\\home\\usr\\lib\\ocaml";

    // assuming that this path is "valid", but the WSL distribution is invalid
    protected static final String N_NAME_WSL = "Fedora";
    protected static final String N_OCAML_BINARY_WSL = "\\\\wsl$\\Fedora\\bin\\ocaml";
    protected static final String N_OCAML_COMPILER_WSL = "\\\\wsl$\\Fedora\\bin\\ocamlc";
    protected static final String N_OCAML_VERSION_WSL = "4.12.0";
    protected static final String N_OCAML_SOURCES_FOLDER_WSL = "\\\\wsl$\\Fedora\\usr\\lib\\ocaml";

    // assuming that ocaml IS installed on this path
    protected static final String V_OCAML_BINARY_WIN = "C:\\cygwin64\\bin\\ocaml.exe";
    protected static final String V_OCAML_COMPILER_WIN = "C:\\cygwin64\\bin\\ocamlc.exe";
    protected static final String V_OCAML_COMPILER_OPT_WIN = "C:\\cygwin64\\bin\\ocamlc.opt.exe";
    protected static final String V_OCAML_VERSION_WIN = "4.10.0";
    protected static final String V_OCAML_SOURCES_FOLDER_WIN = "C:\\cygwin64\\lib\\ocaml";

    // assuming that ocaml IS NOT installed on this path
    protected static final String I_OCAML_BINARY_WIN = "C:\\cygwin\\bin\\ocaml.exe";
    protected static final String I_OCAML_COMPILER_WIN = "C:\\cygwin\\bin\\ocamlc.exe";
    protected static final String I_OCAML_VERSION_WIN = "4.10.0";
    protected static final String I_OCAML_SOURCES_FOLDER_WIN = "C:\\cygwin\\lib\\ocaml";

}
