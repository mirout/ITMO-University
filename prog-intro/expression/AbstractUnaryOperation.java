package expression;

public abstract class AbstractUnaryOperation implements CommonExpression, ExpressionPriority{

    private static final Priority PRIORITY = new Priority(PriorityEnum.UNARY, false);

    private final CommonExpression child;

    public AbstractUnaryOperation(CommonExpression child) {
        this.child = child;
    }

    @Override
    public double evaluate(double x) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int evaluate(int x) {
        return makeOperation(child.evaluate(x));
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOperation(child.evaluate(x, y, z));
    }

    @Override
    public String toMiniString() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getSign(), child.toString());
    }

    protected abstract String getSign();
    protected abstract int makeOperation(int value);
}
