package com.or.ide.search.index;

import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StringStubIndexExtension;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.or.lang.core.psi.impl.PsiFakeModule;
import com.or.lang.core.stub.type.PsiFakeModuleStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ModuleTopLevelIndex extends StringStubIndexExtension<PsiFakeModule> {
    public static void processModules(@NotNull Project project, @Nullable GlobalSearchScope scope, @NotNull IndexKeys.ProcessElement<PsiFakeModule> processor) {
        StubIndex.getInstance().processAllKeys(IndexKeys.MODULES_TOP_LEVEL, project,
                name -> {
                    Collection<PsiFakeModule> collection = getModules(name, project, scope);
                    for (PsiFakeModule module : collection) {
                        processor.process(module);
                    }
                    return true;
                });
    }

    public static @NotNull Collection<PsiFakeModule> getModules(@NotNull String key, @NotNull Project project, @Nullable GlobalSearchScope scope) {
        return StubIndex.getElements(IndexKeys.MODULES_TOP_LEVEL, key, project, scope, PsiFakeModule.class);
    }

    @Override
    public int getVersion() {
        return super.getVersion() + PsiFakeModuleStubElementType.VERSION;
    }

    @Override
    public @NotNull StubIndexKey<String, PsiFakeModule> getKey() {
        return IndexKeys.MODULES_TOP_LEVEL;
    }
}
