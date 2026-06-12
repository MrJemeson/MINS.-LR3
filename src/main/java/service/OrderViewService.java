package service;

import object.Order;

import java.util.List;

public interface OrderViewService {
    List<Order> getOpenOrdersReport();
}

