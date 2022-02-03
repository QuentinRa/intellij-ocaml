package com.ocaml.ide.highlight.annotations;

import com.intellij.build.FilePosition;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.intellij.psi.*;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class OCamlAnnotation {
    /* see CompilerOutputMessage */
    public @NotNull CompilerOutputMessage.Kind kind;
    public String context;
    public String content;
    public @Nullable PsiFile psiFile;
    public @Nullable Editor editor;

    public String file;
    public int startLine;
    public int startColumn;
    public int endLine;
    public int endColumn;
    public ProblemHighlightType highlightType;
    public final ArrayList<IntentionAction> fixes = new ArrayList<>();
    public boolean fileLevel;

    public OCamlAnnotation(@NotNull CompilerOutputMessage currentState,
                           @Nullable PsiFile psi,
                           @Nullable Editor e) {
        kind = currentState.kind;
        context = currentState.context;
        content = currentState.content;
        psiFile = psi;
        editor = e;

        // easier access
        FilePosition filePosition = currentState.filePosition;
        file = filePosition.getFile().getAbsolutePath();
        startLine = filePosition.getStartLine() - 1; // start with 0
        endLine = filePosition.getEndLine() - 1; // start with 0
        startColumn = filePosition.getStartColumn();
        endColumn = filePosition.getEndColumn();

        if (startColumn == -1) startColumn = 0;
        if (endColumn == -1) endColumn = 1;
    }

    public TextRangeInterval computePosition() {
        if (editor == null) return null; // no editor, no job
        // position
        LogicalPosition start = new LogicalPosition(startLine, startColumn);
        LogicalPosition end = new LogicalPosition(endLine, endColumn);
        int startOffset = editor.isDisposed() ? 0 : editor.logicalPositionToOffset(start);
        int endOffset = editor.isDisposed() ? 0 : editor.logicalPositionToOffset(end);

        // it does not make any sense, it must be a syntax error/something that is
        // after the end of the file
        if (startOffset == endOffset) return null;
        return new TextRangeInterval(startOffset, endOffset);
    }

    public boolean isError() { return kind == CompilerOutputMessage.Kind.ERROR; }
    public boolean isWarning() { return kind == CompilerOutputMessage.Kind.WARNING; }
    public boolean isAlert() { return kind == CompilerOutputMessage.Kind.ALERT; }

    public boolean hasCustomHighLightType() {
        return highlightType != null;
    }

    // utils

    // alert: deprecated:
    public void toDeprecated() {
        highlightType = ProblemHighlightType.LIKE_DEPRECATED;
    }

    // error
    public void toUnbound() {
        highlightType = ProblemHighlightType.ERROR;
    }

    // 11, 27, 32, 33, 39, ...
    public void toUnused() {
        highlightType = ProblemHighlightType.LIKE_UNUSED_SYMBOL;
    }

    // 27
    public void toUnusedVariable() {
        toUnused();
    }

    // 70
    public void toMliMissing() {
        fileLevel = true;
    }

    // 24
    public void toBadModuleName() {
        fileLevel = true;
    }
}
