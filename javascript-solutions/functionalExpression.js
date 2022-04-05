"use strict";

const variable = varName => (...args) => args[varNames[varName]];
const cnst = x => () => x;

const operation = oper => (...func) => (...args) => oper(...func.map(x => x(...args)));
const add = operation((x, y) => x + y);
const subtract = operation((x, y) => x - y);
const multiply = operation((x, y) => x * y);
const divide = operation((x, y) => x / y);
const negate = operation(x => -x);
const iff = operation((x, y, z) => (x >= 0) ? y : z);
const abs = operation(Math.abs);

const operationNames = {
    // :NOTE: F.length
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1],
    "iff": [iff, 3],
    "abs": [abs, 1],
};

const pi = cnst(Math.PI);
const e = cnst(Math.E);
const constNames = {
    "pi": pi,
    "e": e
};

const varNames = {
    "x": 0,
    "y": 1,
    "z": 2
};

function parse(expression) {
    let stack = [];
    expression.split(" ").filter(x => x.length > 0).forEach(token => {
        if (token in operationNames) {
            stack.push(operationNames[token][0](...stack.splice(-operationNames[token][1])));
        } else if (token in constNames) {
            stack.push(constNames[token]);
        } else if (token in varNames) {
            stack.push(variable(token));
        } else {
            stack.push(cnst(parseInt(token)));
        }
    });
    return stack.pop();
}
