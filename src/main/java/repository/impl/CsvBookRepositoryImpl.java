package repository.impl;

import csv.BookCsvMapper;
import csv.CsvIO;
import csv.CsvMapper;
import object.Book;
import repository.BookRepository;
import storage.FileStorage;

import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class CsvBookRepositoryImpl implements BookRepository {
    private final Path csvPath;
    private final FileStorage storage;
    private final CsvMapper<Book> mapper;

    public CsvBookRepositoryImpl(Path csvPath, FileStorage storage) {
        this(csvPath, storage, new BookCsvMapper());
    }

    public CsvBookRepositoryImpl(Path csvPath, FileStorage storage, CsvMapper<Book> mapper) {
        this.csvPath = csvPath;
        this.storage = storage;
        this.mapper = mapper;
    }

    private List<Book> loadBooks(){
        return CsvIO.readAll(csvPath, storage, mapper);
    }

    private void saveBooks(List<Book> books) {
        CsvIO.writeAll(csvPath, storage, mapper, books);
    }
    @Override
    public Optional<Book> findById(int bookId) {
        List<Book> books = loadBooks();
        try {
            Book book = books.stream().filter(x -> x.getId() == bookId).findFirst().orElseThrow();
            return Optional.of(book);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Book>> findByName(String bookName) {
        List<Book> books = loadBooks();
        books = books.stream().filter(x -> x.getBookName().toLowerCase().equals(bookName)).toList();
        if (books.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(books);
    }

    @Override
    public Optional<List<Book>> findByAuthor(String authorName) {
        List<Book> books = loadBooks();
        books = books.stream().filter(x -> x.getAuthorName().toLowerCase().equals(authorName)).toList();
        if (books.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(books);
    }

    @Override
    public void changeStatus(int bookId, boolean isTaken) {
        List<Book> books = loadBooks();
        Book book = books.stream().filter(x -> x.getId() == bookId).findFirst().orElseThrow();
        book.setTakenStatus(isTaken);
        saveBooks(books);
    }
}