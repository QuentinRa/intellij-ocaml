package com.ocaml.ide.insight;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.ocaml.ide.OCamlIdeTest;
import com.ocaml.lang.utils.OCamlPsiUtils;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiLet;
import com.or.lang.core.psi.PsiLiteralExpression;
import com.or.lang.core.psi.PsiLowerSymbol;
import com.or.lang.core.psi.PsiUpperSymbol;
import com.or.lang.core.psi.impl.PsiScopedExpr;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlFindFunctionTest extends OCamlIdeTest {

    private static final String FILE_NAME = "call.ml";
    private static final String FILE_NAME_ANNOT = "call.annot";

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/";
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

        ArrayList<Pair<PsiElement, Integer>> candidates = new ArrayList<>();
        ArrayList<Pair<PsiElement, Integer>> res = lookForFunction(caret, -1, candidates);
        assertTrue(res.size() >= 1);
        Pair<PsiElement, Integer> data = res.get(0);
        assertEquals(expectedFunctionName, data.first.getText());
        assertEquals(expectedIndex, (int) data.second);
    }

    private @NotNull ArrayList<Pair<PsiElement, Integer>> lookForFunction(@NotNull PsiElement caret,
                                                                           int index,
                                                                           ArrayList<Pair<PsiElement, Integer>> candidates) {
        @Nullable PsiElement prevSibling = caret.getPrevSibling();
        // skip spaces and comments
        if (caret instanceof PsiWhiteSpace || caret instanceof PsiComment)
            return callLookForFunction(prevSibling, index, candidates);

        System.out.println("current is:"+caret.getClass());
        System.out.println("current is:"+caret.getText());
        if (prevSibling != null) System.out.println("  previous will be:"+prevSibling.getClass());

        if (caret instanceof PsiLiteralExpression || caret instanceof PsiScopedExpr)
            return callLookForFunction(prevSibling, index+1, candidates); // value => new argument
        if (caret instanceof LeafPsiElement) {
            IElementType elementType = ((LeafPsiElement) caret).getElementType();
            System.out.println("  with type:"+elementType);

            if (elementType.equals(OCamlTypes.INT_VALUE) || elementType.equals(OCamlTypes.FLOAT_VALUE)
                    || elementType.equals(OCamlTypes.BOOL_VALUE))
                // value => new argument
                return callLookForFunction(prevSibling, index+1, candidates);
            // ref <value> is still the same argument
            if (elementType.equals(OCamlTypes.REF))
                return callLookForFunction(prevSibling, index, candidates);
            if (elementType.equals(OCamlTypes.LPAREN)) {
                boolean isUnit = OCamlPsiUtils.isNextMeaningfulNextSibling(caret, OCamlTypes.RPAREN);
                if (isUnit)
                    // get out of the Scoped expression
                    // unit = value => new argument
                    return callLookForFunction(caret.getParent().getPrevSibling(), index+1, candidates);
            }

            if (elementType.equals(OCamlTypes.RPAREN)) {
                // we are moving from ')' to before '('
                PsiElement newPrev  = OCamlPsiUtils.getPreviousMeaningfulNextSibling(caret, OCamlTypes.LPAREN);
                if (newPrev != null)
                    // get out of the Scoped expression
                    // unit = value => new argument
                    return callLookForFunction(newPrev.getParent().getPrevSibling(), index+1, candidates);
            }

            // that's a variable
            if (elementType.equals(OCamlTypes.LIDENT))
                // value => new argument
                return callLookForFunction(caret.getParent().getPrevSibling(), index+1, candidates);
        } else // cannot be function if it's a token

        // name of a function
        if (caret instanceof PsiLowerSymbol) {
            if (prevSibling instanceof LeafPsiElement && ((LeafPsiElement) prevSibling).getElementType().equals(OCamlTypes.DOT)) {
                // Module.something
                if (prevSibling.getPrevSibling() instanceof PsiUpperSymbol) {
                    // skip
                    prevSibling = prevSibling.getPrevSibling().getPrevSibling();
                }
            }
            candidates.add(0, new Pair<>(caret, index));
            return callLookForFunction(prevSibling, index+1, candidates);
        }

        // at the end, because it could have been unit for ()
        // and we may not have to do anything, if we were inside this scoped expression,
        // and we found a candidate
        if (candidates.isEmpty() && caret.getParent() instanceof PsiScopedExpr) {
            System.out.println("c:"+candidates);
            // this expression is a new argument
            return callLookForFunction(caret.getParent().getPrevSibling(), index+1, candidates);
        }

        // no function
        return candidates;
    }

    private @NotNull ArrayList<Pair<PsiElement, Integer>> callLookForFunction(@Nullable PsiElement prevSibling, int index,
                                                                               ArrayList<Pair<PsiElement, Integer>> candidates) {
        return prevSibling == null ? candidates : lookForFunction(prevSibling, index, candidates);
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
    public void testWithNot() { // todo: actually, it's not 1, but 0???
        // id got the type (bool -> bool) -> bool -> bool
        // meaning id f b -> b (but, actually, id is 'a -> 'a)
        // ... I'm kinda lost
        assertParameter("let _ = id not (*caret*)true", "id", 1);
    }

    @Test
    public void testWithVariable() {
        assertParameter("let _ = id (*caret*)v_f", "id", 0);
    }
}
