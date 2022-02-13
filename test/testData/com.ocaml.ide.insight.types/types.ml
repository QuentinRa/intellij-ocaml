let x = 5

let x = 5
let y = 5

let x = 5 and y = 5

let x =
    let y = 5
    in y

let mx a b = max a b

module type A

module Make1(F: A) = struct end

module Make2 = struct
    let x = 5
end

type t = int
exception E of int * int

let _ = "Hello, world!"
let rec f x = x

let x () = function
| _ -> ()

class type name = object
	method name : int * int
end


class stack_of_ints =
    object (self)
      val mutable the_list = ([] : int list)
    end

let s = new stack_of_ints;;

module E = Set.Make(
struct type t = int let compare = compare end
)

(* small test with a "complex" function *)
let z = fun x ->
    let z = fun y -> x + y
    in z x

(* adding support for lists *)
let list = [[4],5,6,8]