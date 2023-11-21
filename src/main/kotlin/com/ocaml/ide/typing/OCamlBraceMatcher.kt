package com.ocaml.ide.typing

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.ocaml.language.psi.OCamlTypes

class OCamlBraceMatcher : PairedBraceMatcher {
    override fun getPairs() = Constants.PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, next: IElementType?): Boolean =
        true
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int =
        openingBraceOffset

    internal object Constants {
        val PAIRS: Array<BracePair> = arrayOf(
            BracePair(OCamlTypes.LBRACE, OCamlTypes.RBRACE, false),
            BracePair(OCamlTypes.BEGIN, OCamlTypes.END, false),
            BracePair(OCamlTypes.STRUCT, OCamlTypes.END, false),
            BracePair(OCamlTypes.SIG, OCamlTypes.END, false),
            BracePair(OCamlTypes.OBJECT, OCamlTypes.END, false),
            BracePair(OCamlTypes.LPAREN, OCamlTypes.RPAREN, false),
            BracePair(OCamlTypes.LBRACKET, OCamlTypes.RBRACKET, false),
            BracePair(OCamlTypes.LARRAY, OCamlTypes.RARRAY, false),
            BracePair(OCamlTypes.LT, OCamlTypes.GT, false),
            BracePair(OCamlTypes.DO, OCamlTypes.DONE, false)
        )
    }
}