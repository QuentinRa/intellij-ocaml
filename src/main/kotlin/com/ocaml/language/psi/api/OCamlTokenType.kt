package com.ocaml.language.psi.api

import com.intellij.psi.tree.IElementType
import com.ocaml.ide.files.OCamlLanguage

import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.NotNull

class OCamlTokenType(debugName: String) : IElementType(debugName, OCamlLanguage)