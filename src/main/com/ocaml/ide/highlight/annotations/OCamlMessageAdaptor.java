package com.ocaml.ide.highlight.annotations;

import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;

public class OCamlMessageAdaptor {

    public static @NotNull OCamlAnnotation temper(CompilerOutputMessage currentState) {
        OCamlAnnotation annotation = new OCamlAnnotation(currentState);

        return annotation;
    }
}
