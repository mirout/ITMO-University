package expression;

public class Multiply extends AbstractBinaryOperation {

    private static final Priority PRIORITY = new Priority(PriorityEnum.MULTIPLY, false);

    public Multiply(CommonExpression left, CommonExpression right) {
        super(left, right);
    }

    @Override
    protected int makeOperation(int first, int second) {
        return first * second;
    }

    @Override
    protected double makeOperation(double first, double second) {
        return first*second;
    }

    @Override
    protected String getSign() {
        return "*";
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
