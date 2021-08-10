package expression;

public class And extends AbstractBinaryOperation {
    private static final Priority PRIORITY = new Priority(PriorityEnum.AND, false);

    public And(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        return first & second;
    }

    @Override
    protected double makeOperation(double first, double second) {
        throw new IllegalArgumentException();
    }

    @Override
    protected String getSign() {
        return "&";
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
