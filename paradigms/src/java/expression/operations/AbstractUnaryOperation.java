package expression.operations;

import expression.proxies.Proxy;

public abstract class AbstractUnaryOperation<T> implements CommonExpression<T>, ExpressionPriority{

    private static final Priority PRIORITY = new Priority(PriorityEnum.UNARY, false);

    private final CommonExpression<T> child;
    protected final Proxy<T> proxy;

    public AbstractUnaryOperation(CommonExpression<T> child, Proxy<T> proxy) {
        this.child = child;
        this.proxy = proxy;
    }

    @Override
    public T evaluate(T x) {
        return apply(child.evaluate(x));
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return apply(child.evaluate(x, y, z));
    }

    @Override
    public String toMiniString() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getSign(), child.toString());
    }

    protected abstract String getSign();
    protected abstract T apply(T value);
}
