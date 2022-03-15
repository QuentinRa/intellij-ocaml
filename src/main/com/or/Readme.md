# ReasonML

## Changes
### Added

This is the list features that I added directly into the code of the ReasonML plugin.

* ✅ Adding highlight for `odoc`
* ❌ Improving documentation (https://github.com/QuentinRa/intellij-ocaml/issues/68)
* ❌ Invalid resolved documentation (https://github.com/QuentinRa/intellij-ocaml/issues/69)
* ❌ Priorities for autocompletion (https://github.com/QuentinRa/intellij-ocaml/issues/55)
* ✅ Unpleasant suggestions (https://github.com/QuentinRa/intellij-ocaml/issues/66) 
* ❌ Duplicate suggestions (https://github.com/QuentinRa/intellij-ocaml/issues/67)

### Bugs

This is a list of bugs in this version of the ReasonML plugin.

* ✅ (not sure if it's a bug in ReasonML): copyToTempFile (psiFile.getText()) should be called in a RunAction
* ✅ invalid highlight (https://github.com/QuentinRa/intellij-ocaml/issues/45)
  * Removing ERASE_MARKER in annotator
  * Every other case was from my side of the Lexer
* ❌ bug with the resolver (https://github.com/QuentinRa/intellij-ocaml/issues/62)
* ❌ highlight for todo (https://github.com/QuentinRa/intellij-ocaml/issues/64)
* ❌ empty type in variable view (https://github.com/QuentinRa/intellij-ocaml/issues/63)

## To be merged

This is the list a changes that are planned to be merged with the ReasonML plugin.

* ❌ bug with quotes inside a comment

## Merged

This is a list of changes that were merged with the ReasonML plugin.

* ✅ Live templates (CTRL+J)
* ✅ Spellchecker