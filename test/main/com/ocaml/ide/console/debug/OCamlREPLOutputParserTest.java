package com.ocaml.ide.console.debug;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Pair;
import com.ocaml.OCamlBaseTest;
import com.ocaml.ide.console.debug.groups.TreeElementGroupKind;
import com.ocaml.ide.console.debug.groups.elements.OCamlFunctionElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlTreeElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlTypeElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlVariableElement;
import com.ocaml.sdk.repl.OCamlREPLConstants;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlREPLOutputParserTest extends OCamlBaseTest {

    private void assertVariable(String message, String expectedValue, String expectedText, String expectedLocation) {
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> r = assertResult(message);
        assertSize(1, r);
        Pair<OCamlTreeElement, TreeElementGroupKind> e = r.get(0);
        assertElement(e, expectedValue, expectedText, expectedLocation, OCamlVariableElement.class);
    }

    private void assertFunction(String message, String expectedText, String expectedLocation) {
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> r = assertResult(message);
        assertSize(1, r);
        Pair<OCamlTreeElement, TreeElementGroupKind> e = r.get(0);
        assertElement(e, OCamlREPLConstants.FUN, expectedText, expectedLocation, OCamlFunctionElement.class);
    }

    private void assertType(String message, String expectedValue, String expectedText) {
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> r = assertResult(message);
        assertSize(1, r);
        Pair<OCamlTreeElement, TreeElementGroupKind> e = r.get(0);
        assertElement(e, expectedValue, expectedText, null, OCamlTypeElement.class);
    }

    private @NotNull List<Pair<OCamlTreeElement, TreeElementGroupKind>> assertResult(String message) {
        // get the result
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> res = OCamlREPLOutputParser.parse(message);
        assertNotNull(res);
        return res;
    }

    private <T> void assertElement(@NotNull Pair<OCamlTreeElement, TreeElementGroupKind> parse,
                                   String expectedValue, String expectedText,
                                   String expectedLocation, Class<T> aClass) {
        // get the string
        OCamlTreeElement e = parse.first;
        assertInstanceOf(e, aClass);
        if (expectedValue == null) {
           assertTrue(e.isValueNull());
        } else {
            assertEquals(expectedValue, e.getValue());
        }

        ItemPresentation presentation = e.getPresentation();
        assertEquals(expectedText, presentation.getPresentableText());
        assertEquals(expectedLocation, presentation.getLocationString());
    }

    @Test
    public void testSimpleVariable() {
        assertVariable(
                "val hw : string = \"Hello, World!@.\"",
                "\"Hello, World!@.\"", "hw = \"Hello, World!@.\"",
                "string");
    }

    @Test
    public void testVariableList() {
        assertVariable("val l : int list = [3; 4; 5]",
                "[3; 4; 5]",
                "l = [3; 4; 5]",
                "int list");
    }

    @Test
    public void testVariableConstructor() {
        assertVariable("val t : nucleotide option = Some A",
                "Some A",
                "t = Some A",
                "nucleotide option");
    }

    @Test
    public void testWithNewLine() {
        assertVariable("val x : int\n= 5",
                "5",
                "x = 5",
                "int");
    }

    @Test
    public void testReallyLongVariable() {
        assertVariable("val big_list : int list =\n" +
                        "[3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3;\n" +
                        "4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4;\n" +
                        "5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5;\n" +
                        "3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3;\n" +
                        "4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4;\n" +
                        "5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5;\n" +
                        "3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3;\n" +
                        "4; 5; 3; 4; 5]",
                "[3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; " +
                        "4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; " +
                        "5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; " +
                        "3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; " +
                        "4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; " +
                        "5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; " +
                        "3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; 4; 5; 3; " +
                        "4; 5; 3; 4; 5]",
                "big_list = [3; 4; 5; ...; 3; 4; 5]",
                "int list");
    }

    @Test
    public void testVariableAnd() {
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> res =
                assertResult("val x : int = 5\n" + "val y : int = 3");
        assertSize(2, res);
        assertElement(res.get(0),
                "5",
                "x = 5",
                "int",
                OCamlVariableElement.class);
        assertElement(res.get(1),
                "3",
                "y = 3",
                "int",
                OCamlVariableElement.class);
    }

    @Test
    public void testFunction() {
        assertFunction("val f1 : 'a -> int = <fun>", "f1 = <fun>", "'a -> int");
    }

    @Test
    public void testFunctionWithLongType() {
        assertFunction("val f2 : int -> int -> int -> int -> int -> int = <fun>",
                "f2 = <fun>",
                "int -> int -> int -> int -> int -> int");
    }

    @Test
    public void testFunctionWithNewLine() {
        assertFunction("val f3 : float -> ('a -> float) -> 'a -> float -> float -> float -> float =\n" +
                        "<fun>",
                "f3 = <fun>",
                "float -> ('a -> float) -> 'a -> float -> float -> float -> float");
    }

    @Test
    public void testFunctionAnd() {
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> res =
                assertResult("val f4 : 'a -> int = <fun>\n" + "val f5 : 'a -> int = <fun>");
        assertSize(2, res);
        assertElement(res.get(0),
                OCamlREPLConstants.FUN,
                "f4 = <fun>",
                "'a -> int",
                OCamlFunctionElement.class);
        assertElement(res.get(1),
                OCamlREPLConstants.FUN,
                "f5 = <fun>",
                "'a -> int",
                OCamlFunctionElement.class);
    }

    @Test
    public void testFunctionAndVariable() {
        List<Pair<OCamlTreeElement, TreeElementGroupKind>> res =
                assertResult("val f6 : 'a -> int = <fun>\n" + "val v : int = 5");
        assertSize(2, res);
        assertElement(res.get(0),
                OCamlREPLConstants.FUN,
                "f6 = <fun>",
                "'a -> int",
                OCamlFunctionElement.class);
        assertElement(res.get(1),
                "5",
                "v = 5",
                "int",
                OCamlVariableElement.class);
    }

    @Test
    public void testSimpleType() {
        assertType("type t", null, "t");
    }

    @Test
    public void testType() {
        assertType("type nucleotide = A | C | G | T",
                "A | C | G | T",
                "nucleotide = A | C | G | T");
    }

    @Test
    public void testLongType() {
        assertType("type acide =\n" +
                        "Ala\n" +
                        "| Arg\n" +
                        "| Asn\n" +
                        "| Asp\n" +
                        "| Cys\n" +
                        "| Glu\n" +
                        "| Gln\n" +
                        "| Gly\n" +
                        "| His\n" +
                        "| Ile\n" +
                        "| Leu\n" +
                        "| Lys\n" +
                        "| Phe\n" +
                        "| Pro\n" +
                        "| Ser\n" +
                        "| Thr\n" +
                        "| Trp\n" +
                        "| Tyr\n" +
                        "| Val\n" +
                        "| START\n" +
                        "| STOP",
                "Ala | Arg | Asn | Asp | Cys | Glu | Gln | Gly | His | Ile | Leu | Lys | Phe | Pro | Ser | Thr | Trp | Tyr | Val | START | STOP",
                "acide = Ala | Arg ...ART | STOP");
    }
}
