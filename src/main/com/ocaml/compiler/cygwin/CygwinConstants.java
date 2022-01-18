package com.ocaml.compiler.cygwin;

public interface CygwinConstants {

    String CYGWIN_FOLDER = "cygwin64";
    /** Files.exists is false for ocaml, but it's true for ocaml.opt
     * ocamlc can't be executed too, so we got a bigger problem. **/
    String OCAMLC_OPT = ".opt.exe";

}
