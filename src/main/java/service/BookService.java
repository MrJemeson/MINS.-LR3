package service;

import object.Book;

import java.util.List;

public interface BookService {
    List<Book> getOpenBooksByNameOrAuthor(String searchString);
    Book getById(int bookId);
    void takeBook(int bookId);
    void returnBook(int bookId);
}
