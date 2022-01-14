package com.ocaml.ide.sdk;

import com.intellij.openapi.options.*;
import com.intellij.openapi.projectRoots.*;
import com.intellij.util.*;
import com.ocaml.*;
import org.jetbrains.annotations.*;

import java.io.*;
import java.nio.file.*;

/**
 * Todo: tests for this class
 * Todo: files must be checked on the WSL if we are using a WSL
 * Todo: must work on Windows too
 * Todo: must work on Linux too
 */
public final class OCamlSdkUtils {

    /**
     * Create an SDK or raise an exception with some information
     * as to why the creation failed.
     *
     * @param ocamlBinary must ends with <code>/bin/ocaml</code>
     * @param ocamlCompilerBinary must ends with <code>/bin/ocamlc</code>
     * @param ocamlSourcesFolder ex: <code>/usr/lib/ocaml</code> or <code>/lib/ocaml</code> for opam.
     *                           Must ends with <code>/lib/ocaml</code>
     * @return an SDK, null if there is no data to create an SDK
     * @throws ConfigurationException if the data is invalid
     */
    public static @Nullable Sdk createSdk(@NotNull String ocamlBinary,
                                @NotNull String version,
                                @NotNull String ocamlCompilerBinary,
                                @NotNull String ocamlSourcesFolder) throws ConfigurationException {
        if (version.isEmpty()) // todo: translate
            throw new ConfigurationException("No OCaml version. Please, wait until we detect the version of ocaml.");
        if (!PathUtil.toSystemIndependentName(ocamlBinary).endsWith("/bin/ocaml"))
            throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.binary.invalid"));
        // the user can set the compiler path, so we are good for this one
        // todo: check library folder

        String homePath;

        try {
            Path ocamlBinaryPath = Path.of(ocamlBinary);
            // Path ocamlCompilerPath = Path.of(ocamlCompilerBinary);
            // ocaml
            File file = ocamlBinaryPath.toFile();
            if (!file.exists() || !file.isFile() || !file.canExecute())
                throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.binary.not.exist"));
            // ocaml compiler
            //file = ocamlCompilerPath.toFile();
            //if (!file.exists() || !file.isFile() || !file.canExecute())
            //    throw new ConfigurationException(OCamlBundle.message("sdk.ocaml.compiler.not.exist"));

            // assuming that we are in /bin/ocaml
            // the root is / so the parent of our parent
            homePath = file.getParentFile().getParent();
        } catch (Exception e) {
            throw new ConfigurationException(e.getMessage());
        }

        return createSdk(homePath, version, ocamlBinary, ocamlCompilerBinary, ocamlSourcesFolder);
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
