package com.dune.language.psi.api

import com.intellij.psi.impl.source.tree.CompositePsiElement
import com.intellij.psi.tree.IElementType

open class DuneElementImpl (type: IElementType) : CompositePsiElement(type)