package unitofwork;

import storage.FileStorage;

import java.nio.file.Path;

public class CsvUnitOfWorkFactory implements UnitOfWorkFactory {
    private final FileStorage baseStorage;
    private final Path usersPath;
    private final Path booksPath;
    private final Path ordersPath;

    public CsvUnitOfWorkFactory(FileStorage baseStorage, Path usersPath, Path booksPath, Path ordersPath) {
        this.baseStorage = baseStorage;
        this.usersPath = usersPath;
        this.booksPath = booksPath;
        this.ordersPath = ordersPath;
    }

    @Override
    public UnitOfWork create() {
        return new CsvUnitOfWork(baseStorage, usersPath, booksPath, ordersPath);
    }
}

