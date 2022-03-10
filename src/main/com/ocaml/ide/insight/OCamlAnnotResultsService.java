package com.ocaml.ide.insight;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.ocaml.sdk.annot.OCamlAnnotParser;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.PsiUpperSymbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public final class OCamlAnnotResultsService {
    public final HashMap<String, HashMap<TextRange, OCamlInferredSignature>> results = new HashMap<>();

    /**
     * Update the list of annotation of a file, given its associated annotFile
     * @param file the file associated with the annotFile
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
            HashMap<TextRange, OCamlInferredSignature> res = parser.getIndexedByRange();
            results.put(file, res);
        } catch (IOException | IllegalStateException e) {
            // todo: log
            // may occur if the file is removed because a new one will be generated
            System.out.println("warn:"+e);
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

    public @Nullable OCamlInferredSignature findAnnotationFor(@NotNull PsiElement element) {
        String path = element.getContainingFile().getVirtualFile().getPath();
        HashMap<TextRange, OCamlInferredSignature> rangeToSignature = results.get(path);
        // should not occur
        if (rangeToSignature == null) return null;
        // return annotation
        OCamlInferredSignature signature = rangeToSignature.get(element.getTextRange());
        if (signature == null) { // find another target
            TextRange textRange = findTextRange(element);
            signature = rangeToSignature.get(textRange);
        }
        return signature;
    }

    public boolean hasInfoForElement(@NotNull PsiElement element) {
        String path = element.getContainingFile().getVirtualFile().getPath();
        HashMap<TextRange, OCamlInferredSignature> rangeToSignature = results.get(path);
        // not compiled
        if (rangeToSignature == null) return false;
        // easy case
        if (rangeToSignature.containsKey(element.getTextRange())) return true;
        // try to use some code to find the possible target
        TextRange textRange = findTextRange(element);
        return textRange != null && rangeToSignature.containsKey(textRange);
    }

    private @Nullable TextRange findTextRange(@NotNull PsiElement element) {
        if (element.getNode().getElementType().equals(OCamlTypes.LIDENT)) {
            PsiElement dot = OCamlPsiUtils.getPreviousMeaningfulSibling(element.getParent(), OCamlTypes.DOT);
            if (dot != null) {
                PsiElement upperIdentifier = OCamlPsiUtils.skipMeaninglessPreviousSibling(dot);
                if (upperIdentifier instanceof PsiUpperSymbol) {
                    return new TextRange(upperIdentifier.getTextRange().getStartOffset(),
                            element.getTextRange().getEndOffset());
                }
            }
        }

        if (element.getParent() instanceof PsiUpperSymbol) {
            PsiElement dot = OCamlPsiUtils.getNextMeaningfulSibling(element.getParent(), OCamlTypes.DOT);
            if (dot == null) return element.getTextRange();
            else {
                PsiElement lowerIdentifier = OCamlPsiUtils.skipMeaninglessNextSibling(dot);
                if (lowerIdentifier instanceof PsiLowerSymbol) {
                    return new TextRange(element.getTextRange().getStartOffset(),
                            lowerIdentifier.getTextRange().getEndOffset());
                }
            }
        }

        return null;
    }
}
