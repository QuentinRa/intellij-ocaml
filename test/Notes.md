## ParameterInfo - tests that must be written

```ocaml
let _ = (*cursor*)max 5 7
let _ = max(*cursor*) 5 7
let _ = max 5(*cursor*) 7
let _ = max 5 7(*cursor*)
```

```ocaml
let _(*cursor*) = max 5 7
let _ = max (*cursor*)5 7
```

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

Need to be tested with optional/default and labels.