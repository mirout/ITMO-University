package expression;

public class Not extends AbstractUnaryOperation {

    public Not(CommonExpression child) {
        super(child);
    }

    @Override
    protected String getSign() {
        return "~";
    }

    @Override
    protected int makeOperation(int value) {
        return ~value;
    }
}
