# Observations

## OCaml OPT

The "ocamlc" installed using opam is usually a link to another file. `File.exists` is considering that the file does not exist. Aside from that, the `GeneralCommandLine` is not working on Windows, as we can't execute `ocamlc`.

An alternative is to use `ocamlc.opt` (resp. `ocamlc.opt.exe`).