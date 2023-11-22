package com.or.ide.search;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.psi.tree.TokenSet;
import com.ocaml.lang.lexer.OCamlLexerAdapter;
import com.or.lang.OCamlTypes;
import org.jetbrains.annotations.Nullable;

public class OclFindUsagesProvider extends ORFindUsagesProvider {
    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
                new OCamlLexerAdapter(), //
                TokenSet.create(OCamlTypes.UIDENT, OCamlTypes.LIDENT, OCamlTypes.VARIANT_NAME), //
                TokenSet.EMPTY, //
                TokenSet.create(OCamlTypes.FLOAT_VALUE, OCamlTypes.INT_VALUE, OCamlTypes.STRING_VALUE));
    }
}
