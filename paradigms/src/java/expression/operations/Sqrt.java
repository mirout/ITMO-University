package expression.operations;

import expression.proxies.Proxy;

public class Sqrt<T> extends AbstractUnaryOperation<T> {

    public Sqrt(CommonExpression<T> child, Proxy<T> proxy) {
        super(child, proxy);
    }

    @Override
    protected String getSign() {
        return "sqrt";
    }

    @Override
    protected T apply(T value) {
        return super.proxy.sqrt(value);
    }
}
