# OCaml plugin IntelliJ

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

This plugin is based on [ReasonML](https://github.com/giraud/reasonml-idea-plugin), but **I'm tuning it for my personal use**. **I'm not planning to maintain this repository**, so you should work on the original plugin.

> **Created on**: Windows 10/11 for IntelliJ<br>
> **Goal**: easy to install, complete, compatible in minor IDE, OCaml only. Use WSL+opam on Windows.

<hr>

## Checklist

### Create a project/module

* [x] Can select opam switch
  * [ ] Check the selected switch
  * [ ] Give some instructions
   * [ ] May add a "Download" Opam/Switch
* [x] Some switches are detected
  * [x] tested on Windows with 1 WSL installed
  * [x] tested on Windows with more than 1 WSL installed
  * [ ] tested on Windows with no WSL installed
  * [ ] tested on Linux
* [x] Set up the library
* [x] Set up the values in the settings
  * [ ] Do not allow the user to set a different JDK if the user is creating another module, or update the settings to handle multiples switches
* [x] Set up a default folder "src"
* [ ] Set up a default file
  * [ ] Loaded from a template?
* [ ] Set up a default run configuration for the default file

### Run/Build project

* [x] **Can create a run configuration**
  * [ ] Should detect dependencies or allow the user to select multiples targets
  * [ ] The menu to select a file, should be like in Java
  * [ ] Give options to the compiler?
  * [ ] Give arguments to the program?
  * [ ] Select the output folder (we may use the option -i)
* [x] **Can build .ml file**
  * [x] Save files before build 👀
  * [x] Cancel build
  * [ ] Restart build
  * [x] Stop build
  * [x] The build show warnings/errors
    * [ ] The parser was properly tested
    * [ ] Add some colors?
    * [ ] Add some context?
    * [ ] Add some quick fix?
    * [ ] Sort the results by name then line
  * [ ] Do not force every file to be valid
* [x] **Can execute .ml file**
  * [ ] Should build then run
  * [ ] The program should "run", and not call "build" manually
  * [ ] Show errors/... in colors

### Settings

* [x] Change opam location
* [x] Change switch -> change the library for every module
  * [ ] Can download a new switch from the IDE
* [x] See opam installed libraries (name, desc, version)
  * [ ] Fix bug (error, when loading libs for the first time)
  * [ ] Can update library
  * [ ] Can install library
  * [ ] Can uninstall library
* [ ] Check the values in the settings

### REPL

* [x] **Can run a console with ocaml**
  * [x] With History (arrows up/down, history button)
  * [x] Wrap text
  * [x] Restart
  * [x] Run a command
* [ ] Send some code, from the file, to the console
  * [ ] Add local/headless run ? (like in JUnit tests, or in R?).
  * [ ] Add some shortcut
* [ ] Show variables / env