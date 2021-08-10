package expression.exceptions;

import expression.CommonExpression;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(CommonExpression child) {
        super(child);
    }

    @Override
    protected int makeOperation(int value) {
        if (value == Integer.MIN_VALUE) {
            throw new OverflowException("Negate overflow");
        }
        return super.makeOperation(value);
    }
}
