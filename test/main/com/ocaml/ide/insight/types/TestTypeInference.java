package com.ocaml.ide.insight.types;

import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.OCamlIdeTest;
import com.ocaml.ide.insight.OCamlAnnotResultsService;
import com.ocaml.ide.insight.OCamlTypeInfoHint;
import org.intellij.lang.annotations.Language;
import org.junit.Test;

import java.io.File;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class TestTypeInference extends OCamlIdeTest {

    private static final String FILE_NAME = "types.ml";
    private static final String FILE_NAME_ANNOT = "types.annot";

    private void doTest(int line, int column, String expectedType) {
        @Language("OCaml") String code = loadFile(FILE_NAME);
        assertNotNull(code);

        // create file
        PsiFile file = myFixture.configureByText(FILE_NAME, code);

        int offset = myFixture.getEditor().logicalPositionToOffset(new LogicalPosition(line-1, column-1));
        PsiElement elementAt = file.findElementAt(offset);
        assertNotNull(elementAt);

        // load annotations
        OCamlAnnotResultsService annot = getProject().getService(OCamlAnnotResultsService.class);
        File annotations = new File(getTestDataPath(), FILE_NAME_ANNOT);
        annot.updateForFile(elementAt.getContainingFile().getVirtualFile().getPath(), annotations);

        OCamlTypeInfoHint infoHint = new OCamlTypeInfoHint();
        String type = infoHint.getInformationHint(elementAt);
        assertEquals(expectedType, type);
    }

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/types/";
    }

    // The cursor must be before the variable/...
    // in the tests, even if you can use this after the variable/...
    // in the editor. Change the tests if that's a bother.

    @Test
    public void testSimpleVariable1() {
        doTest(1, 5, "int");
    }

    @Test
    public void testSimpleVariable2() {
        doTest(4, 5, "int");
    }

    @Test
    public void testSimpleVariable3() {
        doTest(6, 15, "int");
    }

    @Test
    public void testSimpleVariable4() {
        doTest(10, 8, "int");
    }

    @Test
    public void testFunction1() {
        doTest(12, 5, "'a -> 'a -> 'a");
    }

    @Test
    public void testParam1() {
        doTest(12, 8, "'a");
    }

    @Test
    public void testFunction2() {
        doTest(12, 14, "'a -> 'a -> 'a");
    }

    @Test
    public void testParam2() {
        doTest(12, 20, "'a");
    }

    @Test
    public void testSimpleVariable5() {
        doTest(25, 9, "string");
    }

    @Test
    public void testFunction3() {
        doTest(26, 9, "'a -> 'a");
    }

    @Test
    public void testFunction4() {
        doTest(44, 35, "'a -> 'a -> int");
    }

    // Bugs

    @Test
    public void testBugLongType() {
        doTest(58, 9, "((int -> int -> int) -> int -> int -> int) -> (int -> int -> int) -> int -> int -> int");
    }
}
