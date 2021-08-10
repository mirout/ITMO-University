package expression.operations;

public class Variable<T> implements CommonExpression<T> {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public T evaluate(T x) {
        return x;
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        switch (name) {
            case "x" : return x;
            case "y" : return y;
            case "z" : return z;
            default : throw new IllegalArgumentException();
        }
    }
}
