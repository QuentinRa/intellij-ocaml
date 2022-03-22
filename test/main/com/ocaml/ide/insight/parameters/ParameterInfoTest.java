package com.ocaml.ide.insight.parameters;

import com.intellij.psi.PsiElement;
import com.ocaml.ide.OCamlIdeTest;
import com.ocaml.ide.insight.OCamlParameterInfo;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class ParameterInfoTest extends OCamlIdeTest {

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/parameters/";
    }

    private void doTest(@NotNull String text, int expectedIndex, String ... values) {
        // load file
        PsiElement caret = configureCodeWithCaret("param.ml", text);
        // load annotations
        configureAnnotResultsService(caret, "param.annot");

        FakeCreateParameterInfoContext context = new FakeCreateParameterInfoContext(
                getProject(), caret
        );

        // generate result
        new OCamlParameterInfo().findElementForParameterInfo(context);

        // get result
        if (expectedIndex == -1) { // empty
            assertNull(context.toShow);
            return;
        }
        // or, not
        assertNotNull(context.toShow);
        assertSize(1, context.toShow);
        assertInstanceOf(context.toShow[0], OCamlParameterInfo.ParameterInfoArgumentList.class);
        var p = (OCamlParameterInfo.ParameterInfoArgumentList) context.toShow[0];

        // assert names
        List<String> expected = Arrays.asList(values);
        assertEquals(expected, p.names);

        // assert index
        assertEquals("Expected index, ", expectedIndex, p.currentArgumentIndex);
    }

    @Test
    public void testBeforeFunctionFull() {
        doTest("let _ = (*caret*) max 5 7", -1);
    }

    @Test
    public void testInvalidContext() {
        doTest("let _(*caret*) = max 5 7", -1);
    }

    @Test
    public void testAfterFirstFull() {
        doTest("let _ = max 5(*caret*) 7", 1,"int", "int");
        doTest("let _ = max 5 7(*caret*)", 2,"int", "int");
    }

    @Test
    public void testAfterFunctionFull() {
        doTest("let _ = max(*caret*) 5 7", 0,"int", "int");
    }

    @Test
    public void testCoupleFull() {
        doTest("let _ = max (5 + max 5 7) (7 + 3)(*caret*)", 2,"int", "int");
        doTest("let _ = max (5 + max 5 7)(*caret*) (7 + 3)", 1,"int", "int");
        doTest("let _ = max(*caret*) (5 + max 5 7) (7 + 3)", 0,"int", "int");
    }

    @Test
    public void testInCoupleFull() {
        doTest("let _ = max (5 + max 5 7) (7 + 3(*caret*))", 2, "int", "int");
        doTest("let _ = max (5 + max 5 7(*caret*)) (7 + 3)", 2, "int", "int");
        doTest("let _ = max (5 +(*caret*) max 5 7) (7 + 3)", 1, "int", "int");
        doTest("let _ = max (5(*caret*) + max 5 7) (7 + 3)", 1, "int", "int");
    }

    @Test
    public void testFunctionAsArgument() {
        String[] types = { "int -> int -> int", "int", "int" };
        doTest("let id v(*caret*) = v", -1);
        doTest("let _ = id max 0(*caret*) 5", 2, types);
        doTest("let _ = id max (*caret*)0 5", 1, types);
        doTest("let _ = id m(*caret*)ax 0 5", 1, types);
        doTest("let _ = id (*caret*)max 0 5", 0, types);
        doTest("let _ = id max 0 5(*caret*)", 3, types);
    }

    @Test
    public void testLabelsOnlyNormalOrder() {
        String[] types = { "x:int", "y:float" };
        doTest("let _ = f ~x:3 ~y:2.0(*caret*)", 2, types);
        doTest("let _ = f ~x:3(*caret*) ~y:2.0", 1, types);
        doTest("let _ = f (*caret*)~x:3 ~y:2.0", 0, types);

        // x and y are inverted
        types = new String[]{"[y:float]", "[x:int]"};
        doTest("let _ = f ~y:2.0 ~x:3(*caret*)", 2, types);
        doTest("let _ = f ~y:2.0 (*caret*)~x:3", 1, types);
        doTest("let _ = f (*caret*)~y:2.0 ~x:3", 0, types);
    }

    // why so many tests? because the code is <s>shit</s> a mess.
    @Test
    public void testLabelsAndNormal() {
        // normal order
        String[] types = { "x:int", "y:float", "int" };
        doTest("let _ = f ~x:3 ~y:2.0 3(*caret*)", 3, types);
        doTest("let _ = f ~x:3 ~y:2.0(*caret*) 3", 2, types);
        doTest("let _ = f ~x:3(*caret*) ~y:2.0 3", 1, types);
        doTest("let _ = f (*caret*)~x:3 ~y:2.0 3", 0, types);

        // we got a third parameter, that is not a label
        types = new String[]{"[int]", "[x:int]", "[y:float]"};
        doTest("let _ = f 3 ~x:3 ~y:2.0(*caret*)", 3, types);
        doTest("let _ = f 3 ~x:3 (*caret*)~y:2.0", 2, types);
        doTest("let _ = f 3 (*caret*)~x:3 ~y:2.0", 1, types);
        doTest("let _ = f (*caret*)3 ~x:3 ~y:2.0", 0, types);
    }
}
