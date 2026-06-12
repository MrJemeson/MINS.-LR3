package unitofwork;

import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;
import repository.factory.CsvRepositoryFactory;
import repository.factory.RepositoryFactory;
import storage.FileStorage;

import java.nio.file.Path;

public class CsvUnitOfWork implements UnitOfWork {
    private final TransactionalFileStorage txStorage;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;

    public CsvUnitOfWork(FileStorage baseStorage, Path usersPath, Path booksPath, Path ordersPath) {
        this.txStorage = new TransactionalFileStorage(baseStorage);
        RepositoryFactory factory = new CsvRepositoryFactory(txStorage);
        this.userRepository = factory.createUserRepository(usersPath);
        this.bookRepository = factory.createBookRepository(booksPath);
        this.orderRepository = factory.createOrderRepository(ordersPath);
    }

    @Override
    public UserRepository users() {
        return userRepository;
    }

    @Override
    public BookRepository books() {
        return bookRepository;
    }

    @Override
    public OrderRepository orders() {
        return orderRepository;
    }

    @Override
    public void commit() {
        txStorage.commit();
    }

    @Override
    public void rollback() {
        txStorage.rollback();
    }
}

