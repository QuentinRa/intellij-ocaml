package com.ocaml.compiler;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.ocaml.utils.files.*;
import org.jetbrains.annotations.*;

import java.io.*;

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

    public static @Nullable DetectionResult detectBinaries() {
        // ocaml
        File ocaml = OCamlPathUtils.findExecutableInPathAsFile("ocaml");
        if (ocaml == null) { return null; }
        // ocamlc
        String ocamlc = OCamlPathUtils.findExecutableInPath("ocamlc");
        if (ocamlc == null) return null;
        // issue on cygwin, ocamlc.exe is not valid
        if (ocamlc.contains("cygwin64")) ocamlc += ".opt.exe";
        // sources
        File sourcesFolder = ocaml.getParentFile();
        if (sourcesFolder == null) { return null; }
        sourcesFolder = sourcesFolder.getParentFile();
        if (sourcesFolder == null) { return null; }
        // check the two known locations
        File lib = new File(sourcesFolder, "lib/ocaml");
        File usrLib = new File(sourcesFolder, "usr/lib/ocaml");
        String sourcesFolderPath;
        if (lib.exists()) sourcesFolderPath = lib.getAbsolutePath();
        else if (usrLib.exists()) sourcesFolderPath = usrLib.getAbsolutePath();
        else { return null; }
        // Look for version
        GeneralCommandLine cli = new GeneralCommandLine(ocamlc, "-version");
        try {
            Process process = cli.createProcess();
            InputStream inputStream = process.getInputStream();
            String version = new String(inputStream.readAllBytes());

            return new DetectionResult(
                    ocaml.getAbsolutePath(),
                    ocamlc, version,
                    sourcesFolderPath);
        } catch (ExecutionException | IOException e) {
            return null;
        }
    }
}
