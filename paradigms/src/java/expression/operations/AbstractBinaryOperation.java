package expression.operations;

import expression.proxies.Proxy;

public abstract class AbstractBinaryOperation<T> implements CommonExpression<T>, ExpressionPriority {
    private final CommonExpression<T> child1;
    private final CommonExpression<T> child2;
    protected final Proxy<T> proxy;

    protected AbstractBinaryOperation(CommonExpression<T> left, CommonExpression<T> right, Proxy<T> proxy) {
        this.child1 = left;
        this.child2 = right;
        this.proxy = proxy;
    }

    @Override
    public T evaluate(T x) {
        return apply(child1.evaluate(x), child2.evaluate(x));
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return apply(child1.evaluate(x, y, z), child2.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", child1.toString(), getSign(), child2.toString());
    }

    @Override
    public String toMiniString() {
        return String.format("%s %s %s", addBrackets(child1, true), getSign(), addBrackets(child2, false));
    }

    private String addBrackets(CommonExpression<T> expression, boolean isLeft) {
        if (expression instanceof ExpressionPriority && getPriority().isBracketsNeed(((ExpressionPriority) expression).getPriority(), isLeft)) {
            return String.format("(%s)", expression.toMiniString());
        }
        return expression.toMiniString();
    }

    protected abstract T apply(T first, T second);

    protected abstract String getSign();
}
