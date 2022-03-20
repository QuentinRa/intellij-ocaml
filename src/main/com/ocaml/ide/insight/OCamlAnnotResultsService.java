package com.ocaml.ide.insight;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.ocaml.sdk.annot.OCamlAnnotParser;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import com.ocaml.utils.editor.LogicalSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public final class OCamlAnnotResultsService {
    public final HashMap<String, HashMap<LogicalSection, OCamlInferredSignature>> results = new HashMap<>();
    public final HashMap<String, HashMap<TextRange, OCamlInferredSignature>> resultsAlt = new HashMap<>();

    /**
     * Update the list of annotation of a file, given its associated annotFile
     * @param file      the file associated with the annotFile
     * @param annotFile the annot file associated with the file
     */
    public void updateForFile(String file, File annotFile) {
        try (BufferedReader r = new BufferedReader(new FileReader(annotFile))) {
            // read
            StringBuilder b = new StringBuilder();
            String s;
            while ((s = r.readLine()) != null) b.append(s).append('\n');
            // parse
            OCamlAnnotParser parser = new OCamlAnnotParser(b.toString());
            results.put(file, parser.getIndexedByPosition());
            resultsAlt.put(file, new HashMap<>()); // reset and create
        } catch (IOException | IllegalStateException e) {
            // todo: log
            // may occur if the file is removed because a new one will be generated
            System.out.println("warn:" + e);
        }
    }

    /**
     * Clear results for a file, for example, if they are assumed
     * to be invalid.

     * @param path the file that was provided in updateFile
     */
    public void clearForFile(String path) {
        results.remove(path);
    }

    // we cannot use getLogicalPosition, so we can't use the first HashMap
    @Deprecated(forRemoval = true)
    public @Nullable OCamlInferredSignature findAnnotationFor(@NotNull PsiElement element) {
        return findAnnotationFor(element, true);
    }

    // we cannot use getLogicalPosition, so we can't use the first HashMap
    public @Nullable OCamlInferredSignature findAnnotationFor(@NotNull PsiElement element, boolean shouldLoadIfNeeded) {
        // else, use the "normal" way
        String path = element.getContainingFile().getVirtualFile().getPath();
        HashMap<TextRange, OCamlInferredSignature> rangeToSignature = resultsAlt.get(path);
        // should not occur
        if (rangeToSignature == null) return null;
        // return annotation
        OCamlInferredSignature signature = rangeToSignature.get(element.getTextRange());
        // may load
        if (signature == null && shouldLoadIfNeeded) {
            signature = getInfoForElement(element);
        }
        return signature;
    }

    private @Nullable LogicalSection getLogicalPosition(@NotNull PsiElement element) {
        Editor fileEditor = FileEditorManager.getInstance(element.getProject()).getSelectedTextEditor();
        if (!(fileEditor instanceof EditorEx)) return null;
        EditorEx editor = (EditorEx) fileEditor;
        int textOffset = element.getTextOffset();
        LogicalPosition start = editor.offsetToLogicalPosition(textOffset);
        LogicalPosition end = editor.offsetToLogicalPosition(textOffset + element.getTextLength());
        return new LogicalSection(start, end);
    }

    public boolean hasInfoForElement(@NotNull PsiElement element) {
        return findAnnotationFor(element, true) != null;
    }

    private @Nullable OCamlInferredSignature getInfoForElement(@NotNull PsiElement element) {
        String path = element.getContainingFile().getVirtualFile().getPath();
        HashMap<LogicalSection, OCamlInferredSignature> rangeToSignature = results.get(path);

        // compiled?
        if (rangeToSignature == null) return null;

        // get logical position
        LogicalSection logicalSection;

        // Tricks to get the logicalSection when we can't ask the editor
        if (!ApplicationManager.getApplication().isDispatchThread()) {
            Document document = element.getContainingFile().getViewProvider().getDocument();
            int textOffset = element.getTextOffset();
            int textOffsetEnd = textOffset + element.getTextLength();
            int lineStart = document.getLineNumber(textOffset);
            int lineEnd = document.getLineNumber(textOffsetEnd);
            int offsetStart = document.getLineStartOffset(lineStart);

            int startColumn = textOffset-offsetStart;
            String[] lines = element.getText().split("\n");
            int endColumn;
            if (lines.length <= 1) { // one line
                endColumn = startColumn + element.getTextLength();
            } else {
                endColumn = lines[lines.length-1].length();
            }

            logicalSection = new LogicalSection(
                    lineStart + 1, startColumn,
                    lineEnd + 1, endColumn);
        } else {
            // get logical position
            logicalSection = getLogicalPosition(element);
        }

        System.out.println("  "+logicalSection);
        if (logicalSection == null) return null;

        // get signature
        OCamlInferredSignature signature = rangeToSignature.get(logicalSection);
        if (signature == null) return null;

        // re-index by range, properly this time
        HashMap<TextRange, OCamlInferredSignature> ranges = resultsAlt.get(path);
        signature.range = element.getTextRange();
        ranges.put(element.getTextRange(), signature);
        resultsAlt.put(path, ranges);
        return signature;
    }
}
