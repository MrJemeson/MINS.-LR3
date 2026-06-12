package repository.impl;

import csv.CsvIO;
import csv.CsvMapper;
import csv.OrderCsvMapper;
import object.Order;
import repository.OrderRepository;
import storage.FileStorage;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class CsvOrderRepositoryImpl implements OrderRepository {
    private final Path csvPath;
    private final FileStorage storage;
    private final CsvMapper<Order> mapper;

    public CsvOrderRepositoryImpl(Path csvPath, FileStorage storage) {
        this(csvPath, storage, new OrderCsvMapper());
    }

    public CsvOrderRepositoryImpl(Path csvPath, FileStorage storage, CsvMapper<Order> mapper) {
        this.csvPath = csvPath;
        this.storage = storage;
        this.mapper = mapper;
    }

    private List<Order> loadOrders(){
        return CsvIO.readAll(csvPath, storage, mapper);
    }

    private void saveOrders(List<Order> orders) {
        CsvIO.writeAll(csvPath, storage, mapper, orders);
    }

    @Override
    public Optional<List<Order>> findByUserId(int userId) {
        List<Order> orders = loadOrders();
        orders = orders.stream().filter(x -> x.getUserId() == userId).toList();
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orders);
    }

    @Override
    public Optional<Order> findById(int orderId) {
        List<Order> orders = loadOrders();
        return orders.stream().filter(x -> x.getId() == orderId).findFirst();
    }

    @Override
    public void addOrder(Order order) {
        List<Order> orders = loadOrders();
        orders.add(order);
        saveOrders(orders);
    }

    @Override
    public int findLastOrderId() {
        List<Order> orders = loadOrders();
        return orders.stream().max(Comparator.comparingInt(Order::getId)).map(Order::getId).orElse(0);

    }

    @Override
    public void closeOrder(int orderId) {
        List<Order> orders = loadOrders();
        Order order = orders.stream().filter(x -> x.getId() == orderId).findFirst().orElseThrow();
        order.setClosedStatus(true);
        saveOrders(orders);
    }

    @Override
    public Optional<List<Order>> getAllOrders() {
        List<Order> orders = loadOrders();
        if (orders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(orders);
    }
}
