package expression;

public class Const implements CommonExpression{

    private final Number value;

    public Const(int value) {
        this.value = value;
    }

    public Const(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == Const.class) {
            return value.equals(((Const) obj).value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public double evaluate(double x) {
        return value.doubleValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }
}
