package service.impl;

import exception.NoSuchBookException;
import object.Book;
import repository.BookRepository;
import service.BookService;

import java.util.ArrayList;
import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getOpenBooksByNameOrAuthor(String searchString) {
        List<Book> booksByName = bookRepository.findByName(searchString.toLowerCase()).orElse(List.of());
        List<Book> booksByAuthor = bookRepository.findByAuthor(searchString.toLowerCase()).orElse(List.of());
        ArrayList<Book> totalBooks = new ArrayList<>(booksByName);
        totalBooks.addAll(booksByAuthor);
        List<Book> totalOpenBooks = totalBooks.stream().filter(x -> !x.isTakenStatus()).toList();
        if (totalOpenBooks.isEmpty()){
            throw new NoSuchBookException("No available book for search key '" + searchString + "' found");
        }
        return totalOpenBooks;
    }

    @Override
    public Book getById(int bookId) {
        return bookRepository.findById(bookId).orElseThrow(() -> new NoSuchBookException("Book id: " + bookId + " does not exist"));
    }

    @Override
    public void takeBook(int bookId) {
        bookRepository.changeStatus(bookId, true);
    }

    @Override
    public void returnBook(int bookId) {
        bookRepository.changeStatus(bookId, false);
    }
}
