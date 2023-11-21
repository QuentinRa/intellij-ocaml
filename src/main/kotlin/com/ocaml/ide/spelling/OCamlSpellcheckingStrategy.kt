package com.ocaml.ide.spelling

import com.intellij.psi.PsiElement
import com.intellij.spellchecker.tokenizer.SpellcheckingStrategy
import com.intellij.spellchecker.tokenizer.Tokenizer
import com.ocaml.ide.files.OCamlLanguage
import com.ocaml.language.psi.OCamlTypes

class OCamlSpellcheckingStrategy : SpellcheckingStrategy() {
    override fun isMyContext(element: PsiElement) = OCamlLanguage.isKindOf(element.language)

    override fun getTokenizer(element: PsiElement?): Tokenizer<*> = when {
        element?.node?.elementType == OCamlTypes.STRING_LITERAL -> TEXT_TOKENIZER
        else -> super.getTokenizer(element)
    }
}