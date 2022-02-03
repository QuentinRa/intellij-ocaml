type t = int [@@ocaml.deprecated]
let _ : t = 5

(*
File "file.ml", line 2, characters 8-9:
Warning 3: deprecated: t

File "file.ml", line 2, characters 8-9:
2 | let _ : t = 5
            ^
Alert deprecated: t
*)