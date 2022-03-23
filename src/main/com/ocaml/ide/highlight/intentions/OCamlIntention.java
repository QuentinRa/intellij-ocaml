package com.ocaml.ide.highlight.intentions;

import com.intellij.build.FilePosition;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.ocaml.ide.highlight.intentions.IntentionActionBuilder;
import com.ocaml.ide.highlight.intentions.fixes.RenameVariableFix;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OCamlIntention {
    /* see CompilerOutputMessage */
    public @NotNull CompilerOutputMessage.Kind kind;
    public String context;
    public String header;
    public String content;

    public String file;
    public int startLine;
    public int startColumn;
    public int endLine;
    public int endColumn;

    public final ArrayList<IntentionActionBuilder> fixes = new ArrayList<>();
    public ProblemHighlightType highlightType;
    public boolean fileLevel;
    // must be true, otherwise, not shown in the console
    // nor in the top-right icon
    public boolean normalLevel = true;

    public OCamlIntention(@NotNull CompilerOutputMessage currentState) {
        kind = currentState.kind;
        context = currentState.context;
        header = content = currentState.content;

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

    public TextRangeInterval computePosition(Editor editor) {
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

    public boolean isError() {
        return kind == CompilerOutputMessage.Kind.ERROR;
    }

    public boolean isWarning() {
        return kind == CompilerOutputMessage.Kind.WARNING;
    }

    public boolean isAlert() {
        return kind == CompilerOutputMessage.Kind.ALERT;
    }

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

    // bunch of errors "mismatch"
    public void toMismatchMli() {
        fileLevel = true;
    }

    // 11, 27, 32, 33, 39, ...
    public void toUnused() {
        highlightType = ProblemHighlightType.LIKE_UNUSED_SYMBOL;
    }

    // 27
    public void toUnusedVariable() {
        toUnused();
        // rename variable to '_'
        fixes.add(new RenameVariableFix("_"));
    }

    // 70
    public void toMliMissing() {
        fileLevel = true;
        // everyone may want to suppress this warning
        // so, for now, as we can't suppress a warning
        // this is disabled
        normalLevel = false;
    }

    // 24
    public void toBadModuleName() {
        fileLevel = true;
    }

    // 11
    public void toUnusedMatchCase() {
        // we should mark the whole statement
        // -> everything until the '|' (easy)
        // -> or the next statement (hard?)
        toUnused();
    }

    public void toUnusedRec() {
        toUnused();
        // the cursor is on the variable, not on the rec
    }

    public void toUnusedValue() {
        toUnused();
        // may be a value (ex: 'x') or
        // todo: a definition (ex: 'val t : int')
        fixes.add(new RenameVariableFix("_"));
    }

    public void toUnusedType() {
        toUnused();
        // may be 'type t' or 'type t = ...'
    }

    public void toUnusedModule() {
        toUnused();
        // may be 'X' / ... a name followed by sig end
        // or module Name = struct end
    }

    public void toUnusedFunctorParameter() {
        toUnused();
        // always a name?
    }

    public void toUnusedOpen() {
        toUnused();
        // 'open Name'
    }
}
