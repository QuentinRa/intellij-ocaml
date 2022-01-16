# intellij-ocaml

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

This plugin is providing support for OCaml in IntelliJ. It's based on ReasonML and intellij-rust. If you are looking for a complete and maintained solution, please check the [ReasonML](https://github.com/giraud/reasonml-idea-plugin) plugin.

## 🎯 What's my goal?

* [ ] We must be able to run/compile ocaml files without having to install Dune, Esy, or BuckleScript (if we have installed ocaml)
* [ ] Support for opam
* [ ] Any installation must be simple, and effortless
* [ ] **Real-time checks for errors/warnings**, including quick fixes
* [ ] **Real-time type inference**
* [ ] **Smart completion**
* [ ] **REPL support**: we must be able to send commands from our file, to a REPL console

Originally, I was planning to make things, so that we can use this plugin outside IntelliJ, but now my goal is to at least make this plugin work in IntelliJ before targeting minor IDEs.

> Please note that some features are already available in ReasonML (ex: support of ODoc), so I will not code them, but "import" them.

## ✨ How will I do that?

I'm planning to use WSL on Windows. It's easy to install ocaml (and opam) on a WSL, as one only has to do what one would have done on Linux. I'm planning to use ocaml rather than ocaml, because this would allow more features in the features, and opam is great if we want to have multiple versions of ocaml.

On Linux, I will directly use ocaml and/or opam, for the reasons mentioned above.

I'm also planning to allow someone to compile on a remote host. This is an experimental feature, hence I'm not sure how it will go, but I will try.

## 📄 License

This project is [MIT licensed](LICENSE).