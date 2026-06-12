package service.validation;

public abstract class AbstractChainHandler<T> {
    private AbstractChainHandler<T> next;

    public final AbstractChainHandler<T> setNext(AbstractChainHandler<T> next) {
        this.next = next;
        return next;
    }


    public final void handle(T context) {
        doHandle(context);
        if (next != null) {
            next.handle(context);
        }
    }

    protected abstract void doHandle(T context);
}

