package unitofwork;

import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;

public interface UnitOfWork extends AutoCloseable {
    UserRepository users();
    BookRepository books();
    OrderRepository orders();

    void commit();
    void rollback();

    @Override
    default void close() {
        rollback();
    }
}

