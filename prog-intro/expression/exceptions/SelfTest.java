package expression.exceptions;

import expression.Const;

public class SelfTest {
    public static void main(String[] args) throws ExpressionParserException {
        var res = ((new ExpressionParser()).parse("sqrt(x * y * z)"));
        System.out.println(res);
        System.out.println(res.evaluate(0,0,0));
        System.out.println(new CheckedMultiply(new Const(-1), new Const(Integer.MIN_VALUE)).evaluate(0));
    }
}
