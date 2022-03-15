# intellij-ocaml

[![Checks](https://badgen.net/github/checks/QuentinRa/intellij-ocaml/)](https://github.com/QuentinRa/intellij-ocaml/actions)
[![Version](https://img.shields.io/jetbrains/plugin/v/18531-ocaml.svg)](https://plugins.jetbrains.com/plugin/18531-ocaml)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18531-ocaml.svg)](https://plugins.jetbrains.com/plugin/18531-ocaml)
[![License: MIT](https://badgen.net/github/license/quentinRa/intellij-ocaml?color=yellow)](LICENSE)
![Dependabot: Active](https://badgen.net/github/dependabot/QuentinRa/intellij-ocaml/)
[![0.2.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/1)](https://github.com/QuentinRa/intellij-ocaml/milestone/1)
[![0.3.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/2)](https://github.com/QuentinRa/intellij-ocaml/milestone/2)
[![0.4.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/3)](https://github.com/QuentinRa/intellij-ocaml/milestone/3)

<!--
At least for now, the repository is maintained.
[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)
-->

This plugin is providing support for OCaml for JetBrains IDEs. It's based on both ReasonML and intellij-rust. If you are looking for a complete and maintained solution, please check out the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) plugin. I'm a student, and I'm lacking the knowledge to do something great.

## 🎯 What's my goal?

* [ ] Can run/compile ocaml files (without having to install some external build system)
* [ ] Support for opam (can install / update packages, ...)
* [ ] Support for Makefile
* [ ] Support for Dune (already supported in ReasonML)
* [x] Any installation must be simple, and effortless
* [ ] **Real-time checks for errors/warnings**, including quick fixes
* [x] **Real-time type inference**
* [ ] **Smart auto-completion**
* [x] **REPL support**: we must be able to send commands from our file, to a REPL console
* [x] **Using a bundle** (possible support for Localization later)
* [x] Working in minor IDEs (CLion, PHPStorm, etc.)

> Please note that some features are already available in ReasonML (ex: odoc support, dune support, etc.), and I will reuse (and hopefully improve) them. [List of features from the ReasonML plugin 🤩️](ReasonML.md)

## ✨ Providers for OCaml

On Windows, I will support the use of Cygwin, OCaml64, or a WSL. Both ocaml installed with, or without opam should work. For the latter, some features won't be available, as some files will be missing.

On Linux, you can also use ocaml installed with, or without opam.

Finally, I'm planning to allow someone to compile on a remote host. This is an experimental feature (from the classes I inspected), so I'm not sure how it will go, but I will give it a try.

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

| REPL Console                                          |
|-------------------------------------------------------|
| ✅ Can execute commands                                |
| ✅ Browse history, use arrow up/down                   |
| ✅ See the values of the variables                     |
| ✅ Send a file to the console                          |
| ✅ Send the selection to the console                   |
| ✅ Send a statement to the console                     |
| ✅ Preview the auto-selected statement, can be changed |
| ❌ Multiples consoles                                  |
| ❌ Select the SDK used in a console                    |
| ❌ Use `utop`                                          |

This is a console allowing the use of the ocaml interpreter more easily. For instance, arrows up/down are now properly handled. You also got a history of your commands.

| Code highlighting                                |
|--------------------------------------------------|
| ✅ .ml / .mli are highlighted (based on ReasonML) |
| ✅ we can see warnings in the file                |
| ✅ we can see errors in the file                  |
| ✅ we can see alerts in the file                  |
| ✅ errors are shown project-wide                  |
| ✅ mli is compiled is present, when in a .ml      |
| ❌ handle dune projects                           |
| ❌ handle Makefile projects                       |

We are compiling the file, and parsing the output, to provide hints in the editor.

| New File                                               |
|--------------------------------------------------------|
| ✅ Can create .ml, .mli, or .ml and .mli                |
| ✅ Editor > File and Code Templates for .ml and .mli    |
| ✅ Buttons to browse the OCaml Manual/API in the editor |

| OCaml                            |
|----------------------------------|
| ❌ Show names before parameters   |
| ✅ Show types with `CTRL+SHIFT+P` |

| Editor for .annot                             |
|-----------------------------------------------|
| ✅ See the source next to the content          |
| ✅ Can click in the editor ➡️ show in the tree |
| ✅ Can click in the tree ➡️ show in the source |

| Dune                                              |
|---------------------------------------------------|
| ✅ Import every feature in ReasonML                |
| ❌ Suggest installing dune                         |
| ❌ Show a message when updating files not targeted |
| ❌ Show an icon to "reload" dune                   |

| Minor IDEs                                    |
|-----------------------------------------------|
| ❌ Can create a project with an SDK (+check)   |
| ❌ Can create a project from a template        |
| ✅ Can use the REPL                            |
| ✅ Highlight, .... (basic features)            |
| ❌ Can change the SDK                          |
| ❌ Warnings/Errors/... shown without compiling |

## 📖 Install ocaml and opam

<details>
<summary>Windows (with WSL, >= 2020.3 only)</summary>

On Windows, you may use a WSL (ex: Windows Store > Debian), then follows the instruction for Linux users
</details>

## 📄 License

This project is [MIT licensed](LICENSE).