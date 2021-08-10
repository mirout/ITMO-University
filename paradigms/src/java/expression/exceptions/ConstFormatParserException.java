package expression.exceptions;

public class ConstFormatParserException extends ExpressionParserException {
    public ConstFormatParserException(String message, Exception e) {
        super(message, e);
    }

    public ConstFormatParserException(String message) {
        super(message);
    }
}
