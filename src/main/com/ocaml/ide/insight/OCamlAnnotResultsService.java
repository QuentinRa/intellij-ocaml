package com.ocaml.ide.insight;

import com.intellij.psi.PsiElement;
import com.ocaml.sdk.annot.OCamlAnnotParser;
import com.ocaml.sdk.annot.OCamlInferredSignature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// todo: handle empty file
// todo: fix problem with type type?
public final class OCamlAnnotResultsService {
    public final HashMap<String, List<OCamlInferredSignature>> results = new HashMap<>();

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
            ArrayList<OCamlInferredSignature> res = parser.get();
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
        List<OCamlInferredSignature> inferredSignatures = results.get(path);
        if (inferredSignatures == null) return null;
        // todo: log ?
//        System.out.println("r:"+element.getTextRange());
        for (OCamlInferredSignature inferredSignature : inferredSignatures) {
            // todo: log ?
//            System.out.println("testing with "+inferredSignature);
            if (inferredSignature.range.contains(element.getTextRange()))
                return inferredSignature;
        }
        return null;
    }
}
