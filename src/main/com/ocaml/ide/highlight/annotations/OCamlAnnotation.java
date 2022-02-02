package com.ocaml.ide.highlight.annotations;

import com.intellij.build.FilePosition;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;

public class OCamlAnnotation {
    /* see CompilerOutputMessage */
    public @NotNull CompilerOutputMessage.Kind kind;
    public String context;
    public String content;

    public String file;
    public int startLine;
    public int startColumn;
    public int endLine;
    public int endColumn;
    public ProblemHighlightType highlightType;

    public OCamlAnnotation(@NotNull CompilerOutputMessage currentState) {
        kind = currentState.kind;
        context = currentState.context;
        content = currentState.content;

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

    public TextRangeInterval computePosition(@NotNull Editor editor) {
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
    public void toDeprecated() {
        highlightType = ProblemHighlightType.LIKE_DEPRECATED;
    }
}
