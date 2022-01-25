package com.ocaml.sdk.output.warnings;

import com.ocaml.sdk.output.BaseOutputTest;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.junit.Test;

/**
 * <h2>unused value</h2>
 * <code>let x = 5</code><br>
 * <code>let x = 7</code><br>
 *
 * <h2>unused rec</h2>
 * <code>let rec x = 7</code>
 *
 * <h2>unused variable</h2>
 *
 * <code>let f v = match v with</code><br>
 * <code>| Some(v) -> 5</code><br>
 * <code>| Some(_)-> 7</code><br>
 * <code>| None -> 3</code>
 *
 * <h2>unused match case</h2>
 *
 * <code>let f v = match v with</code><br>
 * <code>| Some(v) -> 5</code><br>
 * <code>| Some(_)-> 7</code><br>
 * <code>| None -> 3</code>
 */
@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public final class WarningUnusedTest extends BaseOutputTest {

    @Test
    public void testUnusedValue() {
        String output = "File \"file.ml\", line 8, characters 6-7:\n" +
                "Warning 32: unused value y.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 8, 8, 6, 7);
        assertIsShortMessage(message, "unused value y");
    }

    @Test // todo: we should target the rec, not the variable, right?
    public void testUnusedRec() {
        String output = "File \"file.ml\", line 1, characters 8-9:\n" +
                "Warning 39: unused rec flag.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 8, 9);
        assertIsShortMessage(message, "unused rec flag");
    }

    @Test // todo: we should target the variable, not the braces, right?
    public void testUnusedVariable() {
        String output = "File \"file.ml\", line 1, characters 29-32:\n" +
                "Warning 27: unused variable v.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 29, 32);
        assertIsShortMessage(message, "unused variable v");
    }

    @Test // todo: we should target the whole match right?
    public void testUnusedMatchCase() {
        String output = "File \"file.ml\", line 1, characters 40-47:\n" +
                "Warning 11: this match case is unused.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 40, 47);
        assertIsShortMessage(message, "this match case is unused");
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
    public void testUnusedRecContext() {
        String output = "File \"file.ml\", line 1, characters 8-9:\n" +
                "1 | let rec x = 7\n" +
                "            ^\n" +
                "Warning 39: unused rec flag.";
        CompilerOutputMessage message = parseWarning(output);
        assertIsFile(message, "file.ml", 1, 1, 8, 9);
        assertIsShortMessage(message, "unused rec flag");
        assertIsContext(message, "1 | let rec x = 7\n" + "            ^\n");
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
