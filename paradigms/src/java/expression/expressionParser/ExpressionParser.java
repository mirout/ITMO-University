package expression.expressionParser;

import expression.exceptions.*;
import expression.operations.*;
import expression.proxies.Proxy;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Set;

public class ExpressionParser<T> implements Parser<T> {

    private final static Map<String, PriorityEnum> PRIORITIES_BINARY_OPERANDS = Map.of(
                 "+", PriorityEnum.ADD,
                 "-", PriorityEnum.ADD,
                 "*", PriorityEnum.MULTIPLY,
                 "/", PriorityEnum.MULTIPLY,
                 "max", PriorityEnum.MAX,
                 "min", PriorityEnum.MAX,
                 "mod", PriorityEnum.MULTIPLY
    );

    public static final char END = '\0';
    public static final char START = 0xffff;

    private CharSource source;
    private char ch = START;
    private final ArrayDeque<CommonExpression<T>> leftChild = new ArrayDeque<>();
    private boolean isNegate = true;
    private boolean isPreviousOperation = true;
    private final Set<String> setOfVariables = Set.of("x", "y", "z");
    private final Set<String> setOfUnaryOperands = Set.of("count", "abs", "sqrt", "~", "-", "square");
    private final Proxy<T> proxy;

    public ExpressionParser(Proxy<T> proxy) {
        this.proxy = proxy;
    }

    @Override
    public CommonExpression<T> parse(String expression) throws ExpressionParserException {
        return parse(new StringSource(expression + " "));
    }

    private CommonExpression<T> parse(CharSource stringSource) throws ExpressionParserException {
        isNegate = true;
        isPreviousOperation = true;
        this.source = stringSource;
        nextChar();
        return parseExpression();
    }

    private CommonExpression<T> parseExpression() throws ExpressionParserException {
        skipWhitespaces();
        CommonExpression<T> result = parseElement(END);
        skipWhitespaces();
        return result;
    }

    private CommonExpression<T> parseElement(char end) throws ExpressionParserException {
        isNegate = true;
        while (ch != end) {
            skipWhitespaces();
            leftChild.push(parseValue(end));
            skipWhitespaces();
        }
        test(end);
        if (leftChild.size() == 0) {
            throw new ExpressionParserException("Expression don't contains anything");
        }
        return leftChild.pop();
    }

    private CommonExpression<T> parseValue(char end) throws ExpressionParserException {

        if (isDigit() || ch == '-' && isNegate) {
            isIllegalArgument();
            return parseDigit(end);
        }  else if (test('(')) {
            return parseElement(')');
        } else if (test(END)) {
            throw new ExpressionParserException("Unexpected end of expression");
        } else {
            String nextSubstring = readNextSubstring();
            if (setOfVariables.contains(nextSubstring)) {
                isIllegalArgument();
                isPreviousOperation = false;
                isNegate = false;
                return new Variable<>(nextSubstring);
            }
            if (setOfUnaryOperands.contains(nextSubstring)) {
                skipWhitespaces();
                return getUnaryOperation(parseValue(end), nextSubstring);
            }
            if (nextSubstring.isEmpty() || PRIORITIES_BINARY_OPERANDS.containsKey(nextSubstring)) {
                return parseOperation(end, nextSubstring);
            }
            throw new ExpressionParserException("Unexpected substring found: " + nextSubstring);
        }

    }

    private String readNextSubstring() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetterOrDigit(ch)) {
            sb.append(ch);
            nextChar();
        }
        String nextSubstring = sb.toString();
        return sb.toString();
    }

    private CommonExpression<T> parseMinus(char end) throws ExpressionParserException {
        skipWhitespaces();
        return new Negate<>(parseValue(end), proxy);
    }

    private CommonExpression<T> parseOperation(char end, String now) throws ExpressionParserException {
        if (isPreviousOperation) {
            throw new IllegalOperationsParserException(String.format("Unexpected new operation at %d", source.getPosition()));
        }

        isNegate = true;
        isPreviousOperation = true;

        String operand;
        if (now.isEmpty()) {
            operand = Character.toString(ch);
            nextChar();
        } else {
            operand = now;
        }

        skipWhitespaces();

        var firstChild = leftChild.pop();
        var secondChild = parseValue(end);

        skipWhitespaces();

        String nextOperand = getNextOperand();

        if (isBinary(nextOperand)) {
            if (priorityComparing(operand, nextOperand) >= 0) {
                rollBack(nextOperand.length());
                return getBinaryOperation(firstChild, secondChild, operand);
            } else {
                leftChild.push(secondChild);
                while (isBinary(nextOperand) && priorityComparing(operand, nextOperand) < 0) {
                    leftChild.push(parseOperation(end, nextOperand));
                    nextOperand = getNextOperand();
                }
                if (!nextOperand.equals(Character.toString(END))) {
                    rollBack(nextOperand.length());
                }
                return getBinaryOperation(firstChild, leftChild.pop(), operand);
            }
        } else {
            if (!nextOperand.equals(Character.toString(END))) {
                rollBack(nextOperand.length());
            }
            return getBinaryOperation(firstChild, secondChild, operand);
        }
    }

    private void rollBack(int length) {
        for (int i = 0; i < length; i++) {
            previousChar();
        }
    }

    private String getNextOperand() {
        String result = readNextSubstring();
        if (result.isEmpty()) {
            result = Character.toString(ch);
            nextChar();
        }
        return result;
    }

    private boolean isBinary(String operand) {
        if (operand.isEmpty()) {
            operand = Character.toString(ch);
        }
        return PRIORITIES_BINARY_OPERANDS.containsKey(operand);
    }

    private CommonExpression<T> getBinaryOperation(CommonExpression<T> firstChild, CommonExpression<T> secondChild, String operand) throws IllegalArgumentParserException {
        switch (operand) {
            case "+" : return new Add<>(firstChild, secondChild, proxy);
            case "-" : return new Subtract<>(firstChild, secondChild, proxy);
            case "*" : return new Multiply<>(firstChild, secondChild, proxy);
            case "/" : return new Divide<>(firstChild, secondChild, proxy);
            case "max" : return new Max<>(firstChild, secondChild, proxy);
            case "min" : return new Min<>(firstChild, secondChild, proxy);
            case "mod" : return new Mod<>(firstChild, secondChild, proxy);
            default : throw new IllegalArgumentParserException("Parser don't support this operation");
        }
    }

    private CommonExpression<T> getUnaryOperation(CommonExpression<T> child, String operand) throws IllegalArgumentParserException {
        switch (operand) {
            case "count" : return new Count<>(child, proxy);
            case "square" : return new Square<>(child, proxy);
            case "abs" : return new Abs<>(child, proxy);
            case "sqrt" : return new Sqrt<>(child, proxy);
            case "-" : return new Negate<>(child, proxy);
            default : throw new IllegalArgumentParserException("Parser don't support this operation");
        }
    }

    private CommonExpression<T> parseDigit(char end) throws ExpressionParserException {

        StringBuilder sb = new StringBuilder();

        if (ch == '-') {
            nextChar();
            skipWhitespaces();
            if (!isDigit()) {
                return parseMinus(end);
            }
            sb.append("-");
        }

        isNegate = false;
        isPreviousOperation = false;

        skipWhitespaces();

        while (isDigit()) {
            sb.append(ch);
            nextChar();
        }
        try {
            return new Const<>(proxy.value(sb.toString()));
        } catch (NumberFormatException e) {
            throw new ConstFormatParserException(String.format("Illegal const format at %d", source.getPosition()), e);
        }
    }

    private void isIllegalArgument() throws IllegalArgumentParserException {
        if (!isPreviousOperation) {
            throw new IllegalArgumentParserException(String.format("Unexpected new argument at %d", source.getPosition()));
        }
    }

    private boolean isDigit() {
        return Character.isDigit(ch);
    }

    private void skipWhitespaces() {
        while (test(' ') || test('\r') || test('\n') || test('\t')) {
            // skip
        }
    }

    private boolean test(char expected) {
        if (ch == expected) {
            nextChar();
            return true;
        }
        return false;
    }

    private void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    private int priorityComparing(String first, String second) throws ExpressionParserException {
        if (!PRIORITIES_BINARY_OPERANDS.containsKey(first)) {
            throw new IllegalOperationsParserException(String.format("Unsupported operation: %s", first));
        }
        if (!PRIORITIES_BINARY_OPERANDS.containsKey(second)) {
            throw new IllegalOperationsParserException(String.format("Unsupported operation: %s", second));
        }
        return PRIORITIES_BINARY_OPERANDS.get(first).compareTo(PRIORITIES_BINARY_OPERANDS.get(second));
    }

    private  void previousChar() {
        ch = source.hasPrevious() ? source.previous() : START;
    }
}
