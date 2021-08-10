package expression.proxies;

import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class BigIntegerProxy implements Proxy<BigInteger> {
    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        if (y.equals(BigInteger.valueOf(0))) {
            throw new DivisionByZeroException("Division by zero");
        }
        return x.divide(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger abs(BigInteger value) {
        return value.abs();
    }

    @Override
    public BigInteger count(BigInteger value) {
        return BigInteger.valueOf(value.bitCount());
    }

    @Override
    public BigInteger max(BigInteger x, BigInteger y) {
        return x.max(y);
    }

    @Override
    public BigInteger min(BigInteger x, BigInteger y) {
        return x.min(y);
    }

    @Override
    public BigInteger negate(BigInteger value) {
        return value.multiply(BigInteger.valueOf(-1));
    }

    @Override
    public BigInteger sqrt(BigInteger value) {
        return value.sqrt();
    }

    @Override
    public BigInteger value(String value) {
        return new BigInteger(value);
    }

    @Override
    public BigInteger value(int value) {
        return BigInteger.valueOf(value);
    }

    @Override
    public BigInteger square(BigInteger value) {
        return value.pow(2);
    }

    @Override
    public BigInteger mod(BigInteger first, BigInteger second) {
        if (second.equals(BigInteger.valueOf(0))) {
            throw new DivisionByZeroException("Division by zero");
        }
        return first.mod(second);
    }
}
