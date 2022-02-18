package com.ocaml.sdk.providers;

import com.ocaml.OCamlBaseTest;
import com.ocaml.sdk.providers.cygwin.CygwinFolders;
import com.ocaml.sdk.providers.linux.LinuxFolders;
import com.ocaml.sdk.utils.OCamlSdkHomeManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class BaseSdkProviderTest extends OCamlBaseTest {

    @Test
    public void testAtLeastOnceProviderAvailable() {
        boolean isBinAvailable = false;
        boolean isOpamAvailable = false;

        // Cygwin64
        BaseFolderProvider cygwin64 = new CygwinFolders(CygwinFolders.CYGWIN_FOLDER);
        if (cygwin64.isBinAvailable()) isBinAvailable = true;
        if (cygwin64.isOpamAvailable()) isOpamAvailable = true;
        logFolderProvide(cygwin64);

        // OCaml64
        BaseFolderProvider ocaml64 = new CygwinFolders(CygwinFolders.OCAML64_FOLDER);
        if (ocaml64.isBinAvailable()) isBinAvailable = true;
        if (ocaml64.isOpamAvailable()) isOpamAvailable = true;
        logFolderProvide(ocaml64);

        // Linux
        BaseFolderProvider linux = new LinuxFolders();
        if (linux.isBinAvailable()) isBinAvailable = true;
        if (linux.isOpamAvailable()) isOpamAvailable = true;
        logFolderProvide(linux);

        // WSL is available since 203+
        try {
            Class<?> aClass = Class.forName("com.ocaml.sdk.providers.wsl.WSLFolders");
            BaseFolderProvider instance = (BaseFolderProvider) aClass.getDeclaredConstructor().newInstance();

            if (instance.isBinAvailable()) isBinAvailable = true;
            if (instance.isOpamAvailable()) isOpamAvailable = true;

            logFolderProvide(instance);
        } catch (ClassNotFoundException | NoSuchMethodException |
                InstantiationException | IllegalAccessException | InvocationTargetException ignore) {}

        assertTrue(isBinAvailable);
        assertTrue(isOpamAvailable);
    }

    private void logFolderProvide(@NotNull BaseFolderProvider instance) {
        String name = instance.getName();
        System.out.println("  + "+name);
        System.out.println("    - bin : "+instance.isBinAvailable());
        System.out.println("    - opam : "+instance.isOpamAvailable());
    }

    protected void assertInstallationFolderWasSuggested(@NotNull String installationFolder) {
        List<String> homePaths = OCamlSdkHomeManager.suggestHomePaths();
        Path p = Path.of(installationFolder);
        // cannot test anything
        if (!Files.exists(p)) {
            assertTrue(true);
            return;
        }
        File[] files = p.toFile().listFiles();
        if (files == null) { // no files, done
            assertTrue(true);
            return;
        }
        for (File file : files) {
            String path = file.getAbsolutePath();
            if (!OCamlSdkHomeManager.isValid(path)) continue;
            assertTrue(homePaths.remove(path));
        }
    }
}
