package expression.proxies;

import expression.exceptions.DivisionByZeroException;

import java.math.BigInteger;

public class DoubleProxy implements Proxy<Double> {
    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double subtract(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double multiply(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double abs(Double value) {
        return value > 0 ? value : -value;
    }

    @Override
    public Double count(Double value) {
        return value(Long.bitCount(Double.doubleToLongBits(value)));
    }

    @Override
    public Double max(Double x, Double y) {
        return x > y ? x : y;
    }

    @Override
    public Double min(Double x, Double y) {
        return x < y ? x : y;
    }

    @Override
    public Double negate(Double value) {
        return -value;
    }

    @Override
    public Double sqrt(Double value) {
        return Math.sqrt(value);
    }

    @Override
    public Double value(String value) {
        return Double.parseDouble(value);
    }

    @Override
    public Double value(int value) {
        return (double) value;
    }

    @Override
    public Double square(Double value) {
        return value * value;
    }

    @Override
    public Double mod(Double x, Double y) {
        return x % y;
    }
}
