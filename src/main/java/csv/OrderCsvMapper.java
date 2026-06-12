package csv;

import object.Order;

import java.util.StringJoiner;

public class OrderCsvMapper implements CsvMapper<Order> {
    @Override
    public String headerLine() {
        return "\"ID\",\"UserId\",\"BookId\",\"ClosedStatus\"";
    }

    @Override
    public Order parse(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        int id = Integer.parseInt(parts[0].trim());
        int userId = Integer.parseInt(parts[1].trim());
        int bookId = Integer.parseInt(parts[2].trim());
        boolean closedStatus = parts[3].trim().equals("true");
        return new Order(id, bookId, userId, closedStatus);
    }

    @Override
    public String toLine(Order order) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(Integer.toString(order.getId()))
                .add(Integer.toString(order.getUserId()))
                .add(Integer.toString(order.getBookId()))
                .add(order.isClosedStatus() ? "true" : "false");
        return joiner.toString();
    }
}

