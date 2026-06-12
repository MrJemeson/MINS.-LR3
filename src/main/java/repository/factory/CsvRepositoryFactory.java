package repository.factory;

import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;
import repository.impl.CsvBookRepositoryImpl;
import repository.impl.CsvOrderRepositoryImpl;
import repository.impl.CsvUserRepositoryImpl;
import storage.FileStorage;

import java.nio.file.Path;

/** ConcreteCreator: CSV-реализации репозиториев. */
public class CsvRepositoryFactory extends RepositoryFactory {

    public CsvRepositoryFactory(FileStorage storage) {
        super(storage);
    }

    @Override
    public UserRepository createUserRepository(Path csvPath) {
        return new CsvUserRepositoryImpl(csvPath, storage);
    }

    @Override
    public BookRepository createBookRepository(Path csvPath) {
        return new CsvBookRepositoryImpl(csvPath, storage);
    }

    @Override
    public OrderRepository createOrderRepository(Path csvPath) {
        return new CsvOrderRepositoryImpl(csvPath, storage);
    }
}
