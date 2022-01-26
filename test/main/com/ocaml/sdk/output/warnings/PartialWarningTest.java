package com.ocaml.sdk.output.warnings;

import com.ocaml.sdk.output.BaseOutputTest;
import com.ocaml.sdk.output.CompilerOutputMessage;
import org.junit.Test;

/*
4.12.0+???

File "file.ml", line 2, characters 14-17:
2 | let y : int = x 0
                  ^^^
Warning 5 [ignored-partial-application]: this function application is partial,
maybe some arguments are missing.
 */
public final class PartialWarningTest extends BaseOutputTest {

}
