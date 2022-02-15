package com.ocaml.ide.insight;

import com.intellij.psi.PsiElement;
import com.ocaml.ide.OCamlIdeTest;
import com.or.lang.core.psi.PsiLet;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.io.File;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlFindFunctionTest extends OCamlIdeTest {

    private static final String FILE_NAME = "call.ml";
    private static final String FILE_NAME_ANNOT = "call.annot";

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/";
    }

    private void doTest(@Language("OCaml") String code, String expectedFunctionName, int expectedIndex) {
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

        // todo: ...

        assertEquals(expectedFunctionName, "");
        assertEquals(expectedIndex, -1);
    }

    @Test
    public void testFunctionGenericWithRef() {
        doTest("let _ = id ref (*caret*)0", "id", 0);
    }
}
