package expression.exceptions;

import expression.AbstractBinaryOperation;
import expression.CommonExpression;
import expression.Priority;
import expression.PriorityEnum;

public class Max extends AbstractBinaryOperation {

    private static final Priority PRIORITY = new Priority(PriorityEnum.MAX, false);

    protected Max(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) throws OverflowException {
        return first > second ? first : second;
    }

    @Override
    protected double makeOperation(double first, double second) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String getSign() {
        return "max";
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
