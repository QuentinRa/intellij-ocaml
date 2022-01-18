package com.ocaml.compiler;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.wsl.*;
import com.ocaml.*;
import com.ocaml.compiler.cygwin.*;
import com.ocaml.utils.*;
import com.ocaml.utils.files.*;
import org.jetbrains.annotations.*;

import java.io.*;

/**
 * Used to detect things such as the ocaml version in the PATH,
 * or given a binary, the sources and the compiler.
 */
public final class OCamlDetector {

    public static class DetectionResult {
        public final String ocaml;
        public final String ocamlCompiler;
        public final String version;
        public final String sources;

        DetectionResult(String ocaml, String ocamlCompiler, String version,
                        String sources) {
            this.ocaml = ocaml;
            this.ocamlCompiler = ocamlCompiler;
            this.version = version;
            this.sources = sources;
        }
    }

    /**
     * Look for ocaml and ocamlc in the path.
     * Check the version of ocaml with "ocamlc -version".
     * Look for the library in {OCAML_FOLDER}/../../lib/ocaml
     * and in {OCAML_FOLDER}/../../usr/lib/ocaml (separators aren't hardcoded).
     * If we found everything, then we return the DetectionResult,
     * otherwise, we return null.
     */
    public static @Nullable DetectionResult detectBinaries() {
        // ocaml
        File ocaml = OCamlPathUtils.findExecutableInPathAsFile(OCamlConstants.OCAML_EXECUTABLE);
        if (ocaml == null) { return null; }
        // ocamlc
        String ocamlc = OCamlPathUtils.findExecutableInPath(OCamlConstants.OCAML_COMPILER_EXECUTABLE);
        if (ocamlc == null) return null;
        return detectBinaries(ocaml, ocamlc);
    }

    private static @Nullable DetectionResult detectBinaries(File ocaml, String ocamlc) {
        // issue on cygwin, ocamlc.exe is not valid
        if (ocamlc.contains(CygwinConstants.CYGWIN_FOLDER)) ocamlc += CygwinConstants.OCAMLC_OPT;
        // sources
        File sourcesFolder = ocaml.getParentFile();
        if (sourcesFolder == null) { return null; }
        sourcesFolder = sourcesFolder.getParentFile();
        if (sourcesFolder == null) { return null; }
        // check the two known locations
        File lib = new File(sourcesFolder, OCamlConstants.LIB_FOLDER_LOCATION_R);
        File usrLib = new File(sourcesFolder, OCamlConstants.USR_LIB_FOLDER_LOCATION_R);
        String sourcesFolderPath;
        if (lib.exists()) sourcesFolderPath = lib.getAbsolutePath();
        else if (usrLib.exists()) sourcesFolderPath = usrLib.getAbsolutePath();
        else { return null; }
        // Look for version
        GeneralCommandLine cli = new GeneralCommandLine(ocamlc, OCamlConstants.OCAMLC_VERSION);
        try {
            Process process = cli.createProcess();
            InputStream inputStream = process.getInputStream();
            String version = new String(inputStream.readAllBytes()).replace("\n", "");

            return new DetectionResult(
                    ocaml.getAbsolutePath(),
                    ocamlc, version,
                    sourcesFolderPath);
        } catch (ExecutionException | IOException e) {
            // log: log this exception
            return null;
        }
    }

    public static class AssociatedBinaries {
        public final String ocamlCompiler;
        public final String version;
        public final String sources;

        AssociatedBinaries(String ocamlCompiler, String version, String sources) {
            this.ocamlCompiler = ocamlCompiler;
            this.version = version;
            this.sources = sources;
        }

        @Override public String toString() {
            return "AssociatedBinaries{" +
                    "ocamlCompiler='" + ocamlCompiler + '\'' +
                    ", version='" + version + '\'' +
                    ", sources='" + sources + '\'' +
                    '}';
        }
    }

    /**
     * Find the version, the compiler, and the sources folders for an SDK.
     */
    public static void findAssociatedBinaries(String ocamlBinary,
                                              Callback<AssociatedBinaries> callback) {
        WslPath path = WslPath.parseWindowsUncPath(ocamlBinary);
        callback.call(path == null ?
                findAssociatedBinariesWindows(ocamlBinary) :
                findAssociatedBinariesWSL(ocamlBinary, path));
    }

    /**
     * Error message
     */
    public static final AssociatedBinaries NO_ASSOCIATED_BINARIES = new AssociatedBinaries(
            OCamlBundle.message("sdk.ocaml.binary.invalid.short"),
            OCamlBundle.message("sdk.version.unknown"),
            OCamlBundle.message("sdk.ocaml.binary.invalid.short")
    );

    /**
     * Find the version, the compiler, and the sources folders for a WSL SDK.
     */
    private static AssociatedBinaries findAssociatedBinariesWSL(String ocamlBinary, WslPath path) {
        if (!ocamlBinary.endsWith("\\bin\\ocaml")) return NO_ASSOCIATED_BINARIES;
        WSLDistribution distribution = path.getDistribution();
        String ocamlc = distribution.getWslPath(ocamlBinary+"c");
        if ( ocamlc == null ) return NO_ASSOCIATED_BINARIES;
        String root = ocamlc.replace("bin/"+OCamlConstants.OCAML_COMPILER_EXECUTABLE, "");
        String libFolder = root+OCamlConstants.LIB_FOLDER_LOCATION_R;
        String usrLibFolder = root+OCamlConstants.USR_LIB_FOLDER_LOCATION_R;
        WSLCommandLineOptions options = new WSLCommandLineOptions();
        // they are called in the reverse order
        options.addInitCommand("find "+usrLibFolder+" -maxdepth 0 2>&1 || true");
        options.addInitCommand("find "+libFolder+" -maxdepth 0 2>&1 || true");
        options.addInitCommand(ocamlc+" "+OCamlConstants.OCAMLC_VERSION+" 2>&1");
        try {
            GeneralCommandLine cli = new GeneralCommandLine("true");
            cli = distribution.patchCommandLine(cli, null, options);
            Process process = cli.createProcess();
            process.waitFor();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String version = bufferedReader.readLine();
            // not a valid version
            if (version == null || !OCamlConstants.VERSION_REGEXP.matcher(version).matches())
                return NO_ASSOCIATED_BINARIES;
            // Check the two known lib folders
            String libDir = bufferedReader.readLine();
            String source;
            if (libDir == null || !libDir.equals(libFolder)) {
                String usrDir = bufferedReader.readLine();
                if (usrDir == null || !usrDir.endsWith(usrLibFolder)) {
                    return NO_ASSOCIATED_BINARIES;
                } else {
                    source = usrLibFolder;
                }
            } else {
                source = libFolder;
            }
            return new AssociatedBinaries(ocamlBinary+"c", version, distribution.getWindowsPath(source));
        } catch (ExecutionException | InterruptedException | IOException e) {
            // log: log this exception
            return NO_ASSOCIATED_BINARIES;
        }
    }

    /**
     * Find the version, the compiler, and the sources folders for a Windows SDK.
     */
    private static AssociatedBinaries findAssociatedBinariesWindows(String ocamlBinary) {
        // assuming that ocaml exists, is in the bin folder, then we also have ocamlc in
        // the folder.
        if (!ocamlBinary.endsWith("ocaml") && !ocamlBinary.endsWith("ocaml.exe"))
            return NO_ASSOCIATED_BINARIES;
        File ocaml = new File(ocamlBinary);
        if (!ocaml.exists()) return NO_ASSOCIATED_BINARIES;
        File bin = ocaml.getParentFile();
        if (bin == null) return NO_ASSOCIATED_BINARIES;
        File ocamlcFile = new File(bin, OCamlConstants.OCAML_COMPILER_EXECUTABLE);
        if (!ocaml.exists()) {
            ocamlcFile = new File(bin, OCamlConstants.OCAML_COMPILER_EXECUTABLE + ".exe");
            if (!ocaml.exists()) return NO_ASSOCIATED_BINARIES;
        }
        // get the result
        DetectionResult detectionResult = detectBinaries(ocaml, ocamlcFile.getAbsolutePath());
        if (detectionResult == null) return NO_ASSOCIATED_BINARIES;
        // convert
        return new AssociatedBinaries(
                detectionResult.ocamlCompiler,
                detectionResult.version,
                detectionResult.sources
        );
    }
}
