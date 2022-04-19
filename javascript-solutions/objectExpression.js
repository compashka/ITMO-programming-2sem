"use strict";

const constNames = {
    0: new Const(0),
    1: new Const(1)
};

const varNames = {
    "x": 0,
    "y": 1,
    "z": 2
};

function Const(value) {
    this.value = value;
}

function Variable(varName) {
    this.varName = varName;
}

function Operation(operand, operation, diffFunc, ...args) {
    this.args = args;
    this.operand = operand;
    this.operation = operation;
    this.diffFunc = diffFunc;
}

function createPrototype(object, evaluate, toString, diff) {
    object.prototype.evaluate = evaluate;
    object.prototype.toString = toString;
    object.prototype.diff = diff
}

createPrototype(Const,
    function () {
        return +this.value
    },
    function () {
        return this.value.toString()
    },
    () => constNames[0]
);

createPrototype(Variable,
    function (...args) {
        return args[varNames[this.varName]]
    },
    function () {
        return this.varName
    },
    function (diffVar) {
        return this.varName === diffVar ? constNames[1] : constNames[0]
    }
);

createPrototype(Operation,
    function (...args) {
        return this.operation(...this.args.map(f => f.evaluate(...args)))
    },
    function () {
        return this.args.join(" ") + " " + this.operand
    },
    function (diffVar) {
        return this.diffFunc(diffVar, ...this.args)
    }
);

function makeOperation(operand, operation, diffFunc) {
    const result = function (...args) {
        Operation.call(this, operand, operation, diffFunc, ...args)
    };
    result.prototype = Object.create(Operation.prototype);
    result.opLength = operation.length;

    return result
}

let E = new Const(Math.E);
let One = new Const(1);

const Negate = makeOperation("negate", a => -a,
    (diffVar, a) => new Negate(a.diff(diffVar)));
const Add = makeOperation("+", (a, b) => a + b,
    (diffVar, a, b) => new Add(a.diff(diffVar), b.diff(diffVar)));
const Subtract = makeOperation("-", (a, b) => a - b,
    (diffVar, a, b) => new Subtract(a.diff(diffVar), b.diff(diffVar)));
const Multiply = makeOperation("*", (a, b) => a * b,
    (diffVar, a, b) => new Add(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar))));
const Divide = makeOperation("/", (a, b) => a / b,
    (diffVar, a, b) => new Divide(
        new Subtract(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar))), new Multiply(b, b)));
const Pow = makeOperation("pow", (a, b) => Math.pow(a, b),
    (diffVar, a, b) => new Multiply(new Pow(a, new Subtract(b, One)),
        new Add(new Multiply(b, a.diff(diffVar)), new Multiply(new Multiply(a, new Log(E, a)), b.diff(diffVar)))));
const Log = makeOperation("log", (a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)),
    (diffVar, a, b) => new Divide(new Subtract(new Divide(new Multiply(new Log(E, a), b.diff(diffVar)), b),
        new Divide(new Multiply(new Log(E, b), a.diff(diffVar)), a)), new Multiply(new Log(E, a), new Log(E, a))));
const Min3 = makeOperation("min3", (a, b, c) => Math.min(a, b, c));
const Max5 = makeOperation("max5", (a, b, c, d, e) => Math.max(a, b, c, d, e));

const operationNames = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "pow": Pow,
    "log": Log,
    "min3": Min3,
    "max5": Max5
};

function parse(expression) {
    let stack = [];
    expression.split(" ").filter(x => x.length > 0).forEach(token => {
        if (token in operationNames) {
            stack.push(new operationNames[token](...stack.splice(-operationNames[token].opLength)))
        } else if (token in varNames) {
            stack.push(new Variable(token))
        } else {
            stack.push(new Const(token))
        }
    });
    return stack.pop()
}
