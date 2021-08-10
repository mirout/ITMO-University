package expression.exceptions;

import expression.CommonExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        if ((second > 0 && first < Integer.MIN_VALUE + second) || (second < 0 && first > Integer.MAX_VALUE + second)) {
            throw new OverflowException(String.format("Subtract overflow: %d - %d", first, second));
        }
        return super.makeOperation(first, second);
    }
}
