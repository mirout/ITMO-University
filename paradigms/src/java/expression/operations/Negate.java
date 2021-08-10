package expression.operations;

import expression.proxies.Proxy;

public class Negate<T> extends AbstractUnaryOperation<T> {

    public Negate(CommonExpression<T> child, Proxy<T> proxy) {
        super(child, proxy);
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    protected T apply(T value) {
        return super.proxy.negate(value);
    }

}
