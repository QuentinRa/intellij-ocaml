package com.ocaml.compiler;

import com.ocaml.*;
import org.junit.*;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class OCamlDetectorTest extends OCamlBaseTest {

    @Test
    public void testDetectBinaries() {
        // we could check that each value is the one in
        // the PATH, if we got values in the PATH
        OCamlDetector.DetectionResult output = OCamlDetector.detectBinaries();
        assertNotNull(output); // there is everything in the PATH
    }

}
