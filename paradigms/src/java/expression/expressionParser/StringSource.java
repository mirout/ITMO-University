package expression.expressionParser;

public class StringSource implements CharSource {
    private final String data;
    private int pos = -1;

    public StringSource(String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos + 1 < data.length();
    }

    @Override
    public char next() {
        return data.charAt(++pos);
    }

    @Override
    public boolean hasPrevious() {
        return pos > 0;
    }

    @Override
    public char previous() {
        return data.charAt(--pos);
    }

    @Override
    public int getPosition() {
        return pos;
    }
}
