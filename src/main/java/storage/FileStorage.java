package storage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

public interface FileStorage {
    byte[] readBytes(Path path) throws IOException;

    void writeBytes(Path path, byte[] bytes) throws IOException;

    default String readString(Path path, Charset charset) throws IOException {
        return new String(readBytes(path), charset);
    }

    default void writeString(Path path, String content, Charset charset) throws IOException {
        writeBytes(path, content.getBytes(charset));
    }
}

