package com.ocaml.sdk.output.errors;

import com.ocaml.sdk.output.BaseOutputTest;

/*
module type Something = sig
    type t
	val empty: int
	val add: int -> int
end

module Make (X: Set.OrderedType) : Something with type t = X.t

- ML EMPTY

File "file.ml", line 1:
Error: The implementation file.ml does not match the interface file.cmi:
       The module `Make' is required but not provided
       File "file.mli", line 7, characters 0-62: Expected declaration
       The module type `Something' is required but not provided
       File "file.mli", line 1, characters 0-83: Expected declaration

- ML

module type Something = sig
    type t
	val empty: int
	val add: int -> int
end

module Make (X: Set.OrderedType) : Something with type t = X.t = struct
    let empty = 5
    let add _ = 5
end

File "file.ml", line 7, characters 65-114:
Error: Signature mismatch:
       Modules do not match:
         sig val empty : int val add : 'a -> int end
       is not included in
         sig type t = X.t val empty : int val add : int -> int end
       The type `t' is required but not provided
       File "file.ml", line 7, characters 50-62: Expected declaration

File "file.ml", line 1:
Error: The implementation file.ml does not match the interface file.cmi:
       Module type declarations do not match:
         module type Something =
           sig type t val add : int -> int val empty : int end
       does not match
         module type Something =
           sig type t val empty : int val add : int -> int end
       At position module type Something = <here>
       Illegal permutation of structure fields

Sometimes, we got (4.12.0+)
         For example,
         the value "add" and the value "empty" are not in the same order
         in the expected and actual module types.

File "file.ml", line 1, characters 14-16:
Error: This expression has type float but an expression was expected of type
         int

File "file.ml", line 1, characters 14-16:
1 | let x : int = 0.
                  ^^
Error: This expression has type float but an expression was expected of type
         int

File "file.ml", line 2, characters 14-17:
Error: This expression has type 'a -> float
       but an expression was expected of type int

File "file.ml", line 2, characters 14-17:
2 | let y : int = x 0
                  ^^^
Warning 5 [ignored-partial-application]: this function application is partial,
maybe some arguments are missing.
File "file.ml", line 2, characters 14-17:
2 | let y : int = x 0
                  ^^^
Error: This expression has type 'a -> float
       but an expression was expected of type int
 */
public class MismatchErrorTest extends BaseOutputTest {
}
