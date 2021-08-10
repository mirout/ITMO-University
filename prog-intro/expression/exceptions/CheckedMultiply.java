package expression.exceptions;

import expression.CommonExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        int max = Integer.signum(first) == Integer.signum(second) ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        if (first == -1 && second == Integer.MIN_VALUE
                || first != -1 && first != 0 && ((second > 0 && second > max / first) || (second < 0 && second < max / first ))) {
            throw new OverflowException(String.format("Multiply overflow: %d * %d", first, second));
        }
        return super.makeOperation(first, second);
    }
}
