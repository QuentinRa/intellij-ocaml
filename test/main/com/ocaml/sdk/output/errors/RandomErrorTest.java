package com.ocaml.sdk.output.errors;

import com.ocaml.sdk.output.BaseOutputTest;

/*
type t = A | B

module rec A: sig type t += A end = struct type t += A = B.A end

File "file.ml", line 3, characters 18-29:
Error: Type definition t is not extensible

File "file.ml", line 3, characters 18-29:
3 | module rec A: sig type t += A end = struct type t += A = B.A end
                      ^^^^^^^^^^^
Error: Type definition t is not extensible

 */
public class RandomErrorTest extends BaseOutputTest {
}
