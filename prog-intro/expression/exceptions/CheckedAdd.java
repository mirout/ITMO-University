package expression.exceptions;

import expression.Add;
import expression.CommonExpression;

public class CheckedAdd extends Add {

    public CheckedAdd(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        int result = super.makeOperation(first, second);
        if (first > 0 && second > 0 && (result < first || result < second)) {
            throw new OverflowException(String.format("Add overflow: %d + %d", first, second));
        } else if (first < 0 && second < 0 && (result > first || result > second)) {
            throw new OverflowException(String.format("Add overflow: %d + %d", first, second));
        }
        return result;
    }
}
