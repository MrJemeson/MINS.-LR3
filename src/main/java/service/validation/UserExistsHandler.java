package service.validation;

import exception.NoSuchUserException;

public class UserExistsHandler extends AbstractChainHandler<CreateOrderContext> {
    @Override
    protected void doHandle(CreateOrderContext context) {
        context.users.findByUserId(context.userId)
                .orElseThrow(() -> new NoSuchUserException("No such user with id " + context.userId));
    }
}

