package service.impl;

import object.Book;
import object.Order;
import service.BookService;
import service.OrderService;
import service.OrderViewService;
import service.UserService;

import java.util.List;

public class OrderViewServiceImpl implements OrderViewService {
    private final OrderService orderService;
    private final UserService userService;
    private final BookService bookService;

    public OrderViewServiceImpl(OrderService orderService, UserService userService, BookService bookService) {
        this.orderService = orderService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Override
    public List<Order> getOpenOrdersReport() {
        List<Order> orders = orderService.getAllOrders();
        for (Order order : orders) {
            Book book = bookService.getById(order.getBookId());
            order.setBookName(book.getBookName());
            order.setBookAuthor(book.getAuthorName());
            order.setUserName(userService.getUserById(order.getUserId()).getUserName());
        }
        return orders;
    }
}

