package expression;

import expression.exceptions.OverflowException;

public abstract class AbstractBinaryOperation implements CommonExpression, ExpressionPriority {
    private final CommonExpression child1;
    private final CommonExpression child2;

    protected AbstractBinaryOperation(CommonExpression left, CommonExpression right) {
        child1 = left;
        child2 = right;
    }

    @Override
    public int evaluate(int x) {
        return makeOperation(child1.evaluate(x), child2.evaluate(x));
    }

    @Override
    public double evaluate(double x) {
        return makeOperation(child1.evaluate(x), child2.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return makeOperation(child1.evaluate(x, y, z), child2.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return String.format("(%s %s %s)", child1.toString(), getSign(), child2.toString());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            AbstractBinaryOperation that = (AbstractBinaryOperation) obj;
            return child1.equals(that.child1) && child2.equals(that.child2);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ((child1.hashCode() * 31 + child2.hashCode()) * 31 + getClass().hashCode()) * 31;
    }

    @Override
    public String toMiniString() {
        return String.format("%s %s %s", addBrackets(child1, true), getSign(), addBrackets(child2, false));
    }

    private String addBrackets(CommonExpression expression, boolean isLeft) {
        if (expression instanceof ExpressionPriority && getPriority().isBracketsNeed(((ExpressionPriority) expression).getPriority(), isLeft)) {
            return String.format("(%s)", expression.toMiniString());
        }
        return expression.toMiniString();
    }

    protected abstract int makeOperation(int first, int second) throws OverflowException;
    protected abstract double makeOperation(double first, double second);

    protected abstract String getSign();
}
