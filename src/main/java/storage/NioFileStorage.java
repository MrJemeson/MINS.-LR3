package storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NioFileStorage implements FileStorage {
    @Override
    public byte[] readBytes(Path path) throws IOException {
        if (!Files.exists(path)) {
            return new byte[0];
        }
        return Files.readAllBytes(path);
    }

    @Override
    public void writeBytes(Path path, byte[] bytes) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(path, bytes);
    }
}

