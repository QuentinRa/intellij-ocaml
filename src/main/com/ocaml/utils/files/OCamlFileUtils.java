package com.ocaml.utils.files;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public final class OCamlFileUtils {

    /**
     * Load a file stored in resources.
     * The path must starts with a /.
     * The separators for the lines in the returned file will be \n.
     */
    public static @NotNull String loadFileContent(@NotNull String filePath, @Nullable Logger logger) {
        try {
            var url = OCamlFileUtils.class.getClassLoader().getResource(filePath.replaceFirst("/", ""));
            if (url == null) throw new IOException("Couldn't get URL for " + filePath);
            VirtualFile virtualFile = VfsUtil.findFileByURL(url);
            if (virtualFile == null) throw new IOException("Couldn't find file by URL for " + filePath);
            String text = VfsUtil.loadText(virtualFile);
            String[] split = text.split("\r\n");
            return String.join("\n", split);
        } catch (IOException e) {
            if (logger != null) logger.error("Error loading file " + filePath, e);
            return "";
        }
    }

    /**
     * Create a file with some text.
     */
    public static void createFile(@NotNull File sourceRootFile, @NotNull String fileName,
                                  @NotNull String text, @Nullable Logger logger) {
        try {
            File mainFile = new File(sourceRootFile, fileName);
            Files.write(mainFile.toPath(), new ArrayList<>(Arrays.asList(text.split("\n"))));
        } catch (IOException e) {
            if (logger != null) logger.error("Error creating file " + fileName, e);
        }
    }

    @Nullable
    public static File copyToTempFile(@NotNull File tempCompilationDirectory, @NotNull PsiFile psiFile,
                                      @NotNull String name, Logger logger) {
        File sourceTempFile;

        try {
            sourceTempFile = new File(tempCompilationDirectory, name);
            boolean created = sourceTempFile.createNewFile();
            // FIX avoid "deadlock" if the file already exists
            if (!created && !sourceTempFile.exists())
                throw new IOException("Could not create '" + sourceTempFile + "'.");
        } catch (IOException e) {
            logger.info("Temporary file creation failed", e); // log error but do not show it in UI
            return null;
        }

        try {
            FileUtil.writeToFile(sourceTempFile, new String(psiFile.getText().getBytes()),
                    // use the charset of the original file
                    psiFile.getVirtualFile().getCharset());
        } catch (IOException e) {
            // Sometimes, file is locked by another process, not a big deal, skip it
            logger.trace("Write failed: " + e.getLocalizedMessage());
            return null;
        }

        return sourceTempFile;
    }

    public static void deleteDirectory(@NotNull String file) {
        deleteDirectory(new File(file));
    }

    public static void deleteDirectory(@NotNull File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    deleteDirectory(subFile);
                }
            }
        }
        //noinspection ResultOfMethodCallIgnored
        file.delete();
    }
}
