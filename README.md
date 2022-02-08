# intellij-ocaml

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/) (but, development is in progress)

This plugin is providing support for OCaml in IntelliJ. It's based on ReasonML and intellij-rust. If you are looking for a complete and maintained solution, please check the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) plugin. I'm only a student, after all.

## 🎯 What's my goal?

* [ ] We must be able to run/compile ocaml files without having to install Dune, Esy, or BuckleScript (if we have installed ocaml)
* [ ] Support for opam
* [ ] Support for Makefile
* [ ] Support for Dune (already supported in ReasonML)
* [x] Any installation must be simple, and effortless
* [ ] **Real-time checks for errors/warnings**, including quick fixes
* [ ] **Real-time type inference**
* [ ] **Smart completion**
* [x] **REPL support**: we must be able to send commands from our file, to a REPL console
* [x] **Using a bundle** (possible support for Localization later)

Originally, I was planning to make things, so that we can use this plugin outside IntelliJ, but now my goal is to at least make this plugin work in IntelliJ before targeting minor IDEs.

> Please note that some features are already available in ReasonML (ex: support of odoc, dune support, etc.), and I will "import" them.

## ✨ How will I do that?

On Windows, one may use WSL, OCaml64, or a WSL. Both ocaml installed with, or without opam should work. For the latter, some features won't be available, as some files will be missing.

On Linux, you can also use ocaml installed with, or without opam.

Finally, I'm planning to allow someone to compile on a remote host. This is an experimental feature (from the classes I inspected), so I'm not sure how it will go, but I will give it a try.

## 🚀 Features

| Features coming from ReasonML 🤩️                |
|--------------------------------------------------|
| File Structure menu                              |
| Simple autocompletion                            |
| Go to file, declaration, etc.                    |
| Comment line/block                               |
| Insert matching brace, quote                     |
| Find usages                                      |
| Preview `odoc` documentation (CTRL-Q / hover)    |
| ✅ Live templates (**merged**)                    |
| ✅ Spellchecker (**merged**)                      |
| ❌ Highlight for "not", strings in comments (bug) |
| ❌ Highlight for odoc (+issues with the resolver) |

| Setup                                                      |
|------------------------------------------------------------|
| ✅ You got some instructions to install what you need       |
| ✅ You can create a non-opam SDK                            |
| ✅ You can create/add/use an opam SDK                       |
| ✅ Set/Update the project SDK, you can use it in a module   |
| ✅ Set/Update the SDK for a module                          |
| ✅ Edit modules' properties (ex: output folder, src folder) |
| ✅ You can use a template (✅ Makefile, ✅ Dune)              |
| ✅ Can create a file (.ml, .mli, .ml+.mli)                  |
| ❌ The project is created with a default runConfiguration   |

You are now able to create a project using an opam SDK, or create an opam-like SDK. Opam like SDKs should be used if you are not using opam (ex: you only have /bin/ocaml, etc.). Two templates are available, one using a <b>Makefile</b>, and another using <b>Dune</b>.

| SDK                                                     |
|---------------------------------------------------------|
| ✅ We are suggesting SDKs                                |
| ✅ We are suggesting a location for non-opam SDKs        |
| ✅ The SDK is verified                                   |
| ✅ The user can add/remove sources                       |
| ✅ The user see sources, only .ml/.mli files are shown   |
| ✅ Suggest SDK if opening a file in a module without SDK |
| ❌ The user can download OCaml                           |
| ❌ The user can download sources                         |

An SDK is a folder (**named after its ocaml version** such as 4.05.0) in which you got a folder `bin` with `ocaml`, ... and a folder `lib` with the sources. The sources (from the GitHub repository), are stored in `.opam-switch/sources/` (optional).

| REPL Console                                                    |
|-----------------------------------------------------------------|
| ✅ Can execute commands                                          |
| ✅ Browse history, use arrow up/down                             |
| ✅ See the values of the variables                               |
| ✅ Send a file to the console                                    |
| ✅ Send the selection to the console                             |
| ✅ Send a statement to the console                               |
| ✅ Send the whole line, if we are selecting a part, with preview |

This is a console allowing the use of the ocaml interpreter more easily. For instance, arrows up/down are now properly handled. You also got a history of your commands.

| Code highlighting                                |
|--------------------------------------------------|
| ✅ .ml / .mli are highlighted (based on ReasonML) |
| ✅ we can see warnings in the file                |
| ✅ we can see errors in the file                  |
| ✅ we can see alerts in the file                  |
| ✅ errors are shown project-wide                  |
| ✅ mli is compiled is present, when in a .ml      |
| ❌ handle files using other modules               |
| ❌ handle dune projects                           |
| ❌ handle Makefile projects                       |

We are compiling the file, and parsing the output, to provide hints in the editor.

| File                                                   |
|--------------------------------------------------------|
| ✅ Can create .ml, .mli, or .ml and .mli                |
| ✅ Editor > File and Code Templates for .ml and .mli    |
| ✅ Buttons to browse the OCaml Manual/API in the editor |
| ❌ Live templates (improved, priority, ...)             |


| OCaml                                                  |
|--------------------------------------------------------|
| ✅ Can create .ml, .mli, or .ml and .mli                |
| ✅ Editor > File and Code Templates for .ml and .mli    |
| ✅ Buttons to browse the OCaml Manual/API in the editor |
| ❌ Live templates (improved, priority, ...)             |

| Dune                                                          |
|---------------------------------------------------------------|
| ✅ Import every feature in ReasonML                            |
| ❌ Suggest installing dune if opening a dune file without Dune |
| ❌ Show a message when updating files not targeted             |
| ❌ Show an icon to "reload" dune, reloading runConfig, etc.    |

## 📖 Install ocaml and opam

<details>
<summary>Windows (with WSL, >= 2021.3 only)</summary>

On Windows, you may use a WSL (ex: Windows Store > Debian), then follows the instruction for Linux users
</details>

<details>
<summary>Windows (with cygwin)</summary>

Download [cygwin](https://cygwin.com/install.html). In the installer, you will have to pick some packages to install. Select "full" and pick **ocaml** (if you don't want opam), otherwise, pick

* `opam`
* `make`
* `wget` and `curl`
* `tar` and `unzip`
* `libclang` and `mingw[...]clang` (pick the one according to your OS)

To install new versions of OCaml, run `Cygwin.bat` (in cygwin64 folder), then call `opam switch create 4.12.0`.

Ensure that `C:/cygwin64/bin` (for me) is in the path. If you open a PowerShell, and write `opam --version`, you should be good. This is pretty useless (as the command above does not work in a PowerShell), but you are now able to call commands such as `make` in a PowerShell, so you can use a `Makefile`!

Note that, on Windows, there is a problem with `ocaml`/`ocamlc` ([Unbound module Stdlib](https://discuss.ocaml.org/t/unbound-module-stdllib/5133)). You can find the path where both are looking for Stdlib, with `ocamlc -config`. You can set the PATH using an environment variable called `OCAMLLIB`(ex: `Set-Item -Path Env:OCAMLLIB -Value ("C:\Users\username\Desktop\4.13.1+mingw64c\lib\ocaml")`). **NOTE THAT THIS IS DONE BY THE PLUGIN** (for Cygwin/OCaml64 if needed), so you don't have to do this unless you are not using the plugin.
</details>

<details>
<summary>Windows (Cygwin, installer, mingw)</summary>

Simply download the installer for [OCaml64](https://fdopen.github.io/opam-repository-mingw/installation/). Once installed, you will have your SDK in `C:\\OCam64\\home\\username\\.opam\\`. You will have opam installed, so you can add/versions if you want.
</details>

<details>
<summary>Linux</summary>

In my case, on Linux or Debian, I'm using these commands (you may call `sudo apt-get update` first).

* **ocaml**: `sudo apt-get install ocaml`
* **opam** (recommended): `sudo apt-get install opam` then, you may use `òpam switch create 4.12.0` to install `ocaml 4.12.0`
</details>

<details>
<summary>macOS</summary>

I do not have a computer with a macOS, so you should submit feedback, so that I can update this section. From what I know, you may look around

* `brew update`
* `brew install ocaml`
* `brew install opam`

[Source](https://stackoverflow.com/questions/35563263/install-opam-in-mac-os).
</details>

## 📄 License

This project is [MIT licensed](LICENSE).