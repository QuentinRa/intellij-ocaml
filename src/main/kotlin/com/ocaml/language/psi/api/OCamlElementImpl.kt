package com.ocaml.language.psi.api

import com.intellij.psi.impl.source.tree.CompositePsiElement
import com.intellij.psi.tree.IElementType

// Base class for elements implementation
abstract class OCamlElementImpl(type: IElementType) : CompositePsiElement(type)