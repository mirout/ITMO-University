package expression.exceptions;

import expression.CommonExpression;
import expression.AbstractUnaryOperation;

public class Abs extends AbstractUnaryOperation {

    public Abs(CommonExpression child) {
        super(child);
    }

    @Override
    protected String getSign() {
        return "abs";
    }

    @Override
    protected int makeOperation(int value) {
        if (value > 0) {
            return value;
        }
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("Absolute overflow");
        }
        return -value;
    }
}
