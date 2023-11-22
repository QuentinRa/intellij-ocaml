package com.or.lang.core.stub;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.NamedStubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.io.StringRef;
import com.or.lang.core.psi.PsiLet;
import com.or.utils.Joiner;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class PsiLetStub extends NamedStubBase<PsiLet> {
    private final String[] myPath;
    private final List<String> myQnames;
    private final String myAlias;
    private final boolean myIsFunction;
    private final List<String> myDeconstructionNames;

    public PsiLetStub(StubElement parent, @NotNull IStubElementType elementType, String name, String[] path, String alias, boolean isFunction, @NotNull List<String> deconstructionNames) {
        super(parent, elementType, name);
        myPath = path;
        myAlias = alias;
        myIsFunction = isFunction;
        myDeconstructionNames = deconstructionNames;

        String joinedPath = Joiner.join(".", path);
        if (deconstructionNames.isEmpty()) {
            myQnames = List.of(joinedPath + "." + name);
        } else {
            myQnames = deconstructionNames.stream().map(dname -> joinedPath + "." + dname).collect(Collectors.toList());
        }
    }

    public PsiLetStub(StubElement parent, @NotNull IStubElementType elementType, StringRef name, String[] path, String alias, boolean isFunction, @NotNull List<String> deconstructionNames) {
        super(parent, elementType, name);
        myPath = path;
        myAlias = alias;
        myIsFunction = isFunction;
        myDeconstructionNames = deconstructionNames;

        String joinedPath = Joiner.join(".", path);
        if (deconstructionNames.isEmpty()) {
            myQnames = List.of(joinedPath + "." + name);
        } else {
            myQnames = deconstructionNames.stream().map(dname -> joinedPath + "." + dname).collect(Collectors.toList());
        }
    }

    public String[] getPath() {
        return myPath;
    }

    public @NotNull String getQualifiedName() {
        return myQnames.get(0);
    }

    public String getAlias() {
        return myAlias;
    }

    public boolean isFunction() {
        return myIsFunction;
    }

    public @NotNull List<String> getDeconstructionNames() {
        return myDeconstructionNames;
    }

    public @NotNull List<String> getQualifiedNames() {
        return myQnames;
    }

}
