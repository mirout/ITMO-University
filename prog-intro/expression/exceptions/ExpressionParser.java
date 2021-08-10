package expression.exceptions;

import expression.*;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Set;

public class ExpressionParser implements Parser {

    private final static Map<String, PriorityEnum> PRIORITIES_BINARY_OPERANDS = Map.of(
                 "+", PriorityEnum.ADD,
                 "-", PriorityEnum.ADD,
                 "*", PriorityEnum.MULTIPLY,
                 "/", PriorityEnum.MULTIPLY,
                 "&", PriorityEnum.AND,
                 "|", PriorityEnum.OR,
                 "^", PriorityEnum.XOR,
                 "max", PriorityEnum.MAX,
                 "min", PriorityEnum.MAX
    );

    public static final char END = '\0';
    public static final char START = 0xffff;
    private CharSource source;
    private char ch = START;
    private final ArrayDeque<CommonExpression> leftChild = new ArrayDeque<>();
    private boolean isNegate = true;
    private boolean isPreviousOperation = true;
    private String nextSubstring = "";
    private final Set<String> setOfVariables = Set.of("x", "y", "z");
    private final Set<String> setOfUnaryOperands = Set.of("count", "abs", "sqrt", "~", "-");

    @Override
    public CommonExpression parse(String expression) throws ExpressionParserException {
        return parse(new StringSource(expression + " "));
    }

    private CommonExpression parse(CharSource stringSource) throws ExpressionParserException {
        isNegate = true;
        isPreviousOperation = true;
        this.source = stringSource;
        nextChar();
        return parseExpression();
    }

    private CommonExpression parseExpression() throws ExpressionParserException {
        skipWhitespaces();
        CommonExpression result = parseElement(END);
        skipWhitespaces();
        return result;
    }

    private CommonExpression parseElement(char end) throws ExpressionParserException {
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

    private CommonExpression parseValue(char end) throws ExpressionParserException {

        if (isDigit() || ch == '-' && isNegate) {
            isIllegalArgument();
            return parseDigit(end);
        } else if (test('~')) {
            return parseNot(end);
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
                return new Variable(nextSubstring);
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
        nextSubstring = sb.toString();
        return sb.toString();
    }

    private CommonExpression parseMinus(char end) throws ExpressionParserException {
        skipWhitespaces();
        return new Negate(parseValue(end));
    }

    private CommonExpression parseNot(char end) throws ExpressionParserException {
        skipWhitespaces();
        return new Not(parseValue(end));
    }

    private CommonExpression parseOperation(char end, String now) throws ExpressionParserException {
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

    private CommonExpression getBinaryOperation(CommonExpression firstChild, CommonExpression secondChild, String operand) throws IllegalArgumentParserException {
        switch (operand) {
            case "+" : return new CheckedAdd(firstChild, secondChild);
            case "-" : return new CheckedSubtract(firstChild, secondChild);
            case "*" : return new CheckedMultiply(firstChild, secondChild);
            case "/" : return new CheckedDivide(firstChild, secondChild);
            case "&" : return new And(firstChild, secondChild);
            case "^" : return new Xor(firstChild, secondChild);
            case "|" : return new Or(firstChild, secondChild);
            case "max" : return new Max(firstChild, secondChild);
            case "min" : return new Min(firstChild, secondChild);
            default : throw new IllegalArgumentParserException("Parser don't support this operation");
        }
    }

    private CommonExpression getUnaryOperation(CommonExpression child, String operand) throws IllegalArgumentParserException {
        switch (operand) {
            case "count" : return new Count(child);
            case "abs" : return new Abs(child);
            case "sqrt" : return new Sqrt(child);
            case "~" : return new Not(child);
            case "-" : return new Negate(child);
            default : throw new IllegalArgumentParserException("Parser don't support this operation");
        }
    }

    private CommonExpression parseDigit(char end) throws ExpressionParserException {

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
            int result = Integer.parseInt(sb.toString());
            return new Const(result);
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
