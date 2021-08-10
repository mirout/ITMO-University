package expression.expressionParser;

import expression.operations.CommonExpression;
import expression.exceptions.ExpressionParserException;

public interface Parser<T> {
    CommonExpression<T> parse(String expression) throws ExpressionParserException;
}
