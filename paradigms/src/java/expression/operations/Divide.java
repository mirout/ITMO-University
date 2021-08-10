package expression.operations;

import expression.proxies.Proxy;

public class Divide<T> extends AbstractBinaryOperation<T> {

    private static final Priority PRIORITY = new Priority(PriorityEnum.MULTIPLY, true);

    public Divide(CommonExpression<T> left, CommonExpression<T> right, Proxy<T> proxy) {
        super(left, right, proxy);
    }

    @Override
    protected T apply(T first, T second) {
        return proxy.divide(first, second);
    }

    @Override
    protected String getSign() {
        return "/";
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
