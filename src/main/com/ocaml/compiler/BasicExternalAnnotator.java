package com.ocaml.compiler;

import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandlerFactory;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.ocaml.ide.insight.annotations.OCamlMessageAdaptor;
import com.ocaml.ide.highlight.intentions.CompilerOutputProvider;
import com.ocaml.lang.utils.OCamlResolveDependencies;
import com.ocaml.sdk.output.CompilerOutputMessage;
import com.ocaml.sdk.output.CompilerOutputParser;
import com.ocaml.sdk.providers.OCamlSdkProvidersManager;
import com.ocaml.sdk.providers.utils.CompileWithCmtInfo;
import com.ocaml.utils.files.OCamlFileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * I originally named this class StupidExternalAnnotator, or DummyExternalAnnotator.
 * This one is used when no build system is present. Basically, I'm manually looking
 * for dependencies, and compiling everything using 'ocamlc' (one call per file,
 * .mli are compiled, not .ml, aside from the targetFile).
 */
public class BasicExternalAnnotator implements CompilerOutputProvider {

    public CollectedInfo collectInformation(@NotNull PsiFile file, @NotNull Editor editor,
                                            String homePath, @NotNull ModuleRootManager moduleRootManager,
                                            String outputFolder) {
        Project project = file.getProject();
        VirtualFile sourceFile = file.getVirtualFile();
        String targetFile = null;

        Set<Pair<String, PsiFile>> dependencies = OCamlResolveDependencies.resolveForFile(file, moduleRootManager);

        // fix: files may not be in a source folder
        // find the file we are trying to compile
        for (VirtualFile root : moduleRootManager.getContentRoots()) {
            boolean under = VfsUtil.isUnder(sourceFile, Set.of(root));
            if (!under) continue;
            // we are asking for the parent, because we want the root inside the relative path
            // ex: src/errors/mismatch/missing_impl/file.ml
            targetFile = VfsUtil.getRelativePath(sourceFile, root);
            break;
        }
        if (targetFile == null) return null;

        // look for .mli
        PsiFile mli = null;
        if (OCamlFileType.isFile(sourceFile.getName())) {
            VirtualFile mliV = sourceFile.getParent().findChild(OCamlInterfaceFileType.fromSource(sourceFile.getName()));
            if (mliV != null) mli = PsiManager.getInstance(project).findFile(mliV);
        }
        return new CollectedInfo(this, file, editor, homePath, targetFile, mli, dependencies, outputFolder);
    }

    @Override public ExternalCompilerResult doAnnotate(@NotNull CollectedInfo collectedInfo, Logger log) {
        // build/out folder
        File myOutputFolder = new File(collectedInfo.myOutputFolder);
        if (!myOutputFolder.exists() && !myOutputFolder.mkdirs()) {
            log.warn("Couldn't create '" + myOutputFolder + "'");
            return null;
        }
        // target folder
        File targetFolder = new File(myOutputFolder, collectedInfo.myTargetFile).getParentFile();
        if (!targetFolder.exists() && !targetFolder.mkdirs()) {
            log.warn("Couldn't create '" + targetFolder + "'");
            return null;
        }

        VirtualFile sourceFile = collectedInfo.mySourcePsiFile.getVirtualFile();

        // Creates a temporary file on disk with a copy of the current document.
        // We need to make a copy with the REAL content of the file, and work on this copy.
        // If we are working on the given file, we are not compiling/... the "latest" version,
        // as some changes may not have been committed.
        // FIX: Read access is allowed from inside read-action (or EDT) only (see com.intellij.openapi.application.Application.runReadAction())
        //noinspection RedundantSuppression
        @SuppressWarnings("deprecation")
        File sourceTempFile = ApplicationManager.getApplication().runReadAction((Computable<File>)
                () -> OCamlFileUtils.copyToTempFile(targetFolder, collectedInfo.mySourcePsiFile, sourceFile.getName(), log)
        );
        if (sourceTempFile == null) return null;

        try {
            for (Pair<String, PsiFile> pair : collectedInfo.myDependencies) {
                // target folder
                File localTargetFolder = new File(myOutputFolder, pair.first).getParentFile();
//               todo: System.out.println("lft" + localTargetFolder);
                if (!targetFolder.exists() && !targetFolder.mkdirs())
                    continue;
                compileFile(collectedInfo.myHomePath, localTargetFolder, myOutputFolder, pair.second, log);
            }

            // only if we are compiling a .ml,
            // and we got a .mli
            if (collectedInfo.myTargetMli != null) {
                compileFile(collectedInfo.myHomePath, targetFolder, myOutputFolder, collectedInfo.myTargetMli, log);
            }

            // get compiler
            CompileWithCmtInfo compiler = OCamlSdkProvidersManager.INSTANCE.getCompileCommandWithCmt(
                    collectedInfo.myHomePath,
                    myOutputFolder.getAbsolutePath(),
                    sourceTempFile.getPath(),
                    targetFolder.getAbsolutePath(),
                    sourceFile.getNameWithoutExtension()
            );
            if (compiler == null) {
                log.error("No cli found for " + collectedInfo.myHomePath + " (ml).");
                return null;
            }

            OSProcessHandler processHandler = ProcessHandlerFactory
                    .getInstance()
                    .createColoredProcessHandler(compiler.cli);
            Process process = processHandler.getProcess();
            processHandler.startNotify();

            ArrayList<CompilerOutputMessage> info = new ArrayList<>();
            CompilerOutputParser outputParser = new CompilerOutputParser() {
                @Override protected void onMessageReady(@NotNull CompilerOutputMessage message) {
                    // skipping this
                    // Error: Module `Hello_world' is unavailable (required by `Test_hello_world')
                    // because, we are using some shitty way to compile :(
                    // This class was originally called StupidExternalAnnotator x)
                    if (message.content.contains("is unavailable (required by")) return;
                    message.content = OCamlMessageAdaptor.temperPaths(message.content, compiler.rootFolderForTempering);
                    log.debug("added:" + message.header() + " (line->" + message.filePosition.getStartLine() + ")");
                    info.add(message);
                }
            };
            try (BufferedReader stdin = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = stdin.readLine()) != null) {
                    outputParser.parseLine(line);
                }
                // done
                outputParser.inputDone();
            } catch (IOException e) {
                // may occur if the file was removed, because it will be compiled again?
                log.warn("Reading '" + sourceFile.getName() + "' failed (" + e.getMessage() + ").");
                return null;
            }

            String nameWithoutExtension = sourceFile.getNameWithoutExtension();
            File annotFile = new File(targetFolder, nameWithoutExtension + "." + compiler.getAnnotationFileExtension());
            return new ExternalCompilerResult(info, collectedInfo.myEditor, annotFile);
        } catch (Exception e) {
            if (!(e instanceof ProcessCanceledException))
                log.error("Error while processing annotations", e);
            return null;
        }
    }

    private void compileFile(@NotNull String homePath, @NotNull File targetFolder,
                             @NotNull File outputFolder, @NotNull PsiFile targetPsi,
                             @NotNull Logger log) throws Exception {
        AtomicReference<File> tempFile = new AtomicReference<>();
        ApplicationManager.getApplication().runReadAction(() -> tempFile.set(
                OCamlFileUtils.copyToTempFile(targetFolder, targetPsi, targetPsi.getName(), log)
        ));

        // compile .mli
        if (tempFile.get() != null) {
            // get compiler
            CompileWithCmtInfo compiler = OCamlSdkProvidersManager.INSTANCE.getCompileCommandWithCmt(
                    homePath,
                    outputFolder.getAbsolutePath(),
                    tempFile.get().getPath(),
                    targetFolder.getAbsolutePath(),
                    targetPsi.getVirtualFile().getNameWithoutExtension()
            );
            if (compiler == null) {
                log.error("No cli found for " + homePath + " (mli).");
                return;
            }
            Process process = compiler.cli.createProcess();
            process.waitFor(); // wait
        }
    }
}
