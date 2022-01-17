package com.ocaml.compiler;

import java.util.regex.*;

public interface OCamlConstants {

    String LIB_FOLDER_LOCATION = "/lib/ocaml";
    String LIB_FOLDER_LOCATION_R = "lib/ocaml";
    String USR_LIB_FOLDER_LOCATION = "/usr/lib/ocaml";
    String USR_LIB_FOLDER_LOCATION_R = "usr/lib/ocaml";

    String OCAML_EXECUTABLE = "ocaml";
    String OCAML_COMPILER_EXECUTABLE = "ocamlc";
    String OCAMLC_VERSION = "-version";

    Pattern VERSION_PATH_REGEXP = Pattern.compile(".*/(\\d\\.\\d\\d(\\.\\d)?)/?.*");
    Pattern VERSION_REGEXP = Pattern.compile("(\\d\\.\\d\\d(\\.\\d)?)");
}
