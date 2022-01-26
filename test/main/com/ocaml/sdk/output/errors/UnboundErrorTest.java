package com.ocaml.sdk.output.errors;

import com.ocaml.sdk.output.BaseOutputTest;

/*
open G

File "file.ml", line 1, characters 5-6:
Error: Unbound module G

File "file.ml", line 1, characters 5-6:
1 | open G
         ^
Error: Unbound module G

let x = A

File "file.ml", line 1, characters 8-9:
Error: Unbound constructor A

let x : int = 0.

File "file.ml", line 1, characters 8-9:
1 | let x = A
            ^
Error: Unbound constructor A

File "file.ml", line 2, characters 8-9:
Error: This expression has type float
       This is not a function; it cannot be applied.

File "file.ml", line 2, characters 8-9:
2 | let y = x 0
            ^
Error: This expression has type float
       This is not a function; it cannot be applied.

let x : t = 0

File "file.ml", line 1, characters 8-9:
Error: Unbound type constructor t

File "file.ml", line 1, characters 8-9:
1 | let x : t = 0
            ^
Error: Unbound type constructor t
 */
public class UnboundErrorTest extends BaseOutputTest {
}
