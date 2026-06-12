package repository.factory;

import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;
import storage.FileStorage;

import java.nio.file.Path;

/** Creator: фабричные методы для репозиториев (Factory Method). */
public abstract class RepositoryFactory {

    protected final FileStorage storage;

    protected RepositoryFactory(FileStorage storage) {
        this.storage = storage;
    }

    public abstract UserRepository createUserRepository(Path csvPath);

    public abstract BookRepository createBookRepository(Path csvPath);

    public abstract OrderRepository createOrderRepository(Path csvPath);
}
