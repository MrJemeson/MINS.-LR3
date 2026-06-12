import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;
import repository.factory.CsvRepositoryFactory;
import repository.factory.RepositoryFactory;
import service.*;
import service.impl.*;

import java.nio.file.Path;
import storage.FileStorage;
import storage.NioFileStorage;
import unitofwork.CsvUnitOfWorkFactory;
import unitofwork.UnitOfWorkFactory;

public class App {
    public static void main(String[] args) {
        FileStorage storage = new NioFileStorage();
        RepositoryFactory repositoryFactory = new CsvRepositoryFactory(storage);
        Path usersPath = Path.of("data/users.csv");
        Path booksPath = Path.of("data/books.csv");
        Path ordersPath = Path.of("data/orders.csv");
        UserRepository userRepository = repositoryFactory.createUserRepository(usersPath);
        BookRepository bookRepository = repositoryFactory.createBookRepository(booksPath);
        OrderRepository orderRepository = repositoryFactory.createOrderRepository(ordersPath);
        InputService inputService = new InputServiceImpl();
        OutputService outputService = new OutputServiceImpl();
        UserService userService = new UserServiceImpl(userRepository);
        UnitOfWorkFactory uowFactory = new CsvUnitOfWorkFactory(storage, usersPath, booksPath, ordersPath);
        OrderService orderService = new OrderServiceImpl(orderRepository, uowFactory);
        BookService bookService = new BookServiceImpl(bookRepository);
        OrderViewService orderViewService = new OrderViewServiceImpl(orderService, userService, bookService);
        UIService uiService = new UIServiceImpl(orderService, userService, bookService, orderViewService, inputService, outputService);
        uiService.mainMenu();
    }
}
