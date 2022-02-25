package com.ocaml.lang.utils;

import com.intellij.psi.PsiElement;
import com.ocaml.OCamlBaseTest;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.List;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
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
        List<PsiElement> expressions = OCamlAnnotUtils.findExpressionsForAnnotAt(psiElement);
        assertSize(1, expressions);
    }

    @Test
    public void testLet() {
        assertInvalid("let(*caret*) x = 5");
    }

    @Test
    public void testLetEq() {
        assertInvalid("let x =(*caret*) 5");
    }

    @Test
    public void testLetNameVar() {
        assertValid("let x(*caret*) = 5");
    }

    @Test
    public void testInt() {
        assertValid("let x = 5(*caret*)");
    }

    @Test
    public void testFloat() {
        assertValid("let x = 5.0(*caret*)");
    }

    @Test
    public void testString() {
        assertValid("let x = \"Hello, World!@.\"(*caret*)");
    }

    @Test
    public void testUnit() {
        assertValid("let x = ()(*caret*)");
    }

}
