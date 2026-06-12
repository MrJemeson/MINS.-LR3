package csv;

import object.Book;

import java.util.StringJoiner;

public class BookCsvMapper implements CsvMapper<Book> {
    @Override
    public String headerLine() {
        return "\"ID\",\"BookName\",\"AuthorName\",\"Taken\"";
    }

    @Override
    public Book parse(String[] parts) {
        if (parts.length != 4) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        int id = Integer.parseInt(parts[0].trim());
        String bookName = parts[1].trim();
        String authorName = parts[2].trim();
        boolean takenStatus = parts[3].trim().equals("true");
        return new Book(id, bookName, authorName, takenStatus);
    }

    @Override
    public String toLine(Book book) {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(Integer.toString(book.getId()))
                .add(book.getBookName())
                .add(book.getAuthorName())
                .add(book.isTakenStatus() ? "true" : "false");
        return joiner.toString();
    }
}

