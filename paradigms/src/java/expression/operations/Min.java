package expression.operations;

import expression.proxies.Proxy;
import expression.exceptions.OverflowException;

public class Min<T> extends AbstractBinaryOperation<T> {

    private static final Priority PRIORITY = new Priority(PriorityEnum.MAX, false);

    public Min(CommonExpression<T> left, CommonExpression<T> right, Proxy<T> proxy) {
        super(left, right, proxy);
    }

    @Override
    protected T apply(T first, T second) throws OverflowException {
        return super.proxy.min(first, second);
    }

    @Override
    protected String getSign() {
        return "min";
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
