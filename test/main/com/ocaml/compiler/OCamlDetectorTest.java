package com.ocaml.compiler;

import com.ocaml.*;
import org.junit.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class OCamlDetectorTest extends OCamlBaseTest {

    @Test
    public void testDetectBinaries() {
        // we could check that each value is the one in
        // the PATH, if we got values in the PATH
        OCamlDetector.DetectionResult output = OCamlDetector.detectBinaries();
        assertNotNull(output); // there is everything in the PATH
    }

    public void callFindAssociatedBinaries(String binary){
        callFindAssociatedBinaries(binary, OCamlDetector.NO_ASSOCIATED_BINARIES);
    }

    public void callFindAssociatedBinaries(String binary,
                                           OCamlDetector.AssociatedBinaries expected){
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> t = new AtomicReference<>();
        OCamlDetector.findAssociatedBinaries(binary, arg -> {
            try {
                assertEquals(expected.ocamlCompiler, arg.ocamlCompiler);
                assertEquals(expected.version, arg.version);
                assertEquals(expected.sources, arg.sources);
            } catch (Throwable e) {
                t.set(e);
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            fail(e.getMessage());
        }
        if (t.get() != null) fail(t.get().getMessage());
    }

    @Test
    public void testFindAssociatedBinariesEmpty() {
        callFindAssociatedBinaries("");
    }

    @Test
    public void testFindAssociatedBinariesInvalidWindows() {
        callFindAssociatedBinaries("C:\\opam\\bin\\ocaml");
    }

    @Test
    public void testFindAssociatedBinariesInvalidWSL() {
        callFindAssociatedBinaries("\\\\wsl$\\Debian\\not\\valid");
    }

    @Test
    public void testFindAssociatedBinariesNotExistsWSL() {
        callFindAssociatedBinaries(I_OCAML_BINARY_WSL);
    }

    @Test
    public void testFindAssociatedBinariesNotFoundWSL() {
        callFindAssociatedBinaries(N_OCAML_BINARY_WSL);
    }

    @Test
    public void testFindAssociatedBinariesWSL() {
        OCamlDetector.AssociatedBinaries arg = new OCamlDetector.AssociatedBinaries(
                V_OCAML_COMPILER_WSL, V_OCAML_VERSION_WSL, V_OCAML_SOURCES_FOLDER_WSL
        );
        callFindAssociatedBinaries(V_OCAML_BINARY_WSL, arg);
    }

    @Test
    public void testFindAssociatedBinariesWindows() {
        OCamlDetector.AssociatedBinaries arg = new OCamlDetector.AssociatedBinaries(
                V_OCAML_COMPILER_OPT_WIN, V_OCAML_VERSION_WIN, V_OCAML_SOURCES_FOLDER_WIN
        );
        callFindAssociatedBinaries(V_OCAML_BINARY_WIN, arg);
    }

}
