version=4.08.0
comp="$HOME/.opam/$version/bin/ocamlc"
# shellcheck disable=SC2139
alias ocamlc="$comp"
cd test/main/com/ocaml/sdk/output/ || return
$comp -version
alias comp='$comp -c -w +A file.ml && rm -rf *.cm*'
alias comp2='$comp -c -w +A file.mli'
alias comp_='(comp2 && comp) || rm -rf *.cm*'
clear
comp
