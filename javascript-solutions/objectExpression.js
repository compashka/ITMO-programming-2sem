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

let E = new Const(Math.E);
const diffFunctions = {
    // :NOTE: В операции
    "negate": (diffVar, a) => new Negate(a.diff(diffVar)),
    // :NOTE: Продифференцированные аргументы
    "+": (diffVar, a, b) => new Add(a.diff(diffVar), b.diff(diffVar)),
    "-": (diffVar, a, b) => new Subtract(a.diff(diffVar), b.diff(diffVar)),
    "*": (diffVar, a, b) => new Add(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar))),
    "/": (diffVar, a, b) => new Divide(
        new Subtract(new Multiply(a.diff(diffVar), b), new Multiply(a, b.diff(diffVar))), new Multiply(b, b)),
    // :NOTE: Память
    "pow": (diffVar, a, b) => new Multiply(new Pow(a, new Subtract(b, new Const(1))), new Add(new Multiply(b,
        a.diff(diffVar)), new Multiply(new Multiply(a, new Log(E, a)), b.diff(diffVar)))),
    "log": (diffVar, a, b) => new Divide(new Subtract(new Divide(new Multiply(new Log(E, a), b.diff(diffVar)), b),
        new Divide(new Multiply(new Log(E, b), a.diff(diffVar)), a)), new Multiply(new Log(E, a), new Log(E, a)))
};

function Const(value) {
    this.value = value
}

function Variable(varName) {
    this.varName = varName
}

function Operation(...args) {
    this.args = args
}

function createExpr(object, evaluate, toString, diff) {
    object.prototype.evaluate = evaluate;
    object.prototype.toString = toString;
    object.prototype.diff = diff
}

createExpr(Const,
    function () {
        return +this.value
    },
    function () {
        return this.value.toString()
    },
    () => constNames[0]
);

createExpr(Variable,
    function (...args) {
        // :NOTE: varNames[this.varName]
        return args[varNames[this.varName]]
    },
    function () {
        return this.varName
    },
    function (diffVar) {
        return this.varName === diffVar ? constNames[1] : constNames[0]
    }
);

createExpr(Operation,
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

function makeOperation(operand, operation) {
    const result = function (...args) {
        Operation.call(this, ...args)
    };
    result.prototype = Object.create(Operation.prototype);
    result.prototype.operand = operand;
    result.prototype.operation = operation;
    result.prototype.diffFunc = diffFunctions[operand];
    // :NOTE: .constructor
    result.opLength = operation.length;
    return result
}

const Negate = makeOperation("negate", a => -a);
const Add = makeOperation("+", (a, b) => a + b);
const Subtract = makeOperation("-", (a, b) => a - b);
const Multiply = makeOperation("*", (a, b) => a * b);
const Divide = makeOperation("/", (a, b) => a / b);
const Pow = makeOperation("pow", (a, b) => Math.pow(a, b));
const Log = makeOperation("log", (a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)));
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
