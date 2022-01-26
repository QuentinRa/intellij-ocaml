package com.ocaml.sdk.output.errors;

import com.ocaml.sdk.output.BaseOutputTest;

/*
let

File "file.ml", line 1, characters 3-3:
Error: Syntax error

let x = let y = 5 in
        in y

File "file.ml", line 2, characters 8-10:
Error: Syntax error

File "file.ml", line 2, characters 8-10:
2 |         in y
            ^^
Error: Syntax error
 */
public class SyntaxErrorTest extends BaseOutputTest {
}
