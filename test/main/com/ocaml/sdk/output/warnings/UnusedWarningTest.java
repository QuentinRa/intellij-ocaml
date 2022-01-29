package com.ocaml.sdk.output.warnings;

import com.ocaml.sdk.SinceOCamlVersion;
import com.ocaml.sdk.output.BaseOutputTest;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class UnusedWarningTest extends BaseOutputTest {

    /*
     * unused value
     */

    @Test
    public void testUnusedValue() {
        String output = "File \"file.ml\", line 8, characters 6-7:\n" +
                "Warning 32: unused value y.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 8, 8, 6, 7);
        assertIsShortAndContentMessage(message, "unused value y");
    }

    @Test
    public void testUnusedValueContext() {
        String output = "File \"file.ml\", line 1, characters 4-5:\n" +
                "1 | let x = 5\n" +
                "        ^\n" +
                "Warning 32: unused value x.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 4, 5);
        assertIsShortAndContentMessage(message, "unused value x");
        assertIsContext(message, "1 | let x = 5\n" + "        ^\n");
    }

    /*
     * unused rec
     */

    @Test
    public void testUnusedRec() {
        String output = "File \"file.ml\", line 1, characters 8-9:\n" +
                "Warning 39: unused rec flag.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 8, 9);
        assertIsShortAndContentMessage(message, "unused rec flag");
    }

    @Test
    public void testUnusedRecContext() {
        String output = "File \"file.ml\", line 1, characters 8-9:\n" +
                "1 | let rec x = 7\n" +
                "            ^\n" +
                "Warning 39: unused rec flag.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 8, 9);
        assertIsShortAndContentMessage(message, "unused rec flag");
        assertIsContext(message, "1 | let rec x = 7\n" + "            ^\n");
    }

    /*
     * unused variable
     */

    @Test
    public void testUnusedVariable() {
        String output = "File \"file.ml\", line 1, characters 29-32:\n" +
                "Warning 27: unused variable v.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 29, 32);
        assertIsShortAndContentMessage(message, "unused variable v");
    }

    @Test
    public void testUnusedVariableContext() {
        String output = "File \"file.ml\", line 12, characters 6-9:\n" +
                "12 | | Some(v) -> 5\n" +
                "           ^^^\n" +
                "Warning 27: unused variable v.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 12, 12, 6, 9);
        assertIsShortAndContentMessage(message, "unused variable v");
        assertIsContext(message, "12 | | Some(v) -> 5\n" + "           ^^^\n");
    }

    /*
     * unused match case
     */

    @Test
    public void testUnusedMatchCase() {
        String output = "File \"file.ml\", line 1, characters 40-47:\n" +
                "Warning 11: this match case is unused.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 40, 47);
        assertIsShortAndContentMessage(message, "this match case is unused");
    }

    @Test
    public void testUnusedMatchCaseContext() {
        String output = "File \"file.ml\", line 13, characters 2-9:\n" +
                "13 | | Some(v)-> 7\n" +
                "       ^^^^^^^\n" +
                "Warning 11: this match case is unused.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 13, 13, 2, 9);
        assertIsShortAndContentMessage(message, "this match case is unused");
        assertIsContext(message, "13 | | Some(v)-> 7\n" + "       ^^^^^^^\n");
    }

    @Test @SinceOCamlVersion(since = "4.12.0")
    public void testUnusedMatchCaseMnemonic() {
        String output = "File \"file.ml\", line 8, characters 2-9:\n" +
                "8 | | Some(_)-> 7\n" +
                "      ^^^^^^^\n" +
                "Warning 11 [redundant-case]: this match case is unused.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 8, 8, 2, 9);
        assertIsShortAndContentMessage(message, "this match case is unused");
        assertIsContext(message, "8 | | Some(_)-> 7\n" + "      ^^^^^^^\n");
    }

    /*
     * Unused constructor
     */
    @Test
    public void testUnusedConstructorContext() {
        String output = "File \"file.ml\", line 1, characters 0-18:\n" +
                "1 | type t = A | B | C\n" +
                "    ^^^^^^^^^^^^^^^^^^\n" +
                "Warning 37: unused constructor B.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 0, 18);
        assertIsShortAndContentMessage(message, "unused constructor B");
        assertIsContext(message, "1 | type t = A | B | C\n" + "    ^^^^^^^^^^^^^^^^^^\n");
    }

    /*
     * Unused constructor to build values
     */
    @Test
    public void testUnusedConstructorBuildValuesContext() {
        String output = "File \"file.ml\", line 1, characters 0-18:\n" +
                "1 | type t = A | B | C\n" +
                "    ^^^^^^^^^^^^^^^^^^\n" +
                "Warning 37: constructor A is never used to build values.\n" +
                "(However, this constructor appears in patterns.)";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 0, 18);
        assertIsShortMessage(message, "constructor A is never used to build values");
        assertIsContent(message, "Constructor A is never used to build values.\n" +
                "(However, this constructor appears in patterns.)");
        assertIsContext(message, "1 | type t = A | B | C\n" + "    ^^^^^^^^^^^^^^^^^^\n");
    }
}
