package expression.exceptions;

import expression.CommonExpression;
import expression.Divide;

public class CheckedDivide extends Divide {
    public CheckedDivide(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        if (second == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        if (first == Integer.MIN_VALUE && second == -1) {
            throw new OverflowException(String.format("Division overflow: %d / %d", first, second));
        }
        return super.makeOperation(first, second);
    }

    @Override
    protected double makeOperation(double first, double second) {
        return super.makeOperation(first, second);
    }
}
