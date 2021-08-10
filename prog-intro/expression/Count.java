package expression;

public class Count extends AbstractUnaryOperation {

    public Count(CommonExpression child) {
        super(child);
    }

    @Override
    protected String getSign() {
        return "count ";
    }

    @Override
    protected int makeOperation(int value) {
        return Integer.bitCount(value);
    }
}
