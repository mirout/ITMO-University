'use strict'

const operation = apply => (...operations) => (...args) => apply(...operations.map(op => op(...args)));

const add = operation((x, y) => x + y);
const subtract = operation((x, y) => x - y);
const multiply = operation((x, y) => x * y);
const divide = operation((x, y) => x / y);
const negate = operation(x => -x);

const max3 = operation((x, y, z) => Math.max(x, y, z))
const min5 = operation((x1, x2, x3, x4, x5) => Math.min(x1, x2, x3, x4, x5))

const cnst = x => _ => x;

const one = cnst(1);
const two = cnst(2);

const variables = {
    "x": 0,
    "y": 1,
    "z": 2,
}

const variable = name => (...args) => args[variables[name]];

const operations = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1],
    "min5": [min5, 5],
    "max3": [max3, 3],
}

const cnsts = {
    "one": one,
    "two": two,
}

const foldParse = (acc, current) => {
    if (current in operations) {
        const [operation, arity] = operations[current];
        const variables = Array.from(
            {length: arity},
            _ => acc.pop(),
        ).reverse();
        acc.push(operation(...variables));
    } else if (current in cnsts) {
        acc.push(cnsts[current])
    } else if (current in variables) {
        acc.push(variable(current));
    } else if (!isNaN(current)) {
        acc.push(cnst(+current));
    }
    return acc;
}
const parse = expression => expression.trim().split(/\s+/).reduce(foldParse, []).pop();

const expression = parse("x x * 2 x * - 1 +");
const values = Array.from(
    {length: 10},
    (_, i) => expression(i),
);
values.forEach((val, i) => console.log(i + " : " + val));