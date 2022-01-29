package com.ocaml.sdk.output.warnings;

import com.ocaml.sdk.output.BaseOutputTest;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class PatternsWarningTest extends BaseOutputTest {

    @Test
    public void testPatternMatchingNotExhaustiveContext() {
        String output = "File \"file.ml\", line 3, characters 14-36:\n" +
                "3 | ..............match y with\n" +
                "4 | | A -> 0\n" +
                "Warning 8: this pattern-matching is not exhaustive.\n" +
                "Here is an example of a case that is not matched:\n" +
                "(B|C)";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 3, 3, 14, 36);
        assertIsShortMessage(message, "this pattern-matching is not exhaustive");
        assertIsContent(message, "This pattern-matching is not exhaustive.\n" +
                "Here is an example of a case that is not matched:\n" +
                "(B|C)");
        assertIsContext(message,  "3 | ..............match y with\n" + "4 | | A -> 0\n");
    }

}
