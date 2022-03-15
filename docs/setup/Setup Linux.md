<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

You can install **a native SDK** (if you're not using opam), **opam** (**recommended**), or **both**.
**Please note that you may have to user another package manager aside from "apt-get", and you may have to find an alternative to sudo (run as administrator)**.

### 1. Native SDK (non-opam users only)

* `sudo apt-get update`
* `sudo apt-get upgrade`
* `sudo apt-get install ocaml`

### 2. Configure opam (opam users only)

* `sudo apt-get update`
* `sudo apt-get install opam`
* `opam init`
* `opam switch create 4.13.0` to install ocaml 4.13.0. Press "y" when prompted.
* `eval $(opam env)`

### 3. test

* Enter the command `ocaml -vnum`

If it outputs your ocaml version, then you are done ✅.