package expression.exceptions;

public class ExpressionParserException extends Exception {

    public ExpressionParserException(String message, Exception e) {
        super(message, e);
    }

    public ExpressionParserException(String message) {
        super(message);
    }
}
