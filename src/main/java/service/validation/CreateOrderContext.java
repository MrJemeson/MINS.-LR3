package service.validation;

import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;

public class CreateOrderContext {
    public final int userId;
    public final int bookId;
    public final UserRepository users;
    public final BookRepository books;
    public final OrderRepository orders;

    public CreateOrderContext(int userId, int bookId, UserRepository users, BookRepository books, OrderRepository orders) {
        this.userId = userId;
        this.bookId = bookId;
        this.users = users;
        this.books = books;
        this.orders = orders;
    }
}

