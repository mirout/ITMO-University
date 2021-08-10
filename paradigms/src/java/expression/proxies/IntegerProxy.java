package expression.proxies;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

import java.math.BigInteger;

public class IntegerProxy implements Proxy<Integer> {

    private final boolean checked;

    public IntegerProxy(boolean checked) {
        this.checked = checked;
    }

    @Override
    public Integer add(Integer x, Integer y) {
        if (checked) {
            checkAdd(x, y);
        }
        return x + y;
    }

    private void checkAdd(Integer x, Integer y) {
        if (x > 0 && y > 0 && Integer.MAX_VALUE - x < y) {
            throw new OverflowException(String.format("Add overflow: %d + %d", x, y));
        } else if (x < 0 && y < 0 && Integer.MIN_VALUE - x > y) {
            throw new OverflowException(String.format("Add overflow: %d + %d", x, y));
        }
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        if (checked) {
            checkedSubtract(x, y);
        }
        return x - y;
    }

    private void checkedSubtract(Integer x, Integer y) {
        if ((y > 0 && x < Integer.MIN_VALUE + y) || (y < 0 && x > Integer.MAX_VALUE + y)) {
            throw new OverflowException(String.format("Subtract overflow: %d - %d", x, y));
        }
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        if (checked) {
            checkedDivide(x, y);
        }
        return x / y;
    }

    private void checkedDivide(Integer x, Integer y) {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException(String.format("Division overflow: %d / %d", x, y));
        }
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        if (checked) {
            checkedMultiply(x, y);
        }
        return x * y;
    }

    private void checkedMultiply(Integer x, Integer y) {
        int max = Integer.signum(x) == Integer.signum(y) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        if (x == -1 && y == Integer.MIN_VALUE
                || x != -1 && x != 0 && ((y > 0 && y > max / x) || (y < 0 && y < max / x))) {
            throw new OverflowException(String.format("Multiply overflow: %d * %d", x, y));
        }
    }

    @Override
    public Integer abs(Integer value) {
        if (value > 0) {
            return value;
        }
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("Absolute overflow");
        }
        return -value;
    }

    @Override
    public Integer count(Integer value) {
        return Integer.bitCount(value);
    }

    @Override
    public Integer max(Integer x, Integer y) {
        return x > y ? x : y;
    }

    @Override
    public Integer min(Integer x, Integer y) {
        return x < y ? x : y;
    }

    @Override
    public Integer negate(Integer value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("Negate overflow");
        }
        return -value;
    }

    @Override
    public Integer sqrt(Integer value) {
        if (value < 0) {
            throw new OverflowException("Sqrt can't be negate");
        }
        return (int) Math.sqrt(value);
    }

    @Override
    public Integer value(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Integer value(int value) {
        return value;
    }

    @Override
    public Integer square(Integer value) {
        return multiply(value, value);
    }

    @Override
    public Integer mod(Integer x, Integer y) {
        if (y.equals(0)) {
            throw new DivisionByZeroException("Division by zero");
        }
        return x % y;
    }
}
