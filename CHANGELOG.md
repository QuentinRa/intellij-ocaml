# Changelog

All notable changes to this project will be documented in this file.
The plugin is experimental. You should implement any feature directly in the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) plugin.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Unreleased
### Added
- 🚀 parameters names are shown before parameters (#38)
- 🚀 can see the type with `CTRL+SHIFT+P` (#38)

## [0.1.0-EAP]
### Internal
- 🚀🚀 Remade of the test infrastructure to support workflows, and fix bugs

### Changed
- ✨ Fixed sending statement with "and" to the REPL. (#80)

## [0.0.8]
### Added
- 🚀🚀 Available for 203.*
- ✨ Custom error messages if the SDK is invalid. (#42)

### Changed
- ✨ Use "dune --version" to set value in dune-project. (#43)
- ✨ No autocompletion in strings. No auto-completion for numbers, and floats. (#66)
- 🚀 Fix errors when using modules. (#65)
- ✨ Fix problems with the highlighter (#45)

## [0.0.7]
### Added
- ✨ Can show/hide the variable view, can be docked next to the file (#40)
- ✨ Preview auto-selected statement (CTRL-ENTER) if needed (#56)

### Changed
- ✨ Fix issues with the REPL console (#37, #57, #60)

## [0.0.6] - 2022-02-05
### Internal
- Keeping a Changelog

### Added
- 🚀🚀 Almost every feature in ReasonML for OCaml was imported
- 🚀 Can send the selected code to the REPL (#41)
- 🚀 We can send the selection to the console.
- ✨ Buttons to browse the OCaml Manual/API in the editor, and a tab in the SDK window, to change these links. (#49)

### Changed
- 🚀 Improving editor (#41, #47): shown for .mli, not shown for excluded files.
- ✨ Proper highlight for odoc documentation (#49).
- ✨ Proper highlight for warnings, errors, and alerts (#38).

### Removed
- We can set the sources, annotations, and documentation paths, but they were not used anyway. They may be added back later if needed.

## [0.0.5] - 2022-02-01
### Added
- 🚀 Can create .ml, .mli, or .ml+.mli files #34
- 🚀 Highlight #34
- ✨ Suggest setting up SDK if opening a file while not having set the SDK
- 🚀 REPL with variable view #32
- 🚀 Dune Language Support (from ReasonML) #33
- 🚀 Show warnings, alerts, and errors in the editor
- 🚀 Tested on Linux (finally) #14

### Changed
- 🪲 Fixing NPE/errors when saving module settings

## [0.0.4] - 2022-01-22
### Internal
- 🚀 The code is not dependent on SDK Providers (ex: WSL, Cygwin, OCaml64, etc.) anymore. We can add/remove them however we want.

### Added
- 🪲 Fixing #9

### Changed
- ✨ Icon for library roots, filter non-ocaml files #27
- 🚀 Trying to follow IntelliJ Platform Guidelines

## [0.0.3] - 2022-01-19
### Added
- 🚀 Sources are detected. You can browse them in the editor. You can add/remove them, in the project structure. #18 #19
- 🚀 Can use a template (**None**, **Makefile**, or **Dune**) #7
- 🪲 Fixing #16
- 🪲 Fixing #25
- 🪲 Fixing #26
- 🚀 Improving modules' editor (adding content entry)

### Changed
- ✨ The field for the detected sources' folder in the wizard is now a label (97ab383)

## [0.0.2] - 2022-01-17
### Internal
- Targeting both 211, 212, 213, with the code on **one** branch
- Writing README, create tasks on GitHub, planning of the development
- Testing the code
- Bundle

### Added
- 🚀 Adding a project wizard. One can create an opam-like SDK (for those that do not have opam), or create an opam SDK. 
- ✨ For non-opam SDK, fields aside from the `ocaml binary` are detected by the plugin.
- 🚀 Use "Project SDK" as the module SDK, or you can select a custom SDK.
- 🚀 Set the compilation output directory

### Removed
- 📘 Everything available in 0.0.1, will be added back later

## [0.0.1] - 2021-03-10
### Added
- 📘 Create an SDK -> Create a library
- 📘 Building application (CTRL-F9), see errors in the "build" window
- 📘 REPL
- 📘 Run Configuration (can execute a program)