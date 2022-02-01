(* pattern-matching is not exhaustive *)
type t1 = A | B | C

let x (y:t1) = match y with
| A -> 0

(* pattern-matching is fragile *)
type t2 = A | B | C

let y (y:t2) = match y with
| A -> 0
| _ -> 0