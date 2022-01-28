(* unused value *)
let x = 5
let x = 7

(* unused rec *)
let rec y = 7

(* unused variable *)
let f1 v = match v with
| Some(v) -> 5
| None -> 3

(* unused match case *)
let f2 v = match v with
| Some(_) -> 5
| Some(_)-> 7
| None -> 0

(* none *)
type t1 = A | B | C

let f3 (t: t1) = match t with
| 0 -> A
| _ -> A