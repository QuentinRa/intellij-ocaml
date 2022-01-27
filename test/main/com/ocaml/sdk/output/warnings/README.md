### Warning N1 : stupid if

Before

```ocaml
if something then true else false
```

After

```ocaml
something
```

### Warning N2 : duplicates

Before

```ocaml
type t = A

let x y = match y with
| 0 -> A
| 1 -> A
| 2 -> A
```

After

```ocaml
type t = A

let x y = match y with
| 0 | 1 | 2 -> A
```

### Warning N3 : fragile (existing)

Before

```ocaml
type t = A | B | C

let x y = match y with
| A -> 0
| _ -> 1
| C -> 2
```

After v1

```ocaml
type t = A | B | C

let x y = match y with
| A -> 0
| C -> 2
| _ -> 1
```

After v2

```ocaml
type t = A | B | C

let x y = match y with
| A -> 0
| _ -> 1
```