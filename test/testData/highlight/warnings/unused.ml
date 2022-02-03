(* unused value *)
let x = 5
let x = 7

(* unused rec *)
let rec y = 7

(* unused variable *)
let f1 v = match v with
| Some(v) -> 5
| None -> 3

let f2 v = match v with
| Some v -> 5
| None -> 3

type me = A of int * int
let f3 (v: me) = match v with
| A ( v, _ ) -> 5

let f4 (v) = match v with
| Some ( v ) -> 5
| None -> 5

(* unused match case *)
let g v = match v with
| Some(_) -> 5
| Some(_)-> 7
| None -> 0

(* Unused constructor *)

(* Unused constructor to build values *)