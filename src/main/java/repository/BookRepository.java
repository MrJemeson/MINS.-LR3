package repository;

import object.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Optional<Book> findById(int bookId);
    Optional<List<Book>> findByName(String bookName);
    Optional<List<Book>> findByAuthor(String authorName);
    void changeStatus(int bookId, boolean isTaken);
}
