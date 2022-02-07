package com.ocaml.ide.console.debug;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Pair;
import com.ocaml.OCamlBaseTest;
import com.ocaml.ide.console.debug.groups.TreeElementGroupKind;
import com.ocaml.ide.console.debug.groups.elements.OCamlTreeElement;
import com.ocaml.ide.console.debug.groups.elements.OCamlVariableElement;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.ArrayList;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlREPLOutputParserTest extends OCamlBaseTest {

    private void assertVariable(String message, String expectedValue, String expectedText, String expectedLocation) {
        ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> r = assertResult(message);
        assertSize(1, r);
        Pair<OCamlTreeElement, TreeElementGroupKind> e = r.get(0);
        assertElement(e, expectedValue, expectedText, expectedLocation, OCamlVariableElement.class);
    }

    private @NotNull ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> assertResult(String message) {
        // get the result
        ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> res = OCamlREPLOutputParser.parse(message);
        assertNotNull(res);
        return res;
    }

    private <T> void assertElement(@NotNull Pair<OCamlTreeElement, TreeElementGroupKind> parse,
                                   String expectedValue, String expectedText,
                                   String expectedLocation, Class<T> aClass) {
        // get the string
        OCamlTreeElement e = parse.first;
        assertInstanceOf(e, aClass);
        assertEquals(expectedValue, e.getValue());

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
        ArrayList<Pair<OCamlTreeElement, TreeElementGroupKind>> res =
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
}
