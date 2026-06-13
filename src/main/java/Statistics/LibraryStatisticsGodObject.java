package statistics;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;


public class LibraryStatisticsGodObject {

    private static final String USERS_PATH = "data/users.csv";
    private static final String BOOKS_PATH = "data/books.csv";
    private static final String ORDERS_PATH = "data/orders.csv";

    public void printFullReport() {
        StatisticsContext context = new StatisticsContext();
        StatsChainHandler chain = new ReadUsersHandler();
        chain.setNext(new ReadBooksHandler())
                .setNext(new ReadOrdersHandler())
                .setNext(new ComputeStatisticsHandler())
                .setNext(new PrintReportHandler());
        chain.handle(context);
    }

    private static List<String[]> readCsvRows(String hardCodedPath) {
        Path path = Path.of(hardCodedPath);
        String content;
        try {
            byte[] bytes;
            if (!Files.exists(path)) {
                bytes = new byte[0];
            } else {
                bytes = Files.readAllBytes(path);
            }
            content = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV: " + path, e);
        }

        if (content.isBlank()) {
            return List.of();
        }

        List<String[]> values = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new StringReader(content))) {
            String[] parts;
            boolean firstLine = true;
            while ((parts = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (parts.length == 0) {
                    continue;
                }
                values.add(parts);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException("Failed to parse CSV: " + path, e);
        }

        return values;
    }

    private static class StatisticsContext {
        private final Map<Integer, String> userNames = new HashMap<>();
        private final Map<Integer, String> bookTitles = new HashMap<>();
        private final List<int[]> orders = new ArrayList<>();
        private final List<String> takenBooks = new ArrayList<>();

        private int openOrdersCount;
        private int closedOrdersCount;
        private String topUserLine;
    }

    private static abstract class StatsChainHandler {
        private StatsChainHandler next;

        final StatsChainHandler setNext(StatsChainHandler next) {
            this.next = next;
            return next;
        }

        final void handle(StatisticsContext context) {
            doHandle(context);
            if (next != null) {
                next.handle(context);
            }
        }

        protected abstract void doHandle(StatisticsContext context);
    }

    private static class ReadUsersHandler extends StatsChainHandler {
        @Override
        protected void doHandle(StatisticsContext context) {
            for (String[] row : readCsvRows(USERS_PATH)) {
                int id = Integer.parseInt(row[0].trim());
                String name = row[1].trim();
                context.userNames.put(id, name);
            }
        }
    }

    private static class ReadBooksHandler extends StatsChainHandler {
        @Override
        protected void doHandle(StatisticsContext context) {
            for (String[] row : readCsvRows(BOOKS_PATH)) {
                int id = Integer.parseInt(row[0].trim());
                String title = row[1].trim();
                String author = row[2].trim();
                boolean taken = Boolean.parseBoolean(row[3].trim());
                context.bookTitles.put(id, title);
                if (taken) {
                    context.takenBooks.add(title + " (" + author + ")");
                }
            }
        }
    }

    private static class ReadOrdersHandler extends StatsChainHandler {
        @Override
        protected void doHandle(StatisticsContext context) {
            for (String[] row : readCsvRows(ORDERS_PATH)) {
                int id = Integer.parseInt(row[0].trim());
                int userId = Integer.parseInt(row[1].trim());
                int bookId = Integer.parseInt(row[2].trim());
                boolean closed = Boolean.parseBoolean(row[3].trim());
                context.orders.add(new int[]{id, userId, bookId, closed ? 1 : 0});
            }
        }
    }

    private static class ComputeStatisticsHandler extends StatsChainHandler {
        @Override
        protected void doHandle(StatisticsContext context) {
            int open = 0;
            int closed = 0;
            Map<Integer, Integer> ordersPerUser = new HashMap<>();

            for (int[] order : context.orders) {
                int userId = order[1];
                boolean isClosed = order[3] == 1;
                if (isClosed) {
                    closed++;
                } else {
                    open++;
                }
                ordersPerUser.merge(userId, 1, Integer::sum);
            }

            context.openOrdersCount = open;
            context.closedOrdersCount = closed;

            context.topUserLine = ordersPerUser.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(entry -> {
                        String userName = context.userNames.getOrDefault(entry.getKey(), "user#" + entry.getKey());
                        return userName + " — " + entry.getValue() + " order(s)";
                    })
                    .orElse("no orders");
        }
    }

    private static class PrintReportHandler extends StatsChainHandler {
        @Override
        protected void doHandle(StatisticsContext context) {
            System.out.println("############# Statistics #############");
            System.out.println("Users: " + context.userNames.size());
            System.out.println("Books: " + context.bookTitles.size());
            System.out.println("Orders: " + context.orders.size());
            System.out.println("Open orders: " + context.openOrdersCount);
            System.out.println("Closed orders: " + context.closedOrdersCount);
            System.out.println("Most active user: " + context.topUserLine);
            System.out.println("Books currently taken:");
            if (context.takenBooks.isEmpty()) {
                System.out.println("  (none)");
            } else {
                for (int i = 0; i < context.takenBooks.size(); i++) {
                    System.out.println("  " + (i + 1) + ") " + context.takenBooks.get(i));
                }
            }
        }
    }
}
