package service.impl;

import exception.NoOrdersOnUserException;
import exception.NoSuchBookException;
import exception.NoSuchUserException;
import object.Book;
import object.Order;
import object.User;
import service.*;

import java.util.List;
import java.util.Optional;

public class UIServiceImpl implements UIService {
    private final OrderService orderService;
    private final UserService userService;
    private final BookService bookService;
    private final OrderViewService orderViewService;
    private final InputService inputService;
    private final OutputService outputService;

    public UIServiceImpl(
            OrderService orderService,
            UserService userService,
            BookService bookService,
            OrderViewService orderViewService,
            InputService inputService,
            OutputService outputService
    ) {
        this.orderService = orderService;
        this.userService = userService;
        this.bookService = bookService;
        this.orderViewService = orderViewService;
        this.inputService = inputService;
        this.outputService = outputService;
    }

    @Override
    public void mainMenu() {
        int intInput;
        while(true) {
            outputService.displayMainMenu();

            intInput = inputService.inputNumber();
            switch(intInput){
                case 1: {
                    createOrder();
                    break;
                }
                case 2: {
                    closeOrder();
                    break;
                }
                case 3: {
                    reportOpenOrders();
                    break;
                }
            }
        }
    }

    private User findingUser(){
        String userName;
        User user;
        outputService.displayUserNameInputMessage();
        userName = inputService.inputString();
        user = userService.getUserByName(userName);
        return user;
    }

    private Book findingBook(){
        String searchString;
        List<Book> openBooks;
        int bookNum;
        Book book;
        outputService.displayBookSearchInputMessage();
        searchString = inputService.inputString();
        openBooks = bookService.getOpenBooksByNameOrAuthor(searchString);
        outputService.displayList(openBooks);
        while(true){
            outputService.displayPositionNumber();
            bookNum = inputService.inputNumber();
            if (bookNum <= openBooks.size()) {
                book = openBooks.get(bookNum-1);
                break;
            } else outputService.displayWrongInputMessage();
        }
        return book;
    }

    private Optional<Order> choosingOrderForUser(User user) {
        List<Order> orders;
        int orderNum;
        Order order;
        try {
            orders = orderService.getAllOrdersByUserId(user.getId());
        } catch (NoOrdersOnUserException e) {
            outputService.displayExceptionMessage(e.getMessage());
            return Optional.empty();
        }
        orders = orders.stream().filter(x -> !x.isClosedStatus()).toList();
        outputService.displayList(orders);
        while(true){
            outputService.displayPositionNumber();
            orderNum = inputService.inputNumber();
            if (orderNum <= orders.size()) {
                order = orders.get(orderNum-1);
                break;
            } else outputService.displayWrongInputMessage();
        }
        return Optional.of(order);
    }

    private void createOrder() {
        User user;
        try{
            user = findingUser();
        } catch (NoSuchUserException e) {
            outputService.displayExceptionMessage(e.getMessage());
            return;
        }
        Book book;
        try {
            book = findingBook();
        } catch (NoSuchBookException e) {
            outputService.displayExceptionMessage(e.getMessage());
            return;
        }
        try {
            orderService.createOrder(user.getId(), book.getId());
        } catch (RuntimeException e) {
            outputService.displayExceptionMessage(e.getMessage());
        }
    }

    private void closeOrder() {
        User user;
        try {
            user = findingUser();
        } catch (NoSuchUserException e) {
            outputService.displayExceptionMessage(e.getMessage());
            return;
        }
        Optional<Order> order = choosingOrderForUser(user);
        if (order.isEmpty()) {
            return;
        }
        try {
            orderService.closeOrder(order.get().getId());
        } catch (RuntimeException e) {
            outputService.displayExceptionMessage(e.getMessage());
        }
    }

    private void reportOpenOrders(){
        List<Order> orders;
        try {
            orders = orderViewService.getOpenOrdersReport();
        } catch (NoOrdersOnUserException e) {
            outputService.displayExceptionMessage(e.getMessage());
            return;
        }
        outputService.displayList(orders);
    }

}
