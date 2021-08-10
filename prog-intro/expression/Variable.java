package expression;

public class Variable implements CommonExpression {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == Variable.class) {
            return name.equals(((Variable) obj).name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    @Override
    public String toMiniString() {
        return name;
    }

    @Override
    public double evaluate(double x) {
        return x;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (name) {
            case "x" : return x;
            case "y" : return y;
            case "z" : return z;
            default : throw new IllegalArgumentException();
        }
    }
}
