package com.ocaml.ide.insight;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.ocaml.ide.OCamlIdeTest;
import com.or.lang.OCamlTypes;
import com.or.lang.core.psi.PsiLet;
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

        Pair<PsiElement, Integer> data = lookForFunction(caret, 0);
        assertNotNull(data);
        assertEquals(expectedFunctionName, data.first.getText());
        assertEquals(expectedIndex, (int) data.second);
    }

    private @Nullable Pair<PsiElement, Integer> lookForFunction(@NotNull PsiElement caret, int index) {
        PsiElement prevSibling = caret.getPrevSibling();
        System.out.println("with:"+prevSibling.getClass());
        // skip spaces and comments
        if (prevSibling instanceof PsiWhiteSpace || prevSibling instanceof PsiComment)
            return lookForFunction(prevSibling, index);
        if (prevSibling instanceof LeafPsiElement) {
            IElementType elementType = ((LeafPsiElement) prevSibling).getElementType();
            // ref <value> is still the same argument
            if (elementType.equals(OCamlTypes.REF)) return lookForFunction(prevSibling, index);
        }

        // name of a function
        if (prevSibling instanceof PsiLowerSymbol)
            return new Pair<>(prevSibling, index);

        // no function
        return null;
    }

    @Test
    public void testFunctionGenericWithRef() {
        assertParameter("let _ = id ref (*caret*)0", "id", 0);
    }
}
