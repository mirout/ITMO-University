package expression;

import expression.generic.GenericTabulator;
import expression.expressionParser.ExpressionParser;
import expression.proxies.DoubleProxy;

public class SelfTest {
    public static void main(String[] args) throws Exception {
        var res = ((new ExpressionParser<>(new DoubleProxy())).parse("-(-(-    -5 + 16   *x*y) + 1 * z) -(((-11)))"));
        var s = (new GenericTabulator()).tabulate("i", "-(-(-    -5 + 16   *x*y) + 1 * z) -(((-11)))", 0, 0, 0, 0, 0, 0);

        System.out.println(res);
        System.out.println(res.evaluate(0.0,0.0,1.0));
    }
}
