package expression.exceptions;

import expression.CommonExpression;
import expression.AbstractUnaryOperation;

public class Sqrt extends AbstractUnaryOperation {

    public Sqrt(CommonExpression child) {
        super(child);
    }

    @Override
    protected String getSign() {
        return "sqrt";
    }

    @Override
    protected int makeOperation(int value) {
        if (value < 0) {
            throw new OverflowException("Sqrt can't ");
        }
        return (int) Math.sqrt(value);
    }
}
