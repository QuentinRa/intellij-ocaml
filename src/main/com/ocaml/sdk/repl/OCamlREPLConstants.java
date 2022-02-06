package com.ocaml.sdk.repl;

public interface OCamlREPLConstants {

    /**
     * In the toplevel, every line must ends with ";;"
     */
    String END_LINE = ";;";

    /**
     * In the toplevel, unless disabled, this is the default prompt.
     */
    String PROMPT = "#";

    /**
     * Functions are annotated with this
     */
    String FUN = "<fun>";
}
