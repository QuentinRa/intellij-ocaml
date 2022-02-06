package com.ocaml.sdk.repl;

public interface OCamlREPLConstants {

    /**
     * Link to the documentation :)
     */
    String HELP_URL = "https://ocaml.org/releases/4.12/manual/toplevel.html?temporary_until_documentation_done";

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
