package com.ocaml.utils.files;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Utilities to create temporary directories/files.
 */
public final class OCamlTempDirUtils {

    public static @NotNull File getOrCreateTempDirectory(@NotNull Project project, Logger LOG) {
        File result;

        String directoryName = "OCAML_" + project.getName().replaceAll(" ", "_");
        String tempDirectory = FileUtil.getTempDirectory();
        result = new File(tempDirectory, directoryName);
        if (!result.exists()) {
            try {
                result = FileUtil.createTempDirectory(directoryName, null, true);
                LOG.trace("Created temporary annotator directory: "+result.getAbsolutePath());
            } catch (IOException e) {
                LOG.error("Failed to create temporary directory", e);
                throw new RuntimeException(e);
            }
        }

        return result;
    }

    public static void cleanTempDirectory(@NotNull File directory, @NotNull String fileName) {
        File[] files = directory.listFiles((dir, name) -> name.startsWith(fileName) && !name.endsWith(".cmt"));
        if (files != null) Arrays.stream(files).parallel().forEach(FileUtil::asyncDelete);
    }

    @Nullable
    public static File copyToTempFile(@NotNull File tempCompilationDirectory, @NotNull PsiFile psiFile,
                                      @NotNull String nameWithoutExtension, Logger LOG) {
        File sourceTempFile;

        try {
            sourceTempFile = FileUtil.createTempFile(tempCompilationDirectory, nameWithoutExtension, "." + psiFile.getVirtualFile().getExtension());
        } catch (IOException e) {
            LOG.info("Temporary file creation failed", e); // log error but do not show it in UI
            return null;
        }

        try {
            FileUtil.writeToFile(sourceTempFile, psiFile.getText().getBytes());
        } catch (IOException e) {
            // Sometimes, file is locked by another process, not a big deal, skip it
            LOG.trace("Write failed: " + e.getLocalizedMessage());
            return null;
        }

        return sourceTempFile;
    }
}
