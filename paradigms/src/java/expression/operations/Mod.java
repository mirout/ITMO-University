package expression.operations;

import expression.proxies.Proxy;

public class Mod<T> extends AbstractBinaryOperation<T> {

    private static final Priority PRIORITY = new Priority(PriorityEnum.MULTIPLY, true);

    public Mod(CommonExpression<T> left, CommonExpression<T> right, Proxy<T> proxy) {
        super(left, right, proxy);
    }

    @Override
    protected T apply(T first, T second) {
        return super.proxy.mod(first, second);
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
