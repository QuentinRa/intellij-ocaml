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
* [x] **Using a bundle** (possible support for Localization later)

Originally, I was planning to make things, so that we can use this plugin outside IntelliJ, but now my goal is to at least make this plugin work in IntelliJ before targeting minor IDEs.

> Please note that some features are already available in ReasonML (ex: support of odoc, dune support, etc.), and I will "import" them.

## ✨ How will I do that?

I'm planning to use WSL on Windows. It's easy to install ocaml (and opam) on a WSL, as one only has to do what one would have done on Linux. I'm planning to use ocaml rather than ocaml, because this would allow more features in the features, and opam is great if we want to have multiple versions of ocaml.

On Linux, I will directly use ocaml and/or opam, for the reasons mentioned above.

I'm also planning to allow someone to compile on a remote host. This is an experimental feature, hence I'm not sure how it will go, but I will try.

## 🚀 Features

| Setup                                                      |
|------------------------------------------------------------|
| ✅ You got some instructions to install what you need       |
| ✅ You can create a non-opam SDK                            |
| ✅ You can create/add/use an opam SDK                       |
| ✅ Set/Update the project SDK, you can use it in a module   |
| ✅ Set/Update the SDK for a module                          |
| ✅ Edit modules' properties (ex: output folder, src folder) |
| ✅ You can use a template (✅ Makefile, ✅ Dune)              |
| ❌ Add an import wizard                                     |
| ❌ The project is created with a default runConfiguration   |

You are now able to create a project using an opam SDK, or create an opam-like SDK. Opam like SDKs should be used if you are not using opam (ex: you only have /bin/ocaml, etc.). You may use a template, two are available, one using a <b>Makefile</b>, and the other using <b>Dune</b>.

| SDK                                                     |
|---------------------------------------------------------|
| ✅ We are suggesting SDKs                                |
| ✅ We are suggesting a location for non-opam SDKs        |
| ✅ The SDK is verified                                   |
| ✅ The user can add/remove sources                       |
| ✅ Suggest SDK if opening a file in a module without SDK |
| ❌ The user can download OCaml                           |

An SDK is a folder (**named after its ocaml version** such as 4.05.0) in which you got a folder `bin` with `ocaml`, ... and a folder `lib` with the sources. The sources (from the GitHub repository), are stored in `.opam-switch/sources/` (optional).

| REPL Console                        |
|-------------------------------------|
| ✅ Can execute commands              |
| ✅ Browse history, use arrow up/down |
| ❌ See the values of the variables   |
| ❌ Send commands to the console      |
| ❌ Send a file to the console        |

This is a console allowing the use of the ocaml interpreter in an easier way. For instance, arrow up/down are now properly handled. You also got a history of your commands.

| Code highlighting                                |
|--------------------------------------------------|
| ✅ .ml / .mli are highlighted (based on ReasonML) |
| ❌ we can see warnings in the file                |
| ❌ we can see errors in the file                  |
| ❌ we can see alerts in the file                  |

We are compiling the file, and parsing the output, to provide hints in the editor.

## 📖 Install ocaml and opam

<details>
<summary>Windows (with WSL, version >= 2021.3)</summary>

On Windows, you may use a WSL (ex: Windows Store > Debian), then follows the instruction for Linux users
</details>

<details>
<summary>Windows (with cygwin)</summary>

Download [cygwin](https://cygwin.com/install.html). In the installer, you will have to pick some packages to install. Select "full" and pick **ocaml** (if you don't want opam), otherwise pick

* `opam`
* `make`
* `wget` and `curl`
* `tar` and `unzip`
* `libclang` and `mingw[...]clang` (pick the one according to your OS)

To install new versions of OCaml, run `Cygwin.bat` (in cygwin64 folder), then call `òpam switch create 4.12.0`.

Ensure that `C:/cygwin64/bin` (for me) is in the path. If you open a PowerShell, and write `opam --version`, you should be good. This is pretty useless (as the command above does not work in a PowerShell), but you are now able to call commands such as `make` in a PowerShell, so you can use a `Makefile`!
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