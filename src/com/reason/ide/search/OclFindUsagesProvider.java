package com.reason.ide.search;

import com.intellij.lang.cacheBuilder.*;
import com.intellij.psi.tree.*;
import com.reason.lang.core.type.*;
import com.reason.lang.ocaml.*;
import org.jetbrains.annotations.*;

public class OclFindUsagesProvider extends ORFindUsagesProvider {
  @Nullable
  @Override
  public WordsScanner getWordsScanner() {
    ORTypes types = OclTypes.INSTANCE;
    return new DefaultWordsScanner(
        new OclLexer(), //
        TokenSet.create(types.UIDENT, types.LIDENT, types.VARIANT_NAME), //
        TokenSet.EMPTY, //
        TokenSet.create(types.FLOAT_VALUE, types.INT_VALUE, types.STRING_VALUE));
  }
}
