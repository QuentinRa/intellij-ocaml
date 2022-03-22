## ParameterInfo - tests that must be written

```ocaml
let _ = max (5 + max 5 7) (7 + 3)(*cursor*)
let _ = max (5 + max 5 7) (7 + 3(*cursor*))
let _ = max (5 + max 5 7(*cursor*)) (7 + 3)
let _ = max (5 +(*cursor*) max 5 7) (7 + 3)
let _ = max (5(*cursor*) + max 5 7) (7 + 3)
```

```ocaml
let id v(*cursor*) = v
let _ = id max 5(*cursor*) 7
let _ = id max (*cursor*)5 7
let _ = id m(*cursor*)ax 5 7
```

```ocaml
let f ~x:x1 ~y:y1 = x1 - int_of_float y1
let _ = f ~x:3 ~y:2.0(*cursor*)
let _ = f ~x:3(*cursor*) ~y:2.0
let f ~x:x1 ~y:y1 z1 = x1 - int_of_float y1 + z1
let _ = f ~x:3 ~y:2.0 3(*cursor*)
let _ = f 3 ~x:3 ~y:2.0(*cursor*)
let _ = f (*cursor*)3 ~x:3 ~y:2.0
let f ~x ~y = x - int_of_float y
let _ = f ~y:2.0 ~x:3(*cursor*)
let _ = f ~y:2.0 ~x(*cursor*):3
let _ = f (*cursor*)~y:2.0 ~x:3
```

```ocaml
let bump ?(step = 1) x = x + step
let _ = bump 3(*cursor*)
let _ = bump (*cursor*)3
let _ = bump ~step:2 3(*cursor*)
let _ = bump ~step:2(*cursor*) 3
let _ = bump 3(*cursor*) ~step: 2
let _ = bump 3 ~step: 2(*cursor*)

let bump ?(s1 = 1) ?(s2 = 1) x = x + s1 + s2
let _ = bump 3 ~s1: 2 ~s2: 2(*cursor*)
let _ = bump 3 ~s1: 2(*cursor*) ~s2: 2
let _ = bump 3(*cursor*) ~s1: 2 ~s2: 2
let _ = bump ~s1: 2 ~s2: 2 5(*cursor*)
let _ = bump ~s1: 2 ~s2: 2(*cursor*) 5
let _ = bump ~s1: 2(*cursor*) ~s2: 2 5
let _ = bump ~s1: 2 55 ~s2: 2(*cursor*)
let _ = bump ~s1: 2 55(*cursor*) ~s2: 2
let _ = bump ~s1: 2(*cursor*) 55 ~s2: 2

let _ = bump(*cursor*) 3 ~s1: 2 ~s2: 2
let _ = bump(*cursor*) ~s1: 2 ~s2: 2 5
let _ = bump(*cursor*) ~s1: 2 55 ~s2: 2

let _ = bump(*cursor*)
let _ = bump (*cursor*)5
let _ = bump 5(*cursor*)
```