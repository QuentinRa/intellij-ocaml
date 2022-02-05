package com.or.ide.search.index;

import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import com.ocaml.ide.files.FileHelper;
import com.ocaml.ide.files.OCamlFileType;
import com.ocaml.ide.files.OCamlInterfaceFileType;
import com.or.ide.files.FileBase;
import com.or.ide.search.FileModuleData;
import com.or.utils.Log;
import com.or.utils.Platform;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileModuleIndex extends FileBasedIndexExtension<String, FileModuleData> {

    private static final ID<String, FileModuleData> NAME = ID.create("reason.module.fileIndex");
    private static final int VERSION = 1;
    private static final Log LOG = Log.create("index.file");

    private static final DataExternalizer<FileModuleData> EXTERNALIZER = new FileModuleDataExternalizer();

    @Override
    public @NotNull ID<String, FileModuleData> getName() {
        return NAME;
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return EnumeratorStringDescriptor.INSTANCE;
    }

    @NotNull
    @Override
    public DataIndexer<String, FileModuleData, FileContent> getIndexer() {
        return inputData -> {
            Map<String, FileModuleData> map = new HashMap<>();
            String namespace = "";
            FileBase psiFile = (FileBase) inputData.getPsiFile();

            String moduleName = psiFile.getModuleName();

            FileModuleData value =
                    new FileModuleData(inputData.getFile(), namespace, moduleName, FileHelper.isOCaml(inputData.getFileType()), psiFile.isInterface(), psiFile.isComponent());
            if (LOG.isDebugEnabled()) {
                LOG.debug("indexing " + Platform.getRelativePathToModule(inputData.getPsiFile()) + ": " + value);
            }

            map.put(moduleName, value);

            return map;
        };
    }

    @NotNull
    @Override
    public DataExternalizer<FileModuleData> getValueExternalizer() {
        return EXTERNALIZER;
    }

    @Override
    public int getVersion() {
        return VERSION;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return new DefaultFileTypeSpecificInputFilter(
                OCamlFileType.INSTANCE, OCamlInterfaceFileType.INSTANCE);
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    static final class FileModuleDataExternalizer implements DataExternalizer<FileModuleData> {
        @Override
        public void save(@NotNull DataOutput out, @NotNull FileModuleData value) throws IOException {
            out.writeBoolean(value.isOCaml());
            out.writeBoolean(value.isInterface());
            out.writeBoolean(value.isComponent());
            out.writeUTF(value.getPath());
            out.writeUTF(value.getNamespace());
            out.writeUTF(value.getModuleName());
            out.writeUTF(value.getFullname());
        }

        @NotNull
        @Override
        public FileModuleData read(@NotNull DataInput in) throws IOException {
            boolean isOCaml = in.readBoolean();
            boolean isInterface = in.readBoolean();
            boolean isComponent = in.readBoolean();
            String path = in.readUTF();
            String namespace = in.readUTF();
            String moduleName = in.readUTF();
            String fullname = in.readUTF();
            return new FileModuleData(path, fullname, namespace, moduleName, isOCaml, isInterface, isComponent);
        }
    }
}
