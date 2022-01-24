package com.ocaml.sdk.output;

import org.junit.Test;

import java.util.ArrayList;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WarningMessagesTest extends BaseOutputTest {

    /*
All of them got the same pattern

File "file.ml", line 1, characters 4-5:
Warning 32: unused value x.

File "file.ml", line 5, characters 8-9:
Warning 39: unused rec flag.

File "file.ml", line 7, characters 6-9:
Warning 27: unused variable v.

File "file.ml", line 8, characters 2-9:
Warning 11: this match case is unused.
     */
    @Test
    public void testUnusedValue() {
        String output = "File \"file.ml\", line 8, characters 6-7:\n" +
                "Warning 32: unused value y.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 8, 8, 6, 7);
        assertIsShortMessage(message, "unused value y");
    }

    // /////// ////////// ///////////
    //
    //  SINCE OCAML 4.08.0
    //
    // /////// ////////// ///////////

    @Test
    public void testUnusedValueContext() {
        String output = "File \"file.ml\", line 1, characters 4-5:\n" +
                "1 | let x = 5\n" +
                "        ^\n" +
                "Warning 32: unused value x.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 4, 5);
        assertIsShortMessage(message, "unused value x");
        assertIsContext(message, "1 | let x = 5\n" + "        ^\n");
    }

    @Test
    public void testUnusedVariableContext() {
        String output = "File \"file.ml\", line 12, characters 6-9:\n" +
                "12 | | Some(v) -> 5\n" +
                "           ^^^\n" +
                "Warning 27: unused variable v.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 12, 12, 6, 9);
        assertIsShortMessage(message, "unused variable v");
        assertIsContext(message, "12 | | Some(v) -> 5\n" + "           ^^^\n");
    }

    @Test
    public void testUnusedMatchCaseContext() {
        String output = "File \"file.ml\", line 13, characters 2-9:\n" +
                "13 | | Some(v)-> 7\n" +
                "       ^^^^^^^\n" +
                "Warning 11: this match case is unused.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 13, 13, 2, 9);
        assertIsShortMessage(message, "this match case is unused");
        assertIsContext(message, "13 | | Some(v)-> 7\n" + "       ^^^^^^^\n");
    }

    // /////// ////////// ///////////
    //
    //  SINCE OCAML 4.12.0
    //
    // /////// ////////// ///////////

    @Test
    public void testUnusedMatchCaseMnemonic() {
        String output = "File \"file.ml\", line 8, characters 2-9:\n" +
                "8 | | Some(_)-> 7\n" +
                "      ^^^^^^^\n" +
                "Warning 11 [redundant-case]: this match case is unused.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 8, 8, 2, 9);
        assertIsShortMessage(message, "this match case is unused");
        assertIsContext(message, "8 | | Some(_)-> 7\n" + "      ^^^^^^^\n");
    }

}
