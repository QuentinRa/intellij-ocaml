### Unbound module Stdlib

if you got the error [Unbound module Stdlib](https://discuss.ocaml.org/t/unbound-module-stdllib/5133), ensure that the path to the ocaml library is valid (see the path used by `ocamlc -config`). If you need to change it, set the environment variable called `OCAMLLIB`(ex: `"C:\Users\username\Desktop\4.13.1+mingw64c\lib\ocaml"`).

**THIS IS DONE BY THE PLUGIN for Cygwin SDKs**, fill an issue if needed for others. If you are running `ocaml` without the plugin, you have to set it yourself.