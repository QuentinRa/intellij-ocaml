# intellij-ocaml

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

This plugin is providing support for OCaml in IntelliJ. It's based on ReasonML and intellij-rust. If you are looking for a complete and maintained solution, please check the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) plugin.

## 🎯 What's my goal?

* [ ] We must be able to run/compile ocaml files without having to install Dune, Esy, or BuckleScript (if we have installed ocaml)
* [ ] Support for opam
* [ ] Support for Makefile
* [ ] Support for Dune (already supported in ReasonML)
* [ ] Any installation must be simple, and effortless
* [ ] **Real-time checks for errors/warnings**, including quick fixes
* [ ] **Real-time type inference**
* [ ] **Smart completion**
* [ ] **REPL support**: we must be able to send commands from our file, to a REPL console
* [ ] **Using a bundle** (possible support for Localization later)

Originally, I was planning to make things, so that we can use this plugin outside IntelliJ, but now my goal is to at least make this plugin work in IntelliJ before targeting minor IDEs.

> Please note that some features are already available in ReasonML (ex: support of ODoc), so I will not code them, but "import" them.

## ✨ How will I do that?

I'm planning to use WSL on Windows. It's easy to install ocaml (and opam) on a WSL, as one only has to do what one would have done on Linux. I'm planning to use ocaml rather than ocaml, because this would allow more features in the features, and opam is great if we want to have multiple versions of ocaml.

On Linux, I will directly use ocaml and/or opam, for the reasons mentioned above.

I'm also planning to allow someone to compile on a remote host. This is an experimental feature, hence I'm not sure how it will go, but I will try.

## 🚀 Features

| Setup                                                    |
|----------------------------------------------------------|
| ✅ You got some instructions to install what you need     |
| ✅ You can create a non-opam SDK                          |
| ✅ You can create/add/use an opam SDK                     |
| ✅ Set/Update the project SDK, you can use it in a module |
| ✅ Set/Update the SDK for a module                        |
| ✅ Set the output folder for the project                  |
| ✅ Set the output folder for the module                   |
| ✅ The project is created from a template with src...     |
| ❌ The project is created with a default runConfiguration |

You are now able to create a project using an opam SDK, or create an opam-like SDK. Opam like SDKs should be used if you are not using opam (ex: you got /bin/ocaml, etc.). You may use a template, the only one available is an application using a <b>Makefile</b>.

<details>
<summary>Windows (with WSL)</summary>

On Windows, you may use a WSL (ex: Windows Store > Debian), then follows the instruction for Linux users
</details>

<details>
<summary>Windows (with cygwin)</summary>

Download [cygwin](https://cygwin.com/install.html). In the installer, you will have to pick some packages to install. Select "full" and pick **ocaml**, or **opam** (recommended)

* `opam`
* `make`
* `wget`
* `curl`
* `tar`
* `libclang`
* `mingw[...]clang` (pick the one according to your OS)

To install new versions of OCaml, run `Cygwin.bat` (you can't in a PowerShell), then call `òpam switch create 4.12.0`.

Ensure that `C:/cygwin64/bin` is in the path. If you open a PowerShell, and write `ocaml` and the command is working, then you are good.
</details>

<details>
<summary>Linux</summary>

You may not use `apt-get`, please use your distribution package manager if this is not working.

* **ocaml**: `sudo apt-get install ocaml`
* **opam** (recommended): `sudo apt-get install opam` then, you may use `òpam switch create 4.12.0` to install `ocaml 4.12.0`
</details>

| SDK                                                 |
|-----------------------------------------------------|
| ✅ We are suggesting SDKs (✅ Cygwin, ✅ WSL, ❌ .jdks) |
| ✅ We are suggesting a location for non-opam SDKs    |
| ✅ The SDK is verified                               |
| ✅ The user can add/remove sources                   |
| ❌ The user can download sources                     |
| ❌ The sources are indexed (autocompletion)          |
| ❌ You can download OCaml                            |
| ❌ You can add/remove libraries for a module         |

An SDK is a folder (**named after its ocaml version** such as 4.05.0) in which you got a folder "bin" with ocaml, ... and a folder "lib" with the sources. On opam, the folder `.opam-switch/sources/` will also be checked.

## 📄 License

This project is [MIT licensed](LICENSE).