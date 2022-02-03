package com.ocaml.ide.highlight.annotations;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * We need to transform our message into an annotation that we will use in
 * the IDE. We may have to set some fields, etc., that are not done by the parser
 * (SOLID -> the parser is only supposed to parse the file, not tempering the result IMO).
 */
public class OCamlMessageAdaptor {
    public static @NotNull OCamlAnnotation temper(CompilerOutputMessage currentState) {
        return temper(currentState, null, null);
    }

    public static @NotNull OCamlAnnotation temper(CompilerOutputMessage currentState,
                                                  @Nullable PsiFile file,
                                                  @Nullable Editor editor) {
        OCamlAnnotation annotation = new OCamlAnnotation(currentState, file, editor);
        String c = annotation.content;

        if (annotation.isAlert()) {
            if (c.startsWith("Alert deprecated:")) annotation.toDeprecated();
        }

        else if (annotation.isWarning()) {
            if (c.startsWith("Warning 3: deprecated:")) annotation.toDeprecated();
            else if (c.startsWith("Warning 27: unused variable")) annotation.toUnusedVariable();
            else if (c.startsWith("Warning 70: Cannot find interface file.")) annotation.toMliMissing();
            else if (c.startsWith("Warning 24: bad source file name")) annotation.toBadModuleName();
        }

        else if (annotation.isError()) {
            if (c.startsWith("Error: Unbound")) annotation.toUnbound();
        }

        return annotation;
    }
}
