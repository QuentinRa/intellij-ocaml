package com.ocaml.sdk.output.errors.unbound;

import com.ocaml.sdk.output.BaseOutputTest;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.junit.Test;

@SuppressWarnings("JUnit4AnnotatedMethodInJUnit3TestCase")
public class UnboundErrorTest extends BaseOutputTest {

    /*
     * constructor.ml
     */

    @Test
    public void testUnboundConstructor() {
        String output = "File \"constructor.ml\", line 1, characters 8-9:\n" +
                "1 | let x = A\n" +
                "            ^\n" +
                "Error: Unbound constructor A";
        CompilerOutputMessage message = parseError(output);
        assertIsFile(message, "constructor.ml", 1, 1, 8, 9);
        assertIsContent(message, "Error: Unbound constructor A");
        assertIsContext(message,  "1 | let x = A\n" + "            ^\n");
    }

    /*
     * constructor_type.ml
     */

    @Test
    public void testUnboundConstructorType() {
        String output = "File \"constructor_type.ml\", line 1, characters 8-9:\n" +
                "1 | let x : t = 0\n" +
                "            ^\n" +
                "Error: Unbound type constructor t";
        CompilerOutputMessage message = parseError(output);
        assertIsFile(message, "constructor_type.ml", 1, 1, 8, 9);
        assertIsContent(message, "Error: Unbound type constructor t");
        assertIsContext(message,  "1 | let x : t = 0\n" + "            ^\n");
    }

    /*
     * open.ml
     */

    @Test
    public void testUnboundModule() {
        String output = "File \"open.ml\", line 1, characters 5-6:\n" +
                "1 | open G\n" +
                "         ^\n" +
                "Error: Unbound module G";
        CompilerOutputMessage message = parseError(output);
        assertIsFile(message, "open.ml", 1, 1, 5, 6);
        assertIsContent(message, "Error: Unbound module G");
        assertIsContext(message,  "1 | open G\n" + "         ^\n");
    }

}
