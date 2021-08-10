package expression.operations;

import expression.proxies.Proxy;

public class Count<T> extends AbstractUnaryOperation<T> {

    public Count(CommonExpression<T> child, Proxy<T> proxy) {
        super(child, proxy);
    }

    @Override
    protected String getSign() {
        return "count ";
    }

    @Override
    protected T apply(T value) {
        return super.proxy.count(value);
    }
}
