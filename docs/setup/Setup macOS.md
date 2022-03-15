<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

You can install **a native SDK** (if you're not using opam), **opam** (**recommended**), or **both**.
**I do not have a computer with a macOS, so you should submit feedback, so that I can update this section**. This [issue](https://stackoverflow.com/questions/35563263/install-opam-in-mac-os) may help.

### 1. Native SDK (non-opam users only)


* `brew update`
* `brew install ocaml`

### 2. Configure opam (opam users only)

* `brew update`
* `brew install opam`
* `opam init`
* `opam switch create 4.13.0` to install ocaml 4.13.0. Press "y" when prompted.
* `eval $(opam env)`

### 3. test

* Enter the command `ocaml -vnum`

If it outputs your ocaml version, then you are done ✅.