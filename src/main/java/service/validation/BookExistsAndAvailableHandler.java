package service.validation;

import exception.NoSuchBookException;

public class BookExistsAndAvailableHandler extends AbstractChainHandler<CreateOrderContext> {
    @Override
    protected void doHandle(CreateOrderContext context) {
        var book = context.books.findById(context.bookId)
                .orElseThrow(() -> new NoSuchBookException("Book id: " + context.bookId + " does not exist"));
        if (book.isTakenStatus()) {
            throw new NoSuchBookException("Book id: " + context.bookId + " is already taken");
        }
    }
}

