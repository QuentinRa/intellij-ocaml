# intellij-ocaml

[![Checks](https://badgen.net/github/checks/QuentinRa/intellij-ocaml/)](https://github.com/QuentinRa/intellij-ocaml/actions)
[![Version](https://img.shields.io/jetbrains/plugin/v/18531-ocaml.svg)](https://plugins.jetbrains.com/plugin/18531-ocaml)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18531-ocaml.svg)](https://plugins.jetbrains.com/plugin/18531-ocaml)
[![License: MIT](https://badgen.net/github/license/quentinRa/intellij-ocaml?color=yellow)](LICENSE)
![Dependabot: Active](https://badgen.net/github/dependabot/QuentinRa/intellij-ocaml/)
[![0.2.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/1)](https://github.com/QuentinRa/intellij-ocaml/milestone/1)
[![0.3.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/2)](https://github.com/QuentinRa/intellij-ocaml/milestone/2)
[![0.4.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/3)](https://github.com/QuentinRa/intellij-ocaml/milestone/3)
[![0.5.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/4)](https://github.com/QuentinRa/intellij-ocaml/milestone/4)
[![0.6.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/5)](https://github.com/QuentinRa/intellij-ocaml/milestone/8)
[![0.7.0](https://badgen.net/github/milestones/quentinra/intellij-ocaml/6)](https://github.com/QuentinRa/intellij-ocaml/milestone/6)


At least for now, the repository is **unmaintained**. [![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

This plugin is providing support for OCaml for JetBrains IDEs. It's based on both ReasonML and intellij-rust. If you are looking for a complete and maintained solution, please check out the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) plugin (that is based on [ocaml-ide](https://github.com/sidharthkuruvila/ocaml-ide) btw). I'm a student, and I'm lacking the knowledge to do something great.

[Getting Started 🗺️](https://plugins.jetbrains.com/plugin/18531-ocaml/documentation/getting-started) | [Features 🚀](https://plugins.jetbrains.com/plugin/18531-ocaml/documentation/features)

## Current Progress Of The Rework

**Goal**: implement the least feature to have a working plugin using the new parser. This is PoC to test if we can use JetBrains Grammar File.

Internal

* [x] Parser implemented
  * [x] Add some pins
    * [ ] Add more pins
    * [ ] Verify pins
  * [ ] (a,b) should be two variables
  * [ ] Parser Errors are not handled
  * [ ] Parser recovery is not handled
  * [ ] Parser tests were not implemented
  * [ ] Handle visibility (Has MLI? Has VAL? etc. ==> related: icons and structure view)
* [ ] Stubs
  * [x] Add Stubs For Variables
  * [ ] Use stubs in structure view
* [ ] Indexes
  * [x] Add indexes for variables
  * [ ] Indexes include nested variables (Scopes?)
  * [ ] Use an index of integers?
* [ ] GitHub CI

Generalize

* [ ] Add stubs for others
* [ ] Add structure view for others
* [ ] Add indexes for others
* [ ] Add colors (settings+annotator) for others

User features

* [x] Highlighter
  * [x] Add Lexer Highlighter
  * [x] Add Annotator Highlighter
* [x] Spell-checker
  * [x] Comments
  * [x] Strings
  * [x] Identifiers
* [ ] Typing
  * [x] Braces matching (hover the other)
  * [ ] Quotes handler
  * [ ] Paste processor
  * [ ] Smart enter processor
  * [x] Single Line Comments Handler (CTRL+/)
  * [x] Multi Lines Comments Handler (CTRL+SHIFT+/)
  * [ ] Other handlers
* [ ] Smart Features
  * [ ] Annotations
  * [X] Structure View (ALT+7)
    * [x] Variables
    * [x] Handle Visibility (public/private)
    * [ ] Filters (nested variables?)
  * [ ] Surround With
  * [x] Live Templates
    * [x] Basic implementation
    * [ ] Add scopes (no for in MLI?)
    * [x] Not available in comments
    * [x] Not available in other files
  * [X] Line Markers
    * [X] Declaration
    * [x] Implementation
    * [x] Show a nice presentation
* [x] Settings
  * [X] Color Settings Page For Highlighter
  * [x] Add Highlighter Annotator to Color Settings Page

Roadmap

* Fix LineMarker
  * [ ] Do not use hard-coded path to element
* Handle Parser Directives
* Check references
* Handle Include and LineMarker
* Check completion
* Check folding
* Qualified path
  * [ ] Include Modules/Classes/...
  * [ ] Rebuild Line Marker and Indexes
* Add annotator tests (local, global, functions)
* Add line marker tests (see file comment)
* Add reference/include tests
* ~~Test OCaml-LSP~~ (no documentation for plugin developers)
* Modules (files) indexes
* Find usages
* Go to
* Can create files
* Add SDK
* Handle SDK
* Handle Module Creation
* Handle Run Configurations
* Add parser tests
* Add OCamlLanguageUtils;LetBindingMixin Tests
* Can compile using dune (.gitignore? etc.)
* Can display warnings/errors
* Can generate annot file
* Can use annot for type inference
* Space allowed (spaceExistenceTypeBetweenTokens)
* Better stubs (skipChildProcessingWhenBuildingStubs)

Random

* Let#isFunction: use type inference?


Generalization List

* [x] Let
* [x] Val
* [ ] Exception
* [ ] Type
* [ ] Module
* [ ] Module Type

## 🎯 What's my goal?

* [ ] Can run/compile ocaml files (without having to install some external build system)
* [ ] Support for opam (can install / update packages, ...)
* [ ] Support for Makefile
* [ ] Support for Dune
* [x] Any installation must be simple, and effortless
* [ ] **Real-time checks for errors/warnings**, including quick fixes. Can suppress warnings.
* [x] **Real-time type inference**
* [ ] **Smart auto-completion**
* [x] **REPL support**: we must be able to send commands from our file, to a REPL console
* [x] **Using a bundle** (possible support for Localization later)
* [x] Working in minor IDEs (CLion, PHPStorm, etc.)

## 📄 License

This project is [MIT licensed](LICENSE).