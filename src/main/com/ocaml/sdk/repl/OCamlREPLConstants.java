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
     * Types are starting with this symbol
     */
    String TYPE = "type";

    /**
     * Functions are annotated with this
     */
    String FUN = "<fun>";

    /**
     * Variables are starting with this symbol
     */
    String VARIABLE = "val";

    /**
     * Exceptions are starting with this symbol
     */
    String EXCEPTION = "exception";

    /**
     * A module is starting with symbol, and
     * a module type too.
     */
    String MODULE = "module";

    /**
     * A module type is starting with this symbol.
     */
    String MODULE_TYPE = "module type";
}
