package expression.operations;

import expression.proxies.Proxy;

public class Abs<T> extends AbstractUnaryOperation<T> {

    public Abs(CommonExpression<T> child, Proxy<T> proxy) {
        super(child, proxy);
    }

    @Override
    protected String getSign() {
        return "abs";
    }

    @Override
    protected T apply(T value) {
        return super.proxy.abs(value);
    }
}
