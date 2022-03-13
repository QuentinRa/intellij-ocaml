# Dependencies

Files used when I tested resolving dependencies.

```ocaml
(* hello_world.ml *)
let hello_world () = Format.printf "Hello, World!@."
```

```ocaml
(* hello_world.mli *)
open Toto

val hello_world : unit -> unit
```

```ocaml
(* tata.ml *)
exception Tata
```

```ocaml
(* test_hello_world.ml *)
open Hello_world
open Alone

include Hello_world
let v = hello_world ()
let v = Hello_world.hello_world ()
let v = Hello_world.hello_world ()

module Test(X: Set.OrderedType) = struct
end
```

```ocaml
(* toto.ml *)
open Tata

exception Ok
```

```ocaml
(* toto.mli *)
exception Ok
```