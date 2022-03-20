## ParameterInfo

```ocaml
let _ = (*cursor*)max 5 7
let _ = max(*cursor*) 5 7
let _ = max 5(*cursor*) 7
let _ = max 5 7(*cursor*)
```

```ocaml
let _(*cursor*) = max 5 7
```

```ocaml
let _ = max (5 + max 5 7) (7 + 3)(*cursor*)
let _ = max (5 + max 5 7) (7 + 3(*cursor*))
let _ = max (5 + max 5 7(*cursor*)) (7 + 3)
let _ = max (5 +(*cursor*) max 5 7) (7 + 3)
let _ = max (5(*cursor*) + max 5 7) (7 + 3)
```