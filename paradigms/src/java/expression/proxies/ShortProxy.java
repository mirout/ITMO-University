package expression.proxies;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class ShortProxy implements Proxy<Short> {

    @Override
    public Short add(Short x, Short y) {
        return (short)(x + y);
    }

    @Override
    public Short subtract(Short x, Short y) {
        return (short)(x - y);
    }

    @Override
    public Short divide(Short x, Short y) {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        return (short)(x / y);
    }

    @Override
    public Short multiply(Short x, Short y) {
        return (short)(x * y);
    }

    @Override
    public Short abs(Short value) {
        if (value > 0) {
            return value;
        }
        return (short)(-value);
    }

    @Override
    public Short count(Short value) {
        return (short) Integer.bitCount((int) value);
    }

    @Override
    public Short max(Short x, Short y) {
        return x > y ? x : y;
    }

    @Override
    public Short min(Short x, Short y) {
        return x < y ? x : y;
    }

    @Override
    public Short negate(Short value) {
        return (short) -value;
    }

    @Override
    public Short sqrt(Short value) {
        if (value < 0) {
            throw new OverflowException("Sqrt can't be negate");
        }
        return (short) Math.sqrt(value);
    }

    @Override
    public Short value(String value) {
        return Short.parseShort(value);
    }

    @Override
    public Short value(int value) {
        return (short) value;
    }

    @Override
    public Short square(Short value) {
        return multiply(value, value);
    }

    @Override
    public Short mod(Short x, Short y) {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        return (short)(x % y);
    }
}
