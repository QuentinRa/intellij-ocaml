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
| ❌ You can create a non-opam SDK                          |
| ✅ You can create/add/use an opam SDK                     |
| ❌ Set the project SDK, you can use it in a module        |
| ❌ Set the SDK for a module                               |
| ✅ Set the output folder for the project                  |
| ✅ Set the output folder for the module                   |
| ❌ The project is created from a template with src...     |
| ❌ The project is created with a default runConfiguration |
| 😬 You can add libraries for a module (wizard+editor)    |

Once basics are done, you will be able to create an OCaml Project with a default project. You may later use "Project Structure" to set things like the output folder if needed, either for the project or each module.

| SDK                                                              |
|------------------------------------------------------------------|
| ❌ The SDK is verified                                            |
| ❌ The sources are indexed, can add more sources (autocompletion) |
| 😬 You can download OCaml                                        |

An SDK is a folder in which you got a folder "bin" with ocaml, ... and a folder libs with the sources. You got such a folder using opam (ex: `sudo apt-get install opam && opam switch create 4.12.0`). In the project wizard, you have a form to create an SDK for non-opam users.

## 📄 License

This project is [MIT licensed](LICENSE).