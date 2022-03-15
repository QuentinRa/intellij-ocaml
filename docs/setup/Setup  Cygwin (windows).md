<i>

Note: you can always help me to improve the documentation by making a [Pull Request](https://github.com/QuentinRa/intellij-ocaml/docs).

</i>

### 1. Download [cygwin installer](https://www.cygwin.com/install.html)

### 2. Run the installer

You can press "next" a lot of times. Once you need to set cygwin location, please note that **cygwin folder must be located at the root of one of your hard drives**, **and must be named either `cygwin64` or `cygwin`** (**You may ask for this to be changed by opening an issue if this isn't convenient for you**).

![Run Cygwin Installer](https://raw.githubusercontent.com/QuentinRa/intellij-ocaml/main/docs/setup/_images/062c01d7-7a6e-4497-94be-5e4e7be6ad24.png)

* Valid locations: `C:\cygwin64`, `D:\cygwin`
* Invalid locations: `C:\Programs\cygwin64`, `D:\Programs\cygwin` (you may request for some "paths" to be allowed)

## 3. Go To Select Packages

Continue to press next until you are on this page, and switch the view mode to "full".

![Select Packages Cygwin](https://raw.githubusercontent.com/QuentinRa/intellij-ocaml/main/docs/setup/_images/45e14ea5-77c1-4dbd-a132-44d6f38ebe74.png)

### 4. Install OCaml

You can install **a native SDK** (if you're not using opam), **opam** (**recommended**), or **both**.

## Opam SDKs

1. search opam, and pick a version in the column "new"
2. if you don't want a native SDK, search "ocaml" and set back the version in "new" to "skip"

## Native SDKs

* search ocaml, and pick a version in the column "new"

## Note

You may also look for commands you may use. I'm personally picking "make", "wget", "tar", "curl", "unzip", "libclang" and "mingw[...]clang" (ex: mingw64-i686-clang), just in case.

### 5. Configure opam (opam only)

If you are using opam, you now have to download ocaml. Run **cygwin.bat** (ex: `C:\cygwin64\Cygwin.bat`). Enter the following commands (press enter after each command)

* `opam init`
* `opam switch create 4.13.0` to install ocaml 4.13.0. Press "y" when prompted.
* `eval $(opam env)`

### 6. Test

* Enter the command `ocaml -vnum`

If it outputs your ocaml version, then you are done ✅.

### 7. Call cygwin commands from a PowerShell / cmd

You need to ensure that the folder `C:/cygwin64/bin` is in the path.