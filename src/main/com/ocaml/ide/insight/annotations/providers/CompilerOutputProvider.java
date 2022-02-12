package com.ocaml.ide.insight.annotations.providers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public interface CompilerOutputProvider {

    /**
     * @param file the psiFile associated with the current version of the file
     * @param editor the editor
     * @param homePath the path to the SDK home
     * @param moduleRootManager can be used to determine another output folder, or to get the sources' folders.
     * @param outputFolder the default value for the output folder
     * @return Collected information that will be used to compile and generate annotations
     * using {@link #doAnnotate}.
     */
    CollectedInfo collectInformation(@NotNull PsiFile file, @NotNull Editor editor,
                                     String homePath, @NotNull ModuleRootManager moduleRootManager,
                                     String outputFolder);

    /**
     * Compile the files
     * @param collectedInfo the collected information
     * @param log can be used to log warnings/errors
     * @return the annotation results
     */
    AnnotationResult doAnnotate(CollectedInfo collectedInfo, Logger log);
}
