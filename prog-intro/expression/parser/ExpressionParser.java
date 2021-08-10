package expression.parser;

import expression.*;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Set;

public class ExpressionParser implements Parser {

    private final static Map<String, PriorityEnum> PRIORITIES = Map.of(
            "+", PriorityEnum.ADD,
            "-", PriorityEnum.ADD,
            "*", PriorityEnum.MULTIPLY,
            "/", PriorityEnum.MULTIPLY,
            "&", PriorityEnum.AND,
            "|", PriorityEnum.OR,
            "^", PriorityEnum.XOR
    );

    public static final char END = '\0';
    private CharSource source;
    private char ch = 0xffff;
    private final ArrayDeque<CommonExpression> leftChild = new ArrayDeque<>();
    private boolean isNegate = true;
    private boolean isPreviousOperation = true;
    private final Set<String> stringOfBinaryOperation = Set.of("+", "-", "*", "/", "^", "&", "|");
    private final Set<String> setOfVariables = Set.of("x", "y", "z");
    private String nextString;

    @Override
    public CommonExpression parse(String expression) {
        return parse(new StringSource(expression));
    }

    private CommonExpression parse(CharSource stringSource) {
        isNegate = true;
        isPreviousOperation = true;
        this.source = stringSource;
        nextChar();
        return parseExpression();
    }

    private CommonExpression parseExpression() {
        skipWhitespaces();
        CommonExpression result = parseElement(END);
        skipWhitespaces();
        return result;
    }

    private CommonExpression parseElement(char end) {
        isNegate = true;
        while (ch != end) {
            skipWhitespaces();
            leftChild.push(parseValue(end));
            skipWhitespaces();
        }
        test(end);
        if (leftChild.size() == 0) {
            throw new IllegalArgumentException("Expression don't contains anything");
        }
        return leftChild.pop();
    }

    private CommonExpression parseValue(char end) {
        if (isDigit() || ch == '-' && isNegate) {
            isIllegalArgument();
            return parseDigit(end);
        } else if (isVariable()) {
            isIllegalArgument();
            isPreviousOperation = false;
            isNegate = false;
            return new Variable(nextString);
        } else if (checked("count")) {
            return parseCount(end);
        } else if (test('~')) {
            return parseNot(end);
        } else if (testBinaryOperation()) {
            return parseOperation(end);
        } else if (test('(')) {
            return parseElement(')');
        } else if (test(END)) {
            throw new IllegalArgumentException("Unexpected end of expression");
        }
        throw new IllegalArgumentException(String.format("Unexpected character: '%c' at position %d", ch, source.getPosition()));
    }

    private boolean isVariable() {
        skipWhitespaces();
        readNextString();
        if (setOfVariables.contains(nextString)) {
            return true;
        }
        rollBack(nextString.length());
        return false;
    }

    private void readNextString() {
        StringBuilder sb = new StringBuilder();
        while (Character.isAlphabetic(ch)) {
            sb.append(ch);
            nextChar();
        }
        nextString = sb.toString();
    }

    private boolean testBinaryOperation() {
        return stringOfBinaryOperation.contains(Character.toString(ch));
    }

    private CommonExpression parseMinus(char end) {
        skipWhitespaces();
        return new Negate(parseValue(end));
    }

    private CommonExpression parseNot(char end) {
        skipWhitespaces();
        return new Not(parseValue(end));
    }

    private CommonExpression parseCount(char end) {
        skipWhitespaces();
        return new Count(parseValue(end));
    }

    private CommonExpression parseOperation(char end) {
        if (isPreviousOperation) {
            throw new IllegalArgumentException(String.format("Unexpected new operation at %d", source.getPosition()));
        }

        isNegate = true;
        isPreviousOperation = true;

        String operand = Character.toString(ch);
        nextChar();

        skipWhitespaces();

        var firstChild = leftChild.pop();
        var secondChild = parseValue(end);

        skipWhitespaces();

        if (testBinaryOperation()) {
            if (priorityComparing(operand, Character.toString(ch)) >= 0) {
                return getBinaryOperation(firstChild, secondChild, operand);
            } else {
                leftChild.push(secondChild);
                while (testBinaryOperation() && priorityComparing(operand, Character.toString(ch)) < 0) {
                    leftChild.push(parseOperation(end));
                }
                return getBinaryOperation(firstChild, leftChild.pop(), operand);
            }
        } else {
            return getBinaryOperation(firstChild, secondChild, operand);
        }
    }


    private CommonExpression getBinaryOperation(CommonExpression firstChild, CommonExpression secondChild, String operand) {
        switch (operand) {
            case "+" : return new Add(firstChild, secondChild);
            case "-" : return new Subtract(firstChild, secondChild);
            case "*" : return new Multiply(firstChild, secondChild);
            case "/" : return new Divide(firstChild, secondChild);
            case "&" : return new And(firstChild, secondChild);
            case "^" : return new Xor(firstChild, secondChild);
            case "|" : return new Or(firstChild, secondChild);
            default : throw new IllegalArgumentException("Parser don't support this operation");
        }
    }

    private CommonExpression parseDigit(char end) {

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
            throw new IllegalArgumentException(String.format("Illegal const format at %d", source.getPosition()), e);
        }
    }

    private void isIllegalArgument() {
        if (!isPreviousOperation) {
            throw new IllegalArgumentException(String.format("Unexpected new argument at %d", source.getPosition()));
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

    private boolean checked(String expected) {
        skipWhitespaces();
        for (int i = 0; i < expected.length(); i++) {
            if (ch != expected.charAt(i)) {
                rollBack(i);
                return false;
            }
            nextChar();
        }
        return true;
    }

    private void nextChar() {
        ch = source.hasNext() ? source.next() : END;
    }

    private void rollBack(int number) {
        for (int i = 0; i < number; i++) {
            previousChar();
        }
    }

    private int priorityComparing(String first, String second) {
        if (!PRIORITIES.containsKey(first)) {
            throw new IllegalArgumentException(String.format("Unsupported operation: %s", first));
        }
        if (!PRIORITIES.containsKey(second)) {
            throw new IllegalArgumentException(String.format("Unsupported operation: %s", second));
        }
        return PRIORITIES.get(first).compareTo(PRIORITIES.get(second));
    }

    private  void previousChar() {
        ch = source.hasPrevious() ? source.previous() : END;
    }
}
