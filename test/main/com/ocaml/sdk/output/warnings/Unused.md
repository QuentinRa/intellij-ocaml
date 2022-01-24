# Unused

## unused value

```ocaml
let x = 5
let x = 7
```

## unused rec

```ocaml
let rec x = 7
```

## unused variable

```ocaml
let f v = match v with
| Some(v) -> 5
| Some(_)-> 7
| None -> 3
```

## unused match case

```ocaml
let f v = match v with
| Some(v) -> 5
| Some(_)-> 7
| None -> 3
```