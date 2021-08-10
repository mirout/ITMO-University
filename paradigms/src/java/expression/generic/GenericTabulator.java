package expression.generic;

import expression.exceptions.EvaluateException;
import expression.exceptions.ExpressionParserException;
import expression.operations.CommonExpression;
import expression.expressionParser.ExpressionParser;
import expression.proxies.*;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private final Map<String, Proxy<?>> proxies = Map.of(
            "i", new IntegerProxy(true),
            "u", new IntegerProxy(false),
            "d", new DoubleProxy(),
            "bi", new BigIntegerProxy(),
            "s", new ShortProxy(),
            "l", new LongProxy()
    );

    @Override
    public Object[][][] tabulate(
            final String mode, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws Exception {
        if (proxies.containsKey(mode)) {
            return fill(proxies.get(mode), expression, x1, x2, y1, y2, z1, z2);
        } else {
            throw new IllegalArgumentException(String.format("Unsupported flag: -%s", mode));
        }
    }

    private <T> Object[][][] fill(
            final Proxy<T> proxy, final String expression,
            final int x1, final int x2,
            final int y1, final int y2,
            final int z1, final int z2
    ) throws ExpressionParserException {

        int dx = x2 - x1;
        int dy = y2 - y1;
        int dz = z2 - z1;
        ExpressionParser<T> parser = new ExpressionParser<>(proxy);
        Object[][][] result = new Object[dx + 1][dy + 1][dz + 1];
        CommonExpression<T> parsedExpression = parser.parse(expression);

        for (int i = 0; i <= dx; i++) {
            T x = proxy.value(x1 + i);
            for (int j = 0; j <= dy; j++) {
                T y = proxy.value(y1 + j);
                for (int k = 0; k <= dz;k++) {
                    T z = proxy.value(z1 + k);
                    try {
                        result[i][j][k] = parsedExpression.evaluate(x, y, z);
                    } catch (EvaluateException | ArithmeticException e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            Object[][][] result = (new GenericTabulator()).tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j < result[i].length; j++) {
                    for (int k = 0; k < result[j].length; k++) {
                        System.out.format("x:%d y:%d z:%d - %s%n", i - 2, j - 2, k - 2, result[i][j][k]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
