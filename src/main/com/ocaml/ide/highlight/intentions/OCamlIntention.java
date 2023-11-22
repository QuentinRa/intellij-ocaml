package com.ocaml.ide.highlight.intentions;

import com.intellij.build.FilePosition;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.impl.TextRangeInterval;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.highlight.intentions.fixes.DeleteElementBuilder;
import com.ocaml.ide.highlight.intentions.fixes.RenameVariableBuilder;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.sdk.output.CompilerOutputMessage;
import com.ocaml.utils.ComputeMethod;
import com.or.lang.OCamlTypes;
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
    private ComputeMethod<Position, Position> temperPosition = v -> v;

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

    private TextRangeInterval computePosition(Editor editor) {
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

    public Position getPosition(Editor editor, PsiFile file) {
        return temperPosition.call(new Position(file, computePosition(editor)));
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
        fixes.add(new RenameVariableBuilder("_"));
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
        // Hence we need to move the cursor on the rec
        temperPosition = position -> {
            PsiElement psiElement = position.start;
            // unwrap
            if (psiElement != null && psiElement.getNode().getElementType() == OCamlTypes.LIDENT)
                psiElement = psiElement.getParent();
            // search
            while (psiElement != null && psiElement.getNode().getElementType() != OCamlTypes.REC) {
                psiElement = OCamlPsiUtils.skipMeaninglessPreviousSibling(psiElement);
            }
            // move
            if (psiElement != null) {
                int start = psiElement.getTextOffset();
                int len = psiElement.getTextLength();
                return new Position(position.file, new TextRangeInterval(start, start+len));
            }
            return position;
        };

        // delete rec
        fixes.add(new DeleteElementBuilder());
    }

    public void toUnusedValue() {
        toUnused();
        fixes.add(new RenameVariableBuilder("_"));
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

    /**
     * Position of the start/end
     */
    public final class Position {
        public final TextRangeInterval range;
        public final PsiElement start;
        public final PsiElement end;
        public final PsiFile file;

        public Position(PsiFile f, TextRangeInterval r) {
            PsiElement s = null;
            PsiElement e = null;

            if (r != null && !fileLevel) {
                s = f.findElementAt(r.intervalStart());
                e = f.findElementAt(r.intervalEnd()-1);
//                System.out.println(" was found-s:"+s+" ("+(s == null ? "<null>" : s.getText())+")");
//                System.out.println(" was found-e:"+e+" ("+(e == null ? "<null>" : e.getText())+")");
                if (s == e) e = null;
            }

            range = r;
            start = s;
            end = e;
            file = f;
        }
    }
}
