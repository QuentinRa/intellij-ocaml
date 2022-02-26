package com.ocaml.ide.insight.types;

import com.intellij.psi.PsiElement;
import com.ocaml.OCamlBaseTest;
import com.ocaml.lang.utils.OCamlAnnotUtils;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

@Ignore @Deprecated @SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class OCamlAnnotUtilsTest extends OCamlBaseTest {

    public void assertInvalid(@Language("OCaml") @NotNull String code) {
        PsiElement psiElement = configureCodeWithCaret(code);
        System.out.println("e:"+psiElement);
        List<PsiElement> expressions = OCamlAnnotUtils.findExpressionsForAnnotAt(psiElement);
        assertEmpty(expressions);
    }

    public void assertValid(@Language("OCaml") @NotNull String code) {
        PsiElement psiElement = configureCodeWithCaret(code);
        System.out.println("e:"+psiElement);
        System.out.println("  text:"+psiElement.getText());
        List<PsiElement> expressions = OCamlAnnotUtils.findExpressionsForAnnotAt(psiElement);
        assertSize(1, expressions);
    }

    @Test
    public void testLet() {
        assertInvalid("let(*caret*) x = 5");
        assertValid("let x(*caret*) = 5");
        assertValid("let _(*caret*) = 5");
        assertInvalid("let x =(*caret*) 5");
        assertValid("let x = 5(*caret*)");
    }

    @Test
    public void testLiterals() {
        assertValid("true(*caret*)");
        assertValid("false(*caret*)");
        assertValid("5(*caret*)");
        assertValid("5.0(*caret*)");
        assertValid("\"Hello, World!\"(*caret*)");
        assertValid("()(*caret*)");
        assertValid("{ v = 5 }");
    }

    @Test
    public void testFunctions() {
        assertValid("let f1(*caret*) x y = ()");
        assertValid("let f1 x y(*caret*) = ()");
        assertValid("let f2 = fun x -> fun y(*caret*) -> ()");
        assertInvalid("let f2 = fun(*caret*) x -> fun y -> ()");
    }

    @Test
    public void testFunctionFun() {
        assertValid("let derivative dx f = fun x(*caret*) -> (f (x +. dx) -. f x) /. dx");
    }

    @Test
    public void testList() {
        assertValid("let li = 1 :: 2 :: 3 :: [](*caret*)");
        assertValid("[1; 2; 3](*caret*)");
    }

    @Test
    public void testMatch() {
        @Language("OCaml") String code = "let rec eval env = function\n" +
                "    | Num(*1*) i(*2*) -> i(*3*)\n" +
                "    | Var x -> List(*4*).assoc(*5*) x(*6*) env\n" +
                "    | Let (x(*7*), e1, in_e2) ->\n" +
                "       let val_x = eval env (*8*)e1 in\n" +
                "       eval ((x, val_x(*9*)) :: env) in_e2\n" +
                "    | Binop (op, e1, e2) ->\n" +
                "       let v1 = eval env e1 in\n" +
                "       let v2 = eval env e2 in\n" +
                "       eval_op(*10*) op v1 v2\n" +
                "  and eval_op(*11*) op(*12*) v1 v2 =\n" +
                "    match op(*22*) with\n" +
                "    | \"+\" -> v1(*13*) +(*14*) v2\n" +
                "    | \"-\"(*15*) -> v1 -(*16*) v2\n" +
                "    | \"*\" -> v1 *(*17*) v2\n" +
                "    | \"/\" -> v1 /(*18*) v2\n" +
                "    | _(*23*) -> failwith (\"Unknown operator: \"(*19*) ^(*20*) op(*21*));;";

        for (int i = 1; i <= 22; i++) {
            assertValid(code.replace("(*"+i+"*)", OCAML_CARET));
        }
    }

}
