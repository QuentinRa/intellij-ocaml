package com.dune.language.psi.api

import com.dune.language.DuneLanguage
import com.intellij.psi.tree.IElementType

class DuneTokenType(debugName: String) : IElementType(debugName, DuneLanguage)