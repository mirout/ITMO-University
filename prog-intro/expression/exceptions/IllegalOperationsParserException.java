package expression.exceptions;

public class IllegalOperationsParserException extends ExpressionParserException {

    public IllegalOperationsParserException(String message, Exception e) {
        super(message, e);
    }

    public IllegalOperationsParserException(String message) {
        super(message);
    }
}
