package com.ocaml.ide.highlight.annotations;

import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;

/**
 * We need to transform our message into an annotation that we will use in
 * the IDE. We may have to set some fields, etc., that are not done by the parser
 * (SOLID -> the parser is only supposed to parse the file, not tempering the result IMO).
 */
public class OCamlMessageAdaptor {

    public static @NotNull OCamlAnnotation temper(CompilerOutputMessage currentState) {
        OCamlAnnotation annotation = new OCamlAnnotation(currentState);
        String c = annotation.content;

        if (annotation.isAlert()) {
            if (c.startsWith("Alert deprecated:")) annotation.toDeprecated();
        }

        else if (annotation.isWarning()) {
            if (c.startsWith("Warning 3: deprecated:")) annotation.toDeprecated();
        }

        return annotation;
    }
}
