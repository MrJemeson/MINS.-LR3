package unitofwork;

import storage.FileStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;


public class TransactionalFileStorage implements FileStorage {
    private final FileStorage delegate;
    private final Map<Path, byte[]> stagedWrites = new LinkedHashMap<>();

    public TransactionalFileStorage(FileStorage delegate) {
        this.delegate = delegate;
    }

    @Override
    public byte[] readBytes(Path path) throws IOException {
        byte[] staged = stagedWrites.get(path);
        if (staged != null) {
            return staged;
        }
        return delegate.readBytes(path);
    }

    @Override
    public void writeBytes(Path path, byte[] bytes) {
        stagedWrites.put(path, bytes);
    }

    public void commit() {
        Map<Path, byte[]> backups = new LinkedHashMap<>();
        try {
            for (Map.Entry<Path, byte[]> e : stagedWrites.entrySet()) {
                Path path = e.getKey();
                byte[] next = e.getValue();
                backups.put(path, safeRead(path));
                delegate.writeBytes(path, next);
            }
            stagedWrites.clear();
        } catch (Exception writeFailure) {
            for (Map.Entry<Path, byte[]> b : backups.entrySet()) {
                try {
                    delegate.writeBytes(b.getKey(), b.getValue());
                } catch (Exception ignored) {
                }
            }
            throw writeFailure instanceof RuntimeException ? (RuntimeException) writeFailure : new RuntimeException(writeFailure);
        }
    }

    public void rollback() {
        stagedWrites.clear();
    }

    private byte[] safeRead(Path path) {
        try {
            return delegate.readBytes(path);
        } catch (IOException e) {
            return new byte[0];
        }
    }
}

