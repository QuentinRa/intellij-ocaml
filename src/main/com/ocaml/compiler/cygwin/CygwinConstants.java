package com.ocaml.compiler.cygwin;

import com.intellij.util.*;

public interface CygwinConstants {

    String CYGWIN_FOLDER = "cygwin64";
    /** Files.exists is false for ocaml, but it's true for ocaml.opt
     * ocamlc can't be executed too, so we got a bigger problem. **/
    String OCAMLC_OPT = ".opt.exe";

    /** @return the usual path to the opam folder on cygwin */
    static String getOpamFolder() {
        return "cygwin64/home/"+ SystemProperties.getUserName()+"/.opam";
    }
}
