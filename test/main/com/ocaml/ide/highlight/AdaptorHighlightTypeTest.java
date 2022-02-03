package com.ocaml.ide.highlight;

import com.intellij.build.FilePosition;
import com.intellij.codeInspection.ProblemHighlightType;
import com.ocaml.OCamlBaseTest;
import com.ocaml.ide.highlight.annotations.OCamlMessageAdaptor;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.File;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class AdaptorHighlightTypeTest extends OCamlBaseTest {

    private @NotNull CompilerOutputMessage createCompilerOutputMessage(CompilerOutputMessage.Kind kind) {
        CompilerOutputMessage m = new CompilerOutputMessage();
        m.filePosition = new FilePosition(new File("file.ml"), 0,0);
        m.kind = kind;
        return m;
    }

    // Alert/deprecated

    @Test
    public void testDeprecatedWarning() {
        CompilerOutputMessage m = createCompilerOutputMessage(CompilerOutputMessage.Kind.WARNING);
        m.content = "Warning 3: deprecated: t";
        assertEquals(ProblemHighlightType.LIKE_DEPRECATED, OCamlMessageAdaptor.temper(m).highlightType);
    }

    @Test
    public void testDeprecatedAlert() {
        CompilerOutputMessage m = createCompilerOutputMessage(CompilerOutputMessage.Kind.ALERT);
        m.content = "Alert deprecated: t";
        assertEquals(ProblemHighlightType.LIKE_DEPRECATED, OCamlMessageAdaptor.temper(m).highlightType);
    }

    // Errors/unbound

    @Test
    public void testUnboundConstructor() {
        CompilerOutputMessage m = createCompilerOutputMessage(CompilerOutputMessage.Kind.ERROR);
        m.content = "Error: Unbound constructor A";
        assertEquals(ProblemHighlightType.ERROR, OCamlMessageAdaptor.temper(m).highlightType);
    }

    @Test
    public void testUnboundConstructorType() {
        CompilerOutputMessage m = createCompilerOutputMessage(CompilerOutputMessage.Kind.ERROR);
        m.content = "Error: Unbound type constructor t";
        assertEquals(ProblemHighlightType.ERROR, OCamlMessageAdaptor.temper(m).highlightType);
    }

    @Test
    public void testUnboundModule() {
        CompilerOutputMessage m = createCompilerOutputMessage(CompilerOutputMessage.Kind.ERROR);
        m.content = "Error: Unbound module G";
        assertEquals(ProblemHighlightType.ERROR, OCamlMessageAdaptor.temper(m).highlightType);
    }

    // warnings/missing MLI


    // warnings/unused

    @Test
    public void testUnusedVariable() {
        CompilerOutputMessage m = createCompilerOutputMessage(CompilerOutputMessage.Kind.WARNING);
        m.content = "Warning 27: unused variable v.";
        assertEquals(ProblemHighlightType.LIKE_UNUSED_SYMBOL, OCamlMessageAdaptor.temper(m).highlightType);
    }

}
