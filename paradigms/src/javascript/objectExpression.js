"use strict"

function expressionFactory(init, diff, evaluate, toString, prefix, postfix) {
    init.prototype.diff = diff;
    init.prototype.evaluate = evaluate;
    init.prototype.toString = toString;
    init.prototype.prefix = prefix === undefined ? toString : prefix;
    init.prototype.postfix = postfix === undefined ? toString : postfix;
    return init;
}

const AbstractOperation = expressionFactory(
    function (...operands) {
        this.operands = operands;
    },
    function (variable) {
        return this.derivative(...this.operands)(...this.operands.map(x => x.diff(variable)));
    },
    function (...vars) {
        return this.makeOp(...this.operands.map(x => x.evaluate(...vars)));
    },
    function () {
        return this.operands.map(x => x.toString()).join(" ") + " " + this.sign;
    },
    function () {
        return "(" + this.sign + " " + this.operands.map(x => x.prefix()).join(" ") + ")";
    },
    function () {
        return "(" + this.operands.map(x => x.postfix()).join(" ") + " " + this.sign + ")";
    }
)

const operations = new Map()

function newOperation(sign, makeOp, derivative) {
    const Operation = function (...operands) {
        AbstractOperation.call(this, ...operands);
    }
    Operation.prototype = Object.create(AbstractOperation.prototype);
    Operation.prototype.sign = sign;
    Operation.prototype.makeOp = makeOp;
    Operation.prototype.derivative = derivative;

    operations.set(sign, Operation);

    return Operation;
}

const Subtract = newOperation(
    "-",
    (x, y) => x - y,
    (x, y) => (dx, dy) => new Subtract(dx, dy),
)

const Add = newOperation(
    "+",
    (x, y) => x + y,
    (x, y) => (dx, dy) => new Add(dx, dy),
)

const Multiply = newOperation(
    "*",
    (x, y) => x * y,
    (x, y) => (dx, dy) => new Add(new Multiply(dx, y), new Multiply(x, dy)),
)

const Divide = newOperation(
    "/",
    (x, y) => x / y,
    (x, y) => (dx, dy) => new Divide(
        new Subtract(
            new Multiply(dx, y),
            new Multiply(x, dy),
        ),
        new Multiply(y, y)
    ),
)

const Cube = newOperation(
    "cube",
    x => x ** 3,
    x => dx => new Multiply(
        new Multiply(
            Const.THREE,
            dx
        ),
        new Multiply(x, x)
    ),
)

const Cbrt = newOperation(
    "cbrt",
    x => Math.cbrt(x),
    x => dx => new Divide(
        dx,
        new Multiply(
            Const.THREE,
            new Multiply(
                new Cbrt(x),
                new Cbrt(x),
            )
        ),
    )
)

const Sumsq = newOperation(
    "sumsq",
    (...args) => args.reduce((acc, val) => acc + val ** 2, 0),
    (...xs) => (...dxs) => {
        let val = []
        for (let i = 0; i < xs.length; i++) {
            val.push(new Multiply(xs[i], dxs[i]))
        }

        return new Multiply(
            val.reduce((acc, current) => new Add(acc, current), Const.ZERO),
            new Const(2)
        )
    }
)

const Length = newOperation(
    "length",
    (...args) => Math.sqrt(args.reduce((acc, val) => acc + val ** 2, 0)),
    (...xs) => (...dxs) => {
        if (xs.length === 0) {
            return Const.ZERO
        }
        let val = []
        for (let i = 0; i < xs.length; i++) {
            val.push(new Multiply(xs[i], dxs[i]))
        }

        return new Divide(
            val.reduce((acc, current) => new Add(acc, current), Const.ZERO),
            new Length(...xs)
        )
    }
)

const Negate = newOperation(
    "negate",
    x => -x,
    x => dx => new Negate(dx),
)

const Const = expressionFactory(
    function (value) {
        this.value = value
    },
    _ => Const.ZERO,
    function () {
        return this.value;
    },
    function () {
        return this.value.toString();
    },
)

Const.ZERO = new Const(0);
Const.ONE = new Const(1);
Const.THREE = new Const(3);

const vars = {
    "x": 0,
    "y": 1,
    "z": 2,
};

const Variable = expressionFactory(
    function (name) {
        this.name = name
        this.index = vars[name]
    },
    function (variable) {
        return variable === this.name ? Const.ONE : Const.ZERO;
    },
    function (...values) {
        return values[vars[this.name]];
    },
    function () {
        return this.name;
    },
)

const foldParse = (acc, current) => {
    if (operations.has(current)) {
        const operation = operations.get(current);
        acc.push(new operation(...acc.splice(-operation.prototype.makeOp.length)));
    } else if (current in vars) {
        acc.push(new Variable(current));
    } else if (!isNaN(current)) {
        acc.push(new Const(+current));
    }
    return acc;
}

const parse = expression => expression.trim().split(/\s+/).reduce(foldParse, []).pop();

function errorFactory(Message, type) {
    const Exception = function (...args) {
        this.message = Message(...args);
        this.name = type;
    }
    Exception.prototype = new Error();
    return Exception;
}

const UnexpectedTokenError = errorFactory(
    (token, pos) => "Unexpected token: \"" + token + "\", at position: " + pos,
    "UnexpectedTokenError",
)

const UnexpectedEndOfString = errorFactory(
    pos => "Unexpected end of string: " + pos,
    "UnexpectedEndOfString",
)

const IllegalCountOfArguments = errorFactory(
    (found, expected, pos) => "Illegal count of arguments at position" + pos + " found: " + found + ", expected" + expected,
    "IllegalCountOfArguments",
)

const IllegalArgument = errorFactory(
    (name, pos) => "Illegal argument: \"" + name + "\" at " + pos,
    "IllegalArgument",
)

function tokenizerFactory(isBrackets, isWhitespaces) {
    const Tokenizer = function (source) {
        this.source = source;
        this.pos = 0;
    }
    Tokenizer.prototype.skipWhitespaces = function () {
        for (;this.pos < this.source.length && isWhitespaces(this.source[this.pos]); this.pos++) {}
    }
    Tokenizer.prototype.nextToken = function () {
        this.skipWhitespaces()
        if (isBrackets(this.source[this.pos])) {
            return this.source[this.pos++];
        }
        let start = this.pos;
        while (this.pos < this.source.length && !isWhitespaces(this.source[this.pos]) && !isBrackets(this.source[this.pos])) {
            this.pos++;
        }
        return this.source.substring(start, this.pos)
    }
    Tokenizer.prototype.hasNextToken = function () {
        this.skipWhitespaces()
        return this.pos < this.source.length;
    }
    return Tokenizer;
}
const Tokenizer = tokenizerFactory(x => ["(", ")"].indexOf(x) !== -1, x => x === " ")
function parserFactory(getOperation) {
    const Parser = function (tokenizer) {
        this.tokenizer = tokenizer
        this.token = this.tokenizer.nextToken()
    }
    Parser.prototype.parse = function () {
        const result = this.parseToken()
        if (this.tokenizer.hasNextToken()) {
            throw new UnexpectedTokenError(this.tokenizer.nextToken(), this.tokenizer.pos)
        }
        return result
    }
    Parser.prototype.parseExpression = function () {
        let tokens = []
        this.token = this.tokenizer.nextToken()
        while (this.tokenizer.hasNextToken() && this.token !== ")") {
            tokens.push(this.parseToken())
            this.token = this.tokenizer.nextToken()
        }
        let [Operation, args] = getOperation(tokens);
        if (this.token !== ")") {
            throw new UnexpectedEndOfString(this.tokenizer.pos - this.token.length)
        } else if (args.find(value => !("evaluate" in value)) !== undefined) {
            throw new IllegalArgument("find operation in argument", this.tokenizer.pos - this.token.length);
        } else if (Operation === undefined || "evaluate" in Operation) {
            throw new IllegalArgument("expected operation", this.tokenizer.pos - this.token.length);
        } else if (Operation.prototype.makeOp.length !== 0 && args.length !== Operation.prototype.makeOp.length) {
            throw new IllegalCountOfArguments(Operation.prototype.makeOp.length, args.length, this.tokenizer.pos - this.token.length);
        }
        return new Operation(...args)
    }
    Parser.prototype.parseToken = function () {
        if (this.token === "(") {
            return this.parseExpression()
        } else if (this.token !== "" && !isNaN(this.token)) {
            return new Const(+this.token)
        } else if (this.token in vars) {
            return new Variable(this.token);
        } else if (operations.has(this.token)) {
            return operations.get(this.token)
        }
        throw new UnexpectedTokenError(this.token, this.tokenizer.pos - this.token.length);
    }
    return Parser;
}
const prefixParser = parserFactory(x => [x.shift(), x])
const postfixParser = parserFactory(x => [x.pop(), x])
const parsePrefix = x => new prefixParser(new Tokenizer(x)).parse()
const parsePostfix = x => new postfixParser(new Tokenizer(x)).parse()
