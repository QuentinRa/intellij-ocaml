package com.ocaml.sdk.output.warnings;

import com.ocaml.sdk.output.BaseOutputTest;

/*
type t = A | B | C

let x (y:t) = match y with
| A -> 0

File "file.ml", line 3, characters 14-36:
3 | ..............match y with
4 | | A -> 0
Warning 8: this pattern-matching is not exhaustive.
Here is an example of a case that is not matched:
(B|C)

 */
public class RandomWarningTest extends BaseOutputTest {
}
