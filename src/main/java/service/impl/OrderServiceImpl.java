package service.impl;

import exception.NoOrdersOnUserException;
import object.Order;
import repository.BookRepository;
import repository.OrderRepository;
import repository.UserRepository;
import service.OrderService;
import service.validation.BookExistsAndAvailableHandler;
import service.validation.CreateOrderContext;
import service.validation.UserExistsHandler;
import unitofwork.UnitOfWorkFactory;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UnitOfWorkFactory uowFactory;

    public OrderServiceImpl(OrderRepository orderRepository, UnitOfWorkFactory uowFactory) {
        this.orderRepository = orderRepository;
        this.uowFactory = uowFactory;
    }

    @Override
    public void createOrder(int userId, int bookId) {
        try (var uow = uowFactory.create()) {
            UserRepository users = uow.users();
            BookRepository books = uow.books();
            OrderRepository orders = uow.orders();

            var ctx = new CreateOrderContext(userId, bookId, users, books, orders);
            var chain = new UserExistsHandler();
            chain.setNext(new BookExistsAndAvailableHandler());
            chain.handle(ctx);

            int nextId = orders.findLastOrderId() + 1;
            orders.addOrder(new Order(nextId, bookId, userId));
            books.changeStatus(bookId, true);
            uow.commit();
        }
    }

    @Override
    public void closeOrder(int orderId) {
        try (var uow = uowFactory.create()) {
            OrderRepository orders = uow.orders();
            BookRepository books = uow.books();

            Order order = orders.findById(orderId).orElseThrow();
            if (order.isClosedStatus()) {
                return;
            }
            orders.closeOrder(orderId);
            books.changeStatus(order.getBookId(), false);
            uow.commit();
        }
    }

    @Override
    public List<Order> getAllOrdersByUserId(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId).orElseThrow(() -> new NoOrdersOnUserException("No orders for User " + userId + " found"));
        orders = orders.stream().filter(x -> !x.isClosedStatus()).toList();
        if(orders.isEmpty()) {
            throw new NoOrdersOnUserException("No orders for User " + userId + " found");
        }
        return orders;
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.getAllOrders().orElseThrow(() -> new NoOrdersOnUserException("No orders exist"));
        orders = orders.stream().filter(x -> !x.isClosedStatus()).toList();
        if (orders.isEmpty()) throw new NoOrdersOnUserException("No open orders exist");
        return orders;
    }
}
