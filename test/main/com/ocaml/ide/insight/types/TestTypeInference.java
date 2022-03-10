package com.ocaml.ide.insight.types;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.OCamlIdeTest;
import com.ocaml.ide.insight.OCamlAnnotResultsService;
import com.ocaml.ide.insight.OCamlTypeInfoHint;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class TestTypeInference extends OCamlIdeTest {

    private static final String FILE_NAME = "types.ml";
    private static final String FILE_NAME_ANNOT = "types.annot";

    private void doTest(@NotNull String text, String expectedType) {
        @Language("OCaml") String code = loadFile(FILE_NAME);
        assertNotNull(code);

        // create file
        PsiFile file = myFixture.configureByText(FILE_NAME, code);
        int i = file.getText().indexOf(text.replace("(*caret*)", ""));
        i += text.indexOf("(*caret*)")-1;
        PsiElement caret = file.findElementAt(i);
        assertNotNull(caret);

        // load annotations
        OCamlAnnotResultsService annot = getProject().getService(OCamlAnnotResultsService.class);
        File annotations = new File(getTestDataPath(), FILE_NAME_ANNOT);
        annot.updateForFile(caret.getContainingFile().getVirtualFile().getPath(), annotations);

        OCamlTypeInfoHint infoHint = new OCamlTypeInfoHint();
        String type = infoHint.getInformationHint(caret);
        if (expectedType == null) expectedType = OCamlTypeInfoHint.UNKNOWN_TYPE;
        System.out.println("  for "+text+" exp"+expectedType+" was "+type);
        assertEquals(expectedType, type);
    }

    private void assertInvalid(String text) {
        doTest(text, null);
    }

    private void assertValid(String text, String expectedType) {
        doTest(text, expectedType);
    }

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/types/";
    }

    // The cursor must be before the variable/...
    // in the tests, even if you can use this after the variable/...
    // in the editor. Change the tests if that's a bother.

    @Test
    public void testLet() {
        assertInvalid("let(*caret*) x = 5");
        assertValid("let x(*caret*) = 5", "int");
        assertValid("let _(*caret*) = 5", "int");
        assertInvalid("let x =(*caret*) 5");
        assertValid("let x = 5(*caret*)", "int");
    }

    @Test
    public void testLiterals() {
        assertValid("true(*caret*)", "bool");
        assertValid("false(*caret*)", "bool");
        assertValid("5(*caret*)", "int");
        assertValid("5.0(*caret*)", "float");
        assertValid("\"Hello, World!\"(*caret*)", "string");
        // todo: assertValid("()(*caret*)", "unit");
        // todo: assertValid("{ n = 5 }(*caret*)", "s");
    }

    @Test
    public void testFunctions() {
        assertValid("let f1(*caret*) x y = ()", "'a -> 'b -> unit");
        assertValid("let f1 x y(*caret*) = ()", "'b");
        assertValid("let f2 = fun x -> fun y(*caret*) -> ()", "'b");
        assertInvalid("let f2 = fun(*caret*) x -> fun y -> ()");
    }

    @Test
    public void testFunctionFun() {
        assertValid("let derivative dx f = fun x(*caret*) -> (f (x +. dx) -. f x) /. dx",
                "float");
    }

    @Test
    public void testList() {
        // todo: fix
        assertInvalid("let li = 1::2::3::[](*caret*)");
        assertInvalid("[1; 2; 3](*caret*)");
    }

    @Test
    public void testMatch() {
        assertInvalid("| Num(*caret*) i -> i"); // todo: ...
        assertValid("| Num i(*caret*) -> i", "int");
        assertValid("| Num i -> i(*caret*)", "int");
        assertInvalid("List(*caret*).assoc x"); // todo: ...
        assertInvalid("List.assoc(*caret*) x"); // todo: ...
        assertValid("List.assoc x(*caret*)", "string");
        assertValid("(x(*caret*), e1, in_e2)", "string");
        assertValid("eval env e1(*caret*)", "expr");
        assertValid("((x, val_x(*caret*))", "int");
        assertValid("eval_op(*caret*) op v1 v2", "string -> int -> int -> int");
        assertValid("eval_op(*caret*) op v1 v2", "string -> int -> int -> int");
        assertValid("eval_op op(*caret*) v1 v2", "string");
        assertValid("v1(*caret*) + v2", "int");
        assertValid("v1 +(*caret*) v2", "int -> int -> int");
        assertValid("\"-\"(*caret*) -> v1 - v2", "string");
        assertValid("\"-\" -> v1 -(*caret*) v2", "int -> int -> int");
        assertValid("\"*\" -> v1 *(*caret*) v2", "int -> int -> int");
        assertValid("\"/\" -> v1 /(*caret*) v2", "int -> int -> int");
        assertValid("(\"Unknown operator: \"(*caret*) ^ op)", "string");
        assertValid("(\"Unknown operator: \" ^(*caret*) op)", "string -> string -> string");
        assertValid("(\"Unknown operator: \" ^ op(*caret*))", "string");
        assertValid("match op(*caret*) with", "string");
        assertValid("| _(*caret*) ->", "string");
    }
}
