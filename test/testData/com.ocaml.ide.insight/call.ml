let id s = s
let f x y = x + y
let g f x y = f x y
let v_i = 5
let v_f = 5.0

(* missing param *)
let _ = id ref 0
let _ = id !(ref id) 0
let _ = id id ref 0
let _ = id ()
let _ = id "something"
let _ = id v_f
let _ = id true
let _ = id 5.0
let _ = id not true
let _ = f v_i 0
let _ = f v_i v_i
let _ = f 0 v_i
let _ = f v_i 0
let _ = f 5 0
let _ = f (0) 5
let _ = f (0) (5)
let _ = (f) (0) (5)
let _ = f 0 (* some comment *) 5
let _ = f (f 0 5) 0
let _ = g f (f 0 5) 0
let _ = g max 0 5
(* using UpperIdentifier *)
let _ = g Stdlib.max 0 5
let _ = g (fun x y -> max x y) 0 5

(* couples *)
let cpl_s (x, y) = x + y
let _ = cpl_s (3,2)
let _ = cpl_s (0, 3)

(* lists *)
let hd l _ = List.hd l
let _ = hd [0;3;5] 0
let _ = hd [0;3;5] 7
let _ = List.hd [0;3;5]

(* Labeled *)
(*let f ~x ~y = x - y*)
(*let _ = f ~x: 3 ~y: 5*)

(* optional *)
(*let bump ?(step = 1) x = x + step*)
(*let _ = bump 2*)
(*let _ = bump ~step:3 2*)
(*let test ?(x = 0) ?(y = 0) () ?(z = 0) () = (x, y, z)*)
(*let _ = test ~x:2 () ~z:3 ()*)