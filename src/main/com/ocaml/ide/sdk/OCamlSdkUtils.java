package com.ocaml.ide.sdk;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.wsl.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.projectRoots.*;
import com.ocaml.*;
import com.ocaml.utils.files.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;

//else {
//    // handle Windows, WSL not found, and Linux
//    throw new ConfigurationException("Not supported yet.");
//}
// todo: improve error message if "\\" instead of "/"
// todo: ocamlopt (fallback)
// todo: .exe allowed
public final class OCamlSdkUtils {

    /**
     * Create an SDK or raise an exception with some information
     * as to why the creation failed.
     *
     * @param ocamlBinary must ends with <code>/bin/ocaml</code> (.exe allowed)
     * @param ocamlCompilerBinary must ends with <code>/bin/ocamlc</code> (.exe allowed)
     * @param ocamlSourcesFolder ex: <code>/usr/lib/ocaml</code> or <code>/lib/ocaml</code> for opam.
     *                           Must ends with <code>/lib/ocaml</code>
     * @return an SDK, null if there is no data to create an SDK
     * @throws ConfigurationException if the data is invalid
     */
    public static @Nullable Sdk createSdk(@NotNull String ocamlBinary,
                                @NotNull String version,
                                @NotNull String ocamlCompilerBinary,
                                @NotNull String ocamlSourcesFolder) throws ConfigurationException {
        System.out.println("here with1:"+ocamlBinary);
        System.out.println("here with2:"+version);
        System.out.println("here with3:"+ocamlCompilerBinary);
        System.out.println("here with4:"+ocamlSourcesFolder);

        if (version.isEmpty())
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.version.empty"));

        WslPath wslPath = WslPath.parseWindowsUncPath(ocamlBinary);
        WSLDistribution distribution = null;
        if (wslPath != null) {
            distribution = wslPath.getDistribution();
            if (distribution.getVersion() == -1)
                throw new ConfigurationException(OCamlBundle.message("sdk.path.binary.wsl.invalid", distribution.getPresentableName()));
            ocamlBinary = distribution.getWslPath(ocamlBinary);
            if (ocamlBinary == null) ocamlBinary = "";
            ocamlCompilerBinary = distribution.getWslPath(ocamlCompilerBinary);
            if (ocamlCompilerBinary == null) ocamlCompilerBinary = "";
            ocamlSourcesFolder = distribution.getWslPath(ocamlSourcesFolder);
            if (ocamlSourcesFolder == null) ocamlSourcesFolder = "";

            System.out.println("now with1:"+ocamlBinary);
            System.out.println("now with2:"+ocamlCompilerBinary);
            System.out.println("now with3:"+ocamlSourcesFolder);
        }

        // Check paths valid
        if (!OCamlPathUtils.fileEndsWith(ocamlBinary, "/bin/ocaml", ".exe"))
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.binary.invalid"));
        if (!OCamlPathUtils.folderEndsWith(ocamlSourcesFolder,"/lib/ocaml")) // folder, can end with a /
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.sources.folder.invalid"));

        // Check file exists
        int exitCode = -4;
        if (distribution != null) {
            try {
                GeneralCommandLine cli = new GeneralCommandLine("true");
                WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
                wslCommandLineOptions.addInitCommand(
                        "(if [ -f "+ocamlBinary+" ]; then" +
                        " if [ -f "+ocamlCompilerBinary+" ]; then" +
                        " if [ -d "+ocamlSourcesFolder+" ]; then echo 0; else echo -3; fi;" +
                        " else echo -2; fi;" +
                        " else echo -1; fi;)"
                );
                cli = distribution.patchCommandLine(cli, null, wslCommandLineOptions);
                Process process = cli.createProcess();
                process.waitFor();
                exitCode = Integer.parseInt(
                        new String(process.getInputStream().readAllBytes()).replace("\n", "")
                );
            } catch (ExecutionException | InterruptedException | IOException e) {
                throw new ConfigurationException("Unexpected error:"+e.getMessage());
            }
        } else {
            try {
                File file = Path.of(ocamlBinary).toFile();
                if (!file.exists() || !file.isFile()) exitCode = -1;
                else {
                    file = Path.of(ocamlCompilerBinary).toFile();
                    if (!file.exists() || !file.isFile()) {
                        // try again with opt
                        ocamlCompilerBinary = ocamlCompilerBinary.replace("ocamlc.exe", "ocamlopt");
                        ocamlCompilerBinary = ocamlCompilerBinary.replace("ocamlc", "ocamlopt");
                        file = Path.of(ocamlCompilerBinary).toFile();
                        if (!file.exists()) { // issue: no extension = not a file?
                            // try again with opt.exe
                            ocamlCompilerBinary = ocamlCompilerBinary.replace("ocamlopt", "ocamlopt.exe");
                            file = Path.of(ocamlCompilerBinary).toFile();
                            if (!file.exists() || !file.isFile()) exitCode = -2;
                        }
                    }

                    if (exitCode == -4) {
                        file = Path.of(ocamlSourcesFolder).toFile();
                        if (!file.exists() || !file.isDirectory()) exitCode = -3;
                        else {
                            exitCode = 0; // OK
                        }
                    }
                }
            } catch (InvalidPathException e) {
                throw new ConfigurationException("Unexpected error:"+e.getMessage());
            }
        }

        switch (exitCode) {
            case -1:
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.binary.not.found"));
            case -2:
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.compiler.not.found"));
            case -3:
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.sources.not.found"));
            case -4:
                throw new IllegalStateException("Couldn't check if the path exists. Please report this error.");
        }

        return null;
    }

    private static Sdk createSdk(String homePath, String version, String ocamlBinary,
                                 String ocamlCompilerBinary, String ocamlSourcesFolder) {
        //String sdkName = OCamlSdkType.suggestSdkName(version);
        //File file = new File(FileUtil.expandUserHome("~/.jdks"));
        //File sdkHome = FileUtil.findSequentNonexistentFile(file, sdkName.toLowerCase(), "");
        //System.out.println("sdkHome exists?"+sdkHome.exists());
        //System.out.println("sdkHome exists?"+sdkHome.getAbsolutePath());
        //WslPath p = WslPath.parseWindowsUncPath(ocamlBinary);
        //if (p != null) {
        //    try {
        //        String binFolder = "~/.jdks/"+sdkHome.getName()+"/bin";
        //        String libFolder = "~/.jdks/"+sdkHome.getName()+"/lib";
        //
        //        WSLDistribution distribution = p.getDistribution();
        //        homePath = distribution.getWslPath(sdkHome.getAbsolutePath());
        //        String ocamlBinaryWSL = distribution.getWslPath(ocamlBinary);
        //        String ocamlCompilerWSL = distribution.getWslPath(ocamlCompilerBinary);
        //        String ocamlSourcesFolderWSL = distribution.getWslPath(ocamlSourcesFolder);
        //
        //        WSLCommandLineOptions wslCommandLineOptions = new WSLCommandLineOptions();
        //        // the order will be reversed, so we need to put the last commands first
        //        wslCommandLineOptions.addInitCommand("ln -s "+ocamlSourcesFolderWSL+" "+libFolder+"/ocaml/");
        //        wslCommandLineOptions.addInitCommand("ln -s "+ocamlCompilerWSL+" "+binFolder+"/ocamlc");
        //        wslCommandLineOptions.addInitCommand("ln -s "+ocamlBinaryWSL+" "+binFolder+"/ocaml");
        //        wslCommandLineOptions.addInitCommand("mkdir -p "+libFolder+"/ocaml");
        //        wslCommandLineOptions.addInitCommand("mkdir -p "+binFolder);
        //
        //        GeneralCommandLine generalCommandLine = distribution.patchCommandLine(
        //                new GeneralCommandLine("true"),
        //                null,
        //                wslCommandLineOptions
        //        );
        //        Process process = generalCommandLine.createProcess();
        //        process.waitFor();
        //    } catch (ExecutionException | InterruptedException e) {
        //        throw new ConfigurationException(e.getMessage());
        //    }
        //}
        //
        ////Sdk sdk = ProjectJdkTable.getInstance().createSdk(
        ////        OCamlSdkType.suggestSdkName(version),
        ////        OCamlSdkType.getInstance()
        ////);
        ////SdkModificator sdkModificator = sdk.getSdkModificator();
        ////sdkModificator.setHomePath(homePath);
        ////sdkModificator.setVersionString(version);
        //// todo: add sources
        //// todo: commit changes
        //System.out.println("homePath:"+homePath);
        //System.out.println("version:"+version);
        //return null;
        return null;
    }

}
