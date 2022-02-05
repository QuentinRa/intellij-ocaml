package com.or.ide.search.index;

import com.intellij.json.JsonFileType;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class NamespaceIndex extends ScalarIndexExtension<String> {

    private static final int VERSION = 6;

    private static final ID<String, Void> NAME = ID.create("reason.index.bsconfig");
    private static final NamespaceIndex INSTANCE = new NamespaceIndex();

    @NotNull
    public static NamespaceIndex getInstance() {
        return INSTANCE;
    }

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return NAME;
    }

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return inputData -> Collections.emptyMap();
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return file -> (file.getFileType() instanceof JsonFileType
                && "bsconfig.json".equals(file.getName()));
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }
}
