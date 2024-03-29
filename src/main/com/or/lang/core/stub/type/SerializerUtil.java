package com.or.lang.core.stub.type;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class SerializerUtil {
    private SerializerUtil() {
    }

    static void writePath(@NotNull StubOutputStream dataStream, String [] path) throws IOException {
        if (path == null) {
            dataStream.writeByte(0);
        } else {
            dataStream.writeByte(path.length);
            for (String name : path) {
                dataStream.writeUTFFast(name == null ? "" : name);
            }
        }
    }

    static String [] readPath(@NotNull StubInputStream dataStream) throws IOException {
        String[] path = null;
        byte namesCount = dataStream.readByte();
        if (namesCount > 0) {
            path = new String[namesCount];
            for (int i = 0; i < namesCount; i++) {
                path[i] = dataStream.readUTFFast();
            }
        }
        return path;
    }
}
