package com.ocaml.lang.utils;

import com.ocaml.OCamlBaseTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlConsumeParametersTest extends OCamlBaseTest {

    public void consume(String ... params) {
        // we iterated the context and found a list of parameters
        List<String> parameters = Arrays.asList(params);
    }

    @Test
    public void testOneParamOk() {
        consume("unit -> unit", "unit");
        // all parameters were submitted
    }

    @Test
    public void testTwoParamsOk() {
        consume("int -> float -> unit", "int", "float");
        // all parameters were submitted
    }

    @Test
    public void testTypesIParamsOk() {
        consume("int -> float -> unit", "int", "float");
        // all parameters were submitted
    }

}
