package expression.exceptions;

public interface CharSource {
    boolean hasNext();
    char next();
    boolean hasPrevious();
    char previous();
    int getPosition();
}
