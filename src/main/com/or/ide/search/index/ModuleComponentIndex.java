package com.or.ide.search.index;

import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.PsiModule;
import com.or.lang.core.stub.type.PsiModuleStubElementType;
import org.jetbrains.annotations.NotNull;

public class ModuleComponentIndex extends StringStubIndexExtension<PsiModule> {

    @Override
    public int getVersion() {
        return super.getVersion() + PsiModuleStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiModule> getKey() {
        return IndexKeys.MODULES_COMP;
    }


}
