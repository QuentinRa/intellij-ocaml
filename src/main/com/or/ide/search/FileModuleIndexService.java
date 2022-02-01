package com.or.ide.search;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.indexing.FileBasedIndex;
import com.or.ide.search.index.NamespaceIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FileModuleIndexService {
    private final @NotNull NamespaceIndex m_nsIndex;

    public FileModuleIndexService() {
        m_nsIndex = NamespaceIndex.getInstance();
    }

    public static FileModuleIndexService getService() {
        return ServiceManager.getService(FileModuleIndexService.class);
    }

    @NotNull
    public Collection<String> getNamespaces(@NotNull Project project) {
        return FileBasedIndex.getInstance().getAllKeys(m_nsIndex.getName(), project);
    }
}
