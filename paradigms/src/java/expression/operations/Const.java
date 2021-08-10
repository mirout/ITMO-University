package expression.operations;

public class Const<T> implements CommonExpression<T> {

    private final T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
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
    public T evaluate(T x) {
        return value;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }
}
