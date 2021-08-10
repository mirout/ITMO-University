package expression.operations;

import expression.operations.AbstractUnaryOperation;
import expression.operations.CommonExpression;
import expression.proxies.Proxy;

public class Square<T> extends AbstractUnaryOperation<T> {

    public Square(CommonExpression<T> child, Proxy<T> proxy) {
        super(child, proxy);
    }

    @Override
    protected String getSign() {
        return "square";
    }

    @Override
    protected T apply(T value) {
        return super.proxy.square(value);
    }
}
