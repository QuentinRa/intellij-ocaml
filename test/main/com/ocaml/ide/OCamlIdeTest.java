package com.ocaml.ide;

import com.ocaml.OCamlBaseTest;
import com.ocaml.utils.adaptor.UntilIdeVersion;
import org.junit.Test;

public class OCamlIdeTest extends OCamlBaseTest {

    @UntilIdeVersion(release = "203")
    @SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase") @Test
    public void testFake() { assertTrue(true); }

}
