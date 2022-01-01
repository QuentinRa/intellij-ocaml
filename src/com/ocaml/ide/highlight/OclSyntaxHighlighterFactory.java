package com.ocaml.ide.highlight;

import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;

public class OclSyntaxHighlighterFactory extends SyntaxHighlighterFactory {

    @Override public @NotNull SyntaxHighlighter getSyntaxHighlighter(@Nullable Project project,
                                                                     @Nullable VirtualFile virtualFile) {
        return new OclSyntaxHighlighter();
    }
}
