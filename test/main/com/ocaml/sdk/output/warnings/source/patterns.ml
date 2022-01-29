(* pattern-matching is not exhaustive *)
type t = A | B | C

let x (y:t) = match y with
| A -> 0

(* ??? *)
type t2 = A | B | C

let x (y:t2) = match y with
| A -> 0
| _ -> 0