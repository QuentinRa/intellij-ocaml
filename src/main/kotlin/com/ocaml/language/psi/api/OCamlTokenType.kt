package com.ocaml.language.psi.api

import com.intellij.psi.tree.IElementType
import com.ocaml.ide.files.OCamlLanguage

class OCamlTokenType(debugName: String) : IElementType(debugName, OCamlLanguage)