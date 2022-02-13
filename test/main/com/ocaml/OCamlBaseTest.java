package com.ocaml;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class OCamlBaseTest extends BasePlatformTestCase {

    protected @Nullable String loadFile(@NotNull String fileName) {
        try {
            return FileUtil.loadFile(new File(getTestDataPath(), fileName), CharsetToolkit.UTF8, true);
        } catch (IOException e) {
            return null;
        }
    }

    protected String getCustomTestDataPath() {
        return "";
    }

    @Override protected final @NotNull String getTestDataPath() {
        return "test/testData/"+getCustomTestDataPath();
    }

}
