package com.ocaml.ide.spelling

import com.intellij.spellchecker.inspections.IdentifierSplitter
import com.intellij.spellchecker.tokenizer.TokenConsumer
import com.intellij.spellchecker.tokenizer.Tokenizer
import com.ocaml.language.psi.api.OCamlNameIdentifierOwner

object OCamlNameIdentifierOwnerTokenizer : Tokenizer<OCamlNameIdentifierOwner>() {
    override fun tokenize(element: OCamlNameIdentifierOwner, consumer: TokenConsumer) {
        val identifier = element.nameIdentifier ?: return
        consumer.consumeToken(identifier, IdentifierSplitter.getInstance())
    }
}