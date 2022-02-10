package com.ocaml.ide.highlight.annotations;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * We need to transform our message into an annotation that we will use in
 * the IDE. We may have to set some fields, etc., that are not done by the parser
 * (SOLID -> the parser is only supposed to parse the file, not tempering the result IMO).
 */
public final class OCamlMessageAdaptor {
    private OCamlMessageAdaptor() {
    }

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
            // added 26 because for unused (let z = first) in function not highlighted
            if (c.startsWith("Warning 26: unused variable") || c.startsWith("Warning 27: unused variable"))
                annotation.toUnusedVariable();
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
            else if (c.startsWith("Error: The implementation") && c.contains("does not match the interface"))
                annotation.toMismatchMli();
        }

        return annotation;
    }

    /**
     * Convert paths such as "C:/.../out/.../file.ml", or "/mnt/c/.../out/.../file.ml",
     * to ".../file.ml" (resp. .mli)
     *
     * @param message                a message with paths that may be tempered
     * @param rootFolderForTempering the path to out ("C:/.../out/") that was used by the compiler
     * @return the message with relative paths
     */
    @Contract(pure = true)
    public static @NotNull String temperPaths(@NotNull String message, @NotNull String rootFolderForTempering) {
        Pattern pattern = Pattern.compile(
                rootFolderForTempering.replace("\\", "\\\\") + "([^.]*).mli?"
        );
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String match = matcher.group();
            message = message.replace(match, FileUtil.toSystemIndependentName(match.replace(rootFolderForTempering, "")));
        }
        return message;
    }
}
