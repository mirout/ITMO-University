package expression;

public class Negate extends AbstractUnaryOperation {

    public Negate(CommonExpression child) {
        super(child);
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    protected int makeOperation(int value) {
        return -value;
    }

}
