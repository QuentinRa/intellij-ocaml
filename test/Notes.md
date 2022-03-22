## ParameterInfo - tests that must be written

```ocaml
let bump ?(step = 1) x = x + step
let _ = bump 3(*caret*)
let _ = bump (*caret*)3
let _ = bump ~step:2 3(*caret*)
let _ = bump ~step:2(*caret*) 3
let _ = bump 3(*caret*) ~step: 2
let _ = bump 3 ~step: 2(*caret*)

let bump ?(s1 = 1) ?(s2 = 1) x = x + s1 + s2
let _ = bump 3 ~s1: 2 ~s2: 2(*caret*)
let _ = bump 3 ~s1: 2(*caret*) ~s2: 2
let _ = bump 3(*caret*) ~s1: 2 ~s2: 2
let _ = bump ~s1: 2 ~s2: 2 5(*caret*)
let _ = bump ~s1: 2 ~s2: 2(*caret*) 5
let _ = bump ~s1: 2(*caret*) ~s2: 2 5
let _ = bump ~s1: 2 55 ~s2: 2(*caret*)
let _ = bump ~s1: 2 55(*caret*) ~s2: 2
let _ = bump ~s1: 2(*caret*) 55 ~s2: 2

let _ = bump(*caret*) 3 ~s1: 2 ~s2: 2
let _ = bump(*caret*) ~s1: 2 ~s2: 2 5
let _ = bump(*caret*) ~s1: 2 55 ~s2: 2

let _ = bump(*caret*)
let _ = bump (*caret*)5
let _ = bump 5(*caret*)
```