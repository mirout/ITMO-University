package expression.proxies;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class LongProxy implements Proxy<Long> {

    @Override
    public Long add(Long x, Long y) {
        if (x > 0 && y > 0 && Long.MAX_VALUE - x < y) {
            throw new OverflowException(String.format("Add overflow: %d + %d", x, y));
        } else if (x < 0 && y < 0 && Long.MIN_VALUE - x > y) {
            throw new OverflowException(String.format("Add overflow: %d + %d", x, y));
        }
        return x + y;
    }

    @Override
    public Long subtract(Long x, Long y) {
        if ((y > 0 && x < Long.MIN_VALUE + y) || (y < 0 && x > Long.MAX_VALUE + y)) {
            throw new OverflowException(String.format("Subtract overflow: %d - %d", x, y));
        }
        return x - y;
    }

    @Override
    public Long divide(Long x, Long y) {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        if (x == Long.MIN_VALUE && y == -1) {
            throw new OverflowException(String.format("Division overflow: %d / %d", x, y));
        }
        return x / y;
    }

    @Override
    public Long multiply(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long abs(Long value) {
        if (value > 0) {
            return value;
        }
        if (value == Long.MIN_VALUE) {
            throw new OverflowException("Absolute overflow");
        }
        return -value;
    }

    @Override
    public Long count(Long value) {
        return (long) Long.bitCount(value);
    }

    @Override
    public Long max(Long x, Long y) {
        return x > y ? x : y;
    }

    @Override
    public Long min(Long x, Long y) {
        return x < y ? x : y;
    }

    @Override
    public Long negate(Long value) {
        return -value;
    }

    @Override
    public Long sqrt(Long value) {
        return (long) Math.sqrt(value);
    }

    @Override
    public Long value(String value) {
        return Long.parseLong(value);
    }

    @Override
    public Long value(int value) {
        return (long) value;
    }

    @Override
    public Long square(Long value) {
        return multiply(value, value);
    }

    @Override
    public Long mod(Long x, Long y) {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        return x % y;
    }
}
