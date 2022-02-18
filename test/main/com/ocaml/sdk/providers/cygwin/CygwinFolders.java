package com.ocaml.sdk.providers.cygwin;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.util.SystemProperties;
import com.ocaml.sdk.providers.BaseFolderProvider;
import com.ocaml.sdk.utils.OCamlSdkVersionManager;
import com.ocaml.sdk.utils.SdkInfo;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class CygwinFolders implements BaseFolderProvider {

    public static final String CYGWIN_FOLDER = "cygwin64";
    public static final String OCAML64_FOLDER = "OCaml64";

    private final String installationFolderName;
    public @Nullable SdkInfo BIN_VALID_SDK;
    public @Nullable SdkInfo BIN_CREATE_SDK;
    public @Nullable String BIN_VALID_EXE;
    public @Nullable String OPAM_HOME;
    public @Nullable SdkInfo OPAM_VALID_SDK;
    public @Nullable String OPAM_INVALID_BIN;

    public @Nullable String HOME_INVALID;
    public @Nullable String OCAML_BIN_INVALID;
    public @Nullable String OCAML_BIN_INVALID_NO_EXE;

    public CygwinFolders(String installationFolderName) {
        this.installationFolderName = installationFolderName;
        if(!SystemInfo.isWindows) return;
        // resolve the installation folder
        String cygwinRootFolder = null;
        Iterable<Path> fsRoots = FileSystems.getDefault().getRootDirectories();
        for (Path root : fsRoots) {
            Path cygwin64 = root.resolve(installationFolderName);
            if (!Files.exists(cygwin64)) continue;
            cygwinRootFolder = cygwin64.toFile().getAbsolutePath();
            break;
        }
        if (cygwinRootFolder == null) return;

        // fill binaries-related variables
        try {
            final String binaryPath = cygwinRootFolder+"\\bin\\ocaml.exe";

            GeneralCommandLine cli = new GeneralCommandLine(binaryPath, "-vnum");
            Process process = cli.createProcess();
            String version = new String(process.getInputStream().readAllBytes()).trim();
            process.waitFor();
            if (version.isEmpty() || process.exitValue() != 0)
                throw new ExecutionException("No version / switch.");

            String ocamlCompilerName = "ocamlc.opt.exe";

            // we may find ocamlc
            String ocamlPath = cygwinRootFolder+"\\bin\\ocamlc.exe";
            if (Files.exists(Path.of(ocamlPath))) ocamlCompilerName = "ocamlc.exe";

            // lib folder
            String libFolder = "\\lib\\ocaml";
            String libFolderPath = cygwinRootFolder+libFolder;
            if (!Files.exists(Path.of(libFolderPath))) {
                libFolder = "\\usr\\lib\\ocaml";
                libFolderPath = cygwinRootFolder + libFolder;
            }
            if (!Files.exists(Path.of(libFolderPath))) throw new ExecutionException("Library not found");

            /* a valid binary candidate for an SDK **/
            BIN_VALID_SDK = new SdkInfo(
                    cygwinRootFolder,
                    cygwinRootFolder+"\\bin\\ocaml.exe",
                    ocamlCompilerName,
                    version
            );

            BIN_CREATE_SDK = new SdkInfo(
                    null,
                    cygwinRootFolder+"\\bin\\ocaml.exe",
                    cygwinRootFolder+"\\bin\\"+ocamlCompilerName,
                    version,
                    cygwinRootFolder+libFolder
            );
        } catch (IOException | ExecutionException | InterruptedException ignore) {
        }

        /* a valid file that is not ocaml **/
        BIN_VALID_EXE = "C:\\cygwin64\\bin\\find.exe";

        try {
            String opamHome = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() +"\\.opam\\";
            Path opamHomePath = Path.of(opamHome);
            if (!Files.exists(opamHomePath)) throw new ExecutionException("Opam not installed");
            /* path to the opam folder **/
            OPAM_HOME = opamHome;

            File[] files = opamHomePath.toFile().listFiles();
            if (files == null || files.length == 0) throw new ExecutionException("Opam folder empty");

            for (File file : files) {
                String version = file.getName();
                if (!OCamlSdkVersionManager.isValid(version)) continue;

                String path = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\"+version+"\\";

                // we may find ocamlc
                String ocamlCompilerName = "ocamlc.opt.exe";
                String ocamlPath = path+"\\bin\\ocamlc.exe";
                if (Files.exists(Path.of(ocamlPath))) ocamlCompilerName = "ocamlc.exe";

                /* everything should be valid **/
                OPAM_VALID_SDK = new SdkInfo(
                        path,
                        "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\"+version+"\\bin\\ocaml.exe",
                        ocamlCompilerName,
                        version
                );

                /* expected: properly formatted path, non-existing SDK version **/
                OPAM_INVALID_BIN = "C:\\cygwin64\\home\\"+ SystemProperties.getUserName() + "\\.opam\\0.0.0\\bin\\ocaml.exe";
            }
        } catch (ExecutionException ignore) {}

        HOME_INVALID = "C:\\cygwin64\\invalid";
        OCAML_BIN_INVALID = "C:\\cygwin64\\invalid\\bin\\ocaml.exe";
        OCAML_BIN_INVALID_NO_EXE = "C:\\cygwin64\\invalid\\bin\\ocaml";
    }

    @Override public String getName() {
        return installationFolderName;
    }

    @Override public boolean isBinAvailable() {
        return BIN_VALID_SDK != null || BIN_CREATE_SDK != null;
    }

    @Override public boolean isOpamAvailable() {
        return OPAM_VALID_SDK != null || OPAM_INVALID_BIN != null;
    }
}
