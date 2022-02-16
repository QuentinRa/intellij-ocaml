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
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.io.File;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlFindFunctionTest extends OCamlIdeTest {

    private static final String FILE_NAME = "call.ml";
    private static final String FILE_NAME_ANNOT = "call.annot";

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/";
    }

    private void assertParameter(@Language("OCaml") String code, String expectedFunctionName, int expectedIndex) {
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

        Pair<PsiElement, Integer> data = lookForFunction(caret, -1);
        assertNotNull(data);
        assertEquals(expectedFunctionName, data.first.getText());
        assertEquals(expectedIndex, (int) data.second);
    }

    private @Nullable Pair<PsiElement, Integer> lookForFunction(@NotNull PsiElement caret, int index) {
        @Nullable PsiElement prevSibling = caret.getPrevSibling();
        System.out.println("current is:"+caret.getClass());
        System.out.println("current is:"+caret.getText());
        if (prevSibling != null) System.out.println("  previous will be:"+prevSibling.getClass());
        // skip spaces and comments
        if (caret instanceof PsiWhiteSpace || caret instanceof PsiComment)
            return callLookForFunction(prevSibling, index);
        if (caret instanceof PsiLiteralExpression)
            return callLookForFunction(prevSibling, index+1); // value => new argument
        if (caret instanceof LeafPsiElement) {
            IElementType elementType = ((LeafPsiElement) caret).getElementType();
            if (elementType.equals(OCamlTypes.INT_VALUE)
                    || elementType.equals(OCamlTypes.FLOAT_VALUE)
                    || elementType.equals(OCamlTypes.BOOL_VALUE)
            )
                return callLookForFunction(prevSibling, index+1); // value => new argument
            // ref <value> is still the same argument
            if (elementType.equals(OCamlTypes.REF))
                return callLookForFunction(prevSibling, index);
            if (elementType.equals(OCamlTypes.LPAREN)) {
                boolean isUnit = OCamlPsiUtils.isNextMeaningfulNextSibling(caret, OCamlTypes.RPAREN);
                if (isUnit)
                    // get out of the Scoped expression
                    // value => new argument
                    return callLookForFunction(caret.getParent().getPrevSibling(), index+1);
            }

            if (elementType.equals(OCamlTypes.RPAREN)) {
                // we are moving from ')' to before '('
                PsiElement newPrev  = OCamlPsiUtils.getPreviousMeaningfulNextSibling(caret, OCamlTypes.LPAREN);
                if (newPrev != null)
                    // get out of the Scoped expression
                    // value => new argument
                    return callLookForFunction(newPrev.getParent().getPrevSibling(), index+1);
            }
        }

        // name of a function
        if (caret instanceof PsiLowerSymbol)
            return new Pair<>(caret, index);

        // no function
        return null;
    }

    private @Nullable Pair<PsiElement, Integer> callLookForFunction(@Nullable PsiElement prevSibling, int index) {
        return prevSibling == null ? null : lookForFunction(prevSibling, index);
    }

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
}
