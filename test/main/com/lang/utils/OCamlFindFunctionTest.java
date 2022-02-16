package com.lang.utils;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.ocaml.ide.OCamlIdeTest;
import com.ocaml.ide.insight.OCamlAnnotResultsService;
import com.ocaml.lang.utils.OCamlFindFunction;
import com.or.lang.core.psi.*;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlFindFunctionTest extends OCamlIdeTest {

    private static final String FILE_NAME = "call.ml";
    private static final String FILE_NAME_ANNOT = "call.annot";

    @Override protected String getCustomTestDataPath() {
        return "com.lang.utils/";
    }

    private void assertParameter(@Language("OCaml") @NotNull String code, String expectedFunctionName, int expectedIndex) {
        @Language("OCaml") String originalMlCode = loadFile(FILE_NAME);
        assertNotNull(originalMlCode);

        // create file
        myFixture.configureByText(FILE_NAME, originalMlCode);

        PsiLet elementAt = myFixture.findElementByText(code.replace("(*caret*)", ""), PsiLet.class);
        assertNotNull(elementAt);
        int i = code.indexOf("(*caret*)");
        assertNotSame("Caret missing",-1, i);
        PsiElement caret = elementAt.findElementAt(i);
        assertNotNull(caret);

        // load annotations
        OCamlAnnotResultsService annot = getProject().getService(OCamlAnnotResultsService.class);
        File annotations = new File(getTestDataPath(), FILE_NAME_ANNOT);
        annot.updateForFile(elementAt.getContainingFile().getVirtualFile().getPath(), annotations);

        ArrayList<Pair<PsiElement, Integer>> res = new OCamlFindFunction(caret).lookForFunction();
        assertTrue(res.size() >= 1);
        Pair<PsiElement, Integer> data = res.get(0);
        assertEquals(expectedFunctionName, data.first.getText());
        assertEquals(expectedIndex, (int) data.second);
    }

    //testing with OCamlInferredSignature{name='null', kind=VALUE, type='int', file=call.ml(l:8-8, c:15-16), range=(113,114)}
    @Test
    public void testFunctionGenericWithRefBefore() {
        assertParameter("let _ = id ref (*caret*)0", "id", 0);
    }

    @Test
    public void testFunctionGenericWithRefAfter() {
        assertParameter("let _ = id ref 0(*caret*)", "id", 0);
    }

    @Test
    public void testFunctionWithUnitBefore() {
        assertParameter("let _ = id (*caret*)()", "id", 0);
    }

    @Test
    public void testFunctionWithUnitIn() {
        assertParameter("let _ = id ((*caret*))", "id", 0);
    }

    @Test
    public void testFunctionWithUnitAfter() {
        assertParameter("let _ = id ()(*caret*)", "id", 0);
    }

    @Test
    public void testFunctionWithString() {
        assertParameter("let _ = id (*caret*)\"something\"", "id", 0);
    }

    @Test
    public void testFunctionWithBool() {
        assertParameter("let _ = id (*caret*)true", "id", 0);
    }

    @Test
    public void testFunctionWithFloat() {
        assertParameter("let _ = id (*caret*)5.0", "id", 0);
    }

    @Test
    public void testFunctionTwoParamsP1() {
        assertParameter("let _ = f 5 (*caret*)0", "f", 1);
    }

    @Test
    public void testFunctionTwoParamsP2() {
        assertParameter("let _ = f (*caret*)5 0", "f", 0);
    }

    @Test
    public void testFunctionTwoParamsFirstScopedBeforeP1() {
        assertParameter("let _ = f (*caret*)(0) 5", "f", 0);
    }

    @Test
    public void testFunctionTwoParamsFirstScopedBeforeP2() {
        assertParameter("let _ = f (0) (*caret*)5", "f", 1);
    }

    @Test
    public void testFunctionTwoParamsBothScopedP1() {
        assertParameter("let _ = f (*caret*)(0) (5)", "f", 0);
    }

    @Test
    public void testFunctionTwoParamsBothScopedAfterP2() {
        assertParameter("let _ = f (0) (5)(*caret*)", "f", 1);
    }

    // todo: not supported / ignored test
    @Test @Ignore
    public void ignoredTestFunctionWasScoped() {
        assertParameter("let _ = (f) (0) (*caret*)(5)", "f", 1);
    }

    @Test
    public void testFunctionWithComment() {
        assertParameter("let _ = f 0 (* some comment *) (*caret*)5", "f", 1);
    }

    @Test
    public void testCallWithNestedCall() {
        assertParameter("let _ = f (f 0 5) (*caret*)0", "f", 1);
    }

    @Test
    public void testCallWithNestedCallIn() {
        assertParameter("let _ = g f (f 0 (*caret*)5) 0", "f", 1);
    }

    @Test
    public void testFunctionAsParameter() {
        assertParameter("let _ = g max 0 (*caret*)5", "g", 2);
    }

    @Test
    public void testFunctionAsParameterWithModule() {
        assertParameter("let _ = g Stdlib.max 0 (*caret*)5", "g", 2);
    }

    @Test
    public void testFunAsParameter() {
        assertParameter("let _ = g (fun x y -> max x y) 0 (*caret*)5", "g", 2);
    }

    @Test
    public void testFunAsParameterIn() {
        assertParameter("let _ = g (fun x y -> max x (*caret*)y) 0 5", "max", 1);
    }

    @Test
    public void testWithVariableFirst() {
        assertParameter("let _ = f v_i (*caret*)0", "f", 1);
    }

    @Test
    public void testWithVariableSecond() {
        assertParameter("let _ = f 0 (*caret*)v_i", "f", 1);
    }

    @Test
    public void testWithVariableBoth() {
        assertParameter("let _ = f v_i (*caret*)v_i", "f", 1);
    }

    @Test
    public void testWithVariable() {
        assertParameter("let _ = id (*caret*)v_f", "id", 0);
    }

    @Test
    public void testWithNot() { // todo: actually, it's not 1, but 0???
        // id got the type (bool -> bool) -> bool -> bool
        // meaning id f b -> b (but, actually, id is 'a -> 'a)
        assertParameter("let _ = id not (*caret*)true", "id", 1);
    }

    @Test
    public void testWithVariantFunction() { // todo: actually, it's not 1, but 0???
        assertParameter("let _ = id id ref (*caret*)0", "id", 1);
    }

    @Test
    public void testWithValueOfRef() { // todo: actually, it's not 1, but 0???
        assertParameter("let _ = id !(ref id) (*caret*)0", "id", 1);
    }

    // tuples
    @Test
    public void testWithTupleOfInt() {
        assertParameter("let _ = cpl_s (3,(*caret*)2)", "cpl_s", 0);
    }

    @Test
    public void testWithTupleOfTupleWithFunction() {
        assertParameter("let _ = cpl_s (0, cpl_s (5, (*caret*)7))", "cpl_s", 0);
    }

    @Test
    public void testWithTupleOfTupleWithFunctionBefore() {
        assertParameter("let _ = cpl_s (0, cpl_s (*caret*)(5, 7))", "cpl_s", 0);
    }

    @Test
    public void testWith2TuplesBeforeFunction() {
        assertParameter("let _ = cpl2_s (0, v_i) (\"toto\",(*caret*) max 3 7)", "cpl2_s", 1);
    }

    @Test
    public void testWith2TuplesVariable() {
        assertParameter("let _ = cpl2_s (0, (*caret*)v_i) (\"toto\", max 3 7)", "cpl2_s", 0);
    }

    // weird

    @Test
    public void testFunAsParameterOut() { // todo: what is an argument is a function -> fail
        assertParameter("let _ = g (fun x (*caret*)y -> max x y) 0 5", "g", 0);
    }
}
