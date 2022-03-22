package com.ocaml.ide.insight.parameters;

import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.ocaml.ide.OCamlIdeTest;
import com.ocaml.ide.insight.OCamlParameterInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.Arrays;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class ParameterInfoTest extends OCamlIdeTest {

    @Override protected String getCustomTestDataPath() {
        return "com.ocaml.ide.insight/parameters/";
    }

    private void doTest(@NotNull String text) {
        // load file
        PsiElement caret = configureCodeWithCaret("param.ml", text);
        // load annotations
        configureAnnotResultsService(caret, "param.annot");

        System.out.println(caret);

        FakeCreateParameterInfoContext context = new FakeCreateParameterInfoContext(
                getProject(), caret
        );

        new OCamlParameterInfo().findElementForParameterInfo(context);

        assertNotNull(context.toShow);
        assertSize(1, context.toShow);
        assertInstanceOf(context.toShow[0], OCamlParameterInfo.ParameterInfoArgumentList.class);
        var p = (OCamlParameterInfo.ParameterInfoArgumentList) context.toShow[0];
        System.out.println(p);
    }

    @Test
    public void testA() {
        doTest("let _ = max 5(*caret*) 7");
    }

}
