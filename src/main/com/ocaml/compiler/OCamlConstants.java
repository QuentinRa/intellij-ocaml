package com.ocaml.compiler;

import java.util.regex.*;

public interface OCamlConstants {

    /** ABSOLUTE PATH TO THE LIB FOLDER (1/2) **/
    String LIB_FOLDER_LOCATION = "/lib/ocaml";
    /** RELATIVE PATH TO THE LIB FOLDER (1/2) **/
    String LIB_FOLDER_LOCATION_R = "lib/ocaml";
    /** ABSOLUTE PATH TO THE LIB FOLDER (2/2) **/
    String USR_LIB_FOLDER_LOCATION = "/usr/lib/ocaml";
    /** RELATIVE PATH TO THE LIB FOLDER (2/2) **/
    String USR_LIB_FOLDER_LOCATION_R = "usr/lib/ocaml";

    String OCAML_EXECUTABLE = "ocaml";
    String OCAML_COMPILER_EXECUTABLE = "ocamlc";
    String OCAMLC_VERSION = "-version";

    Pattern VERSION_PATH_REGEXP = Pattern.compile(".*/(\\d\\.\\d\\d(\\.\\d)?)/?.*");
    Pattern VERSION_REGEXP = Pattern.compile("(\\d\\.\\d\\d(\\.\\d)?)");

    /** including the leading '.' **/
    String FILE_EXTENSION = ".ml";
    String FILE_INTERFACE_EXTENSION = ".mli";
}
