package com.ocaml.compiler.opam;

public interface OpamConstants {

    String SOURCES_FOLDER = ".opam-switch/sources/";
    /**
     * Files.exists is false for ocaml, but it's true for ocaml.opt
     **/
    String OCAMLC_OPT = ".opt";

}
