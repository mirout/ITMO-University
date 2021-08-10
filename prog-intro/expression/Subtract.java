package expression;

public class Subtract extends AbstractBinaryOperation {
    private static final Priority PRIORITY = new Priority(PriorityEnum.ADD, true);

    public Subtract(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        return first - second;
    }

    @Override
    protected double makeOperation(double first, double second) {
        return first - second;
    }

    @Override
    protected String getSign() {
        return "-";
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
