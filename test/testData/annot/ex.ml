(*
~/.opam/4.13.0/bin/ocamlc test.ml -annot && rm -rf *.cmo *.cmi a.out
~/.opam/rincewind/_build/default/bin/rincewind.exe test.cmt
*)

let x = function ()
| _ -> ()

(*
bug
let () = Format.printf "ok";;
*)

class type name = object end

(*module Stack = struct
  class ['a] stack init = object
  end

  type 'a t = 'a stack

  let make init = new stack init
end*)

class stack_of_ints =
    object (self)
      val mutable the_list = ([] : int list)     (* instance variable *)
    end;;

let s = new stack_of_ints;;

(* val s : stack_of_ints = <obj> *)