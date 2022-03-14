<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

### 1. Run the installer

Simply download the [OCaml64 installer](https://fdopen.github.io/opam-repository-mingw/installation/), that will install and configure opam on cygwin for you. 

### 2. Install a new version of ocaml

Once you are done, you may call

* Run `C:\\OCam64\\Cygwin.bat`
* And `opam switch create 4.12.0` to install `ocaml 4.12.0`

### 3. Test

* Enter the command `ocaml -vnum`

If it outputs your ocaml version, then you are done ✅.

### 4. Call OCaml64 commands from a PowerShell / cmd

You need to ensure that the folder C:/OCaml64/bin is in the path (it should be the case, if the installer did its job).