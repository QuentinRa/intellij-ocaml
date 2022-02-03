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
            if (c.startsWith("Warning 27: unused variable")) annotation.toUnusedVariable();
            else if (c.startsWith("Warning 70: Cannot find interface file.")) annotation.toMliMissing();
            else if (c.startsWith("Warning 39: unused rec flag.")) annotation.toUnusedRec();
            else if (c.startsWith("Warning 3: deprecated:")) annotation.toDeprecated();
            else if (c.startsWith("Warning 24: bad source file name")) annotation.toBadModuleName();
            else if (c.startsWith("Warning 11: this match case is unused.")) annotation.toUnusedMatchCase();
            else if (c.startsWith("Warning 32: unused value")) annotation.toUnusedValue();
            else if (c.startsWith("Warning 34: unused type")) annotation.toUnusedType();
            else if (c.startsWith("Warning 60: unused module")) annotation.toUnusedModule();
            else if (c.startsWith("Warning 67: unused functor parameter")) annotation.toUnusedFunctorParameter();
            else if (c.startsWith("Warning 33: unused open")) annotation.toUnusedOpen();
        }

        else if (annotation.isError()) {
            if (c.startsWith("Error: Unbound")) annotation.toUnbound();
            else if (c.startsWith("Error: The implementation") && c.contains("does not match the interface")) annotation.toMismatchMli();
        }

        return annotation;
    }
}
