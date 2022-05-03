"use strict";

const constList = {
    0: new Const(0),
    1: new Const(1),
    2: new Const(2),
    'E': new Const(Math.E)
};

const varsList = {
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

function Operation(...args) {
    this.args = args;
}

function createPrototype(object, evaluate, toString, diff, prefix, postfix) {
    object.prototype.evaluate = evaluate;
    object.prototype.toString = toString;
    object.prototype.diff = diff;
    object.prototype.prefix = prefix;
    object.prototype.postfix = postfix;
}

createPrototype(Const,
    function () {
        return +this.value;
    },
    function () {
        return this.value.toString();
    },
    () => constList[0],
    function () {
        return this.value.toString();
    },
    function () {
        return this.value.toString();
    }
);

createPrototype(Variable,
    function (...args) {
        return args[varsList[this.varName]];
    },
    function () {
        return this.varName;
    },
    function (diffVar) {
        return this.varName === diffVar ? constList[1] : constList[0];
    },
    function () {
        return this.varName;
    },
    function () {
        return this.varName;
    }
);

createPrototype(Operation,
    function (...args) {
        return this.operation(...this.args.map(f => f.evaluate(...args)));
    },
    function () {
        return this.args.join(" ") + " " + this.operand;
    },
    function (diffVar) {
        return this.diffFunc(...(this.args.concat(this.args.map(x => x.diff(diffVar)))));
    },
    function () {
        return "(" + this.operand + " " + this.args.map(x => x.prefix()).join(" ") + ")";
    },
    function () {
        return "(" + this.args.map(x => x.postfix()).join(" ") + " " + this.operand + ")";
    }
);

function makeOperation(operand, operation, diffFunc) {
    const result = function (...args) {
        Operation.call(this, ...args)
    };
    result.prototype = Object.create(Operation.prototype);
    result.prototype.constructor = result;
    result.prototype.operand = operand;
    result.prototype.operation = operation;
    result.prototype.diffFunc = diffFunc;
    result.opLength = operation.length;
    result.operand = operand;

    return result
}


// :NOTE: Предварительно дифф.
// (a, b, da, db)
const Negate = makeOperation("negate", a => -a,
    (a, da) => new Negate(da));
const Add = makeOperation("+", (a, b) => a + b,
    (a, b, da, db) => new Add(da, db));
const Subtract = makeOperation("-", (a, b) => a - b,
    (a, b, da, db) => new Subtract(da, db));
const Multiply = makeOperation("*", (a, b) => a * b,
    (a, b, da, db) => new Add(new Multiply(da, b), new Multiply(a, db)));
const Divide = makeOperation("/", (a, b) => a / b,
    (a, b, da, db) => new Divide(
        new Subtract(new Multiply(da, b), new Multiply(a, db)), new Multiply(b, b)));
const Pow = makeOperation("pow", (a, b) => Math.pow(a, b),
    (a, b, da, db) => new Multiply(new Pow(a, new Subtract(b, constList[1])),
        new Add(new Multiply(b, da), new Multiply(new Multiply(a, new Log(constList['E'], a)), db))));
const Log = makeOperation("log", (a, b) => Math.log(Math.abs(b)) / Math.log(Math.abs(a)),
    (a, b, da, db) => new Divide(new Subtract(new Divide(new Multiply(new Log(constList['E'], a), db), b),
        new Divide(new Multiply(new Log(constList['E'], b), da), a)), new Multiply(new Log(constList['E'], a),
        new Log(constList['E'], a))));
const Min3 = makeOperation("min3", (...args) => Math.min(...args));
const Max5 = makeOperation("max5", (...args) => Math.max(...args));
const Mean = makeOperation("mean",
    (...args) => args.length === 0 ? 0 : args.reduce((a, b) => a + b, 0) / args.length,
    (...args) => new Mean(...args.slice(args.length / 2, args.length))
);
const Var = makeOperation("var", (...args) => {
        const mean = args.reduce((a, b) => a + b, 0) / args.length;
        return args.length === 0 ? 0 : args.reduce((a, b) => a + (b - mean) * (b - mean), 0) / args.length;
    },
    (...allArgs) => {
        const args = allArgs.slice(0, allArgs.length / 2);
        const argsDiff = allArgs.slice(allArgs.length / 2, allArgs.length);
        let index = 0;
        return new Multiply(constList[2],
            new Mean(...args.map(f =>
                    new Multiply(
                        new Subtract(f, new Mean(...args)),
                        new Subtract(argsDiff[index++], new Mean(...argsDiff))
                    )
                )
            )
        );
    }
);

const operatorsList = {
    "+": Add,
    "-": Subtract,
    "*": Multiply,
    "/": Divide,
    "negate": Negate,
    "pow": Pow,
    "log": Log,
    "min3": Min3,
    "max5": Max5,
    "mean": Mean,
    "var": Var
};

function parse(expression) {
    let stack = [];
    expression.split(" ").filter(x => x.length > 0).forEach(token => {
        if (token in operatorsList) {
            stack.push(new operatorsList[token](...stack.splice(-operatorsList[token].opLength)))
        } else if (token in varsList) {
            stack.push(new Variable(token))
        } else {
            stack.push(new Const(token))
        }
    });
    return stack.pop()
}

const ParserError = function (position, errorMessage) {
    this.message = `Parser error at position: ${position}: ${errorMessage} `;
};
ParserError.prototype = Object.create(Error.prototype);
ParserError.prototype.name = "ParserError";
ParserError.prototype.constructor = ParserError;

class InvalidOperatorError extends ParserError {
    constructor(position, operand) {
        super(position, `Invalid operator '${operand}'`);
    }
}

class InvalidTokenError extends ParserError {
    constructor(position, token) {
        super(position, `Invalid token '${token}'`);
    }
}

class MissingBracketError extends ParserError {
    constructor(position, token) {
        super(position, `Expected ')', but found '${token === undefined ? 'end of expression' : token}'`);
    }
}

class ArityOperationError extends ParserError {
    constructor(position, foundLength, expectLength, operand) {
        let messageError = (expectLength === undefined) ? `Expected not ${foundLength} arguments for operation`
            : `Expected ${expectLength} arguments for operation '${operand}', but found ${foundLength}`;
        super(position, messageError);
    }
}

function CharSource(input) {
    return {
        position: 0,
        input: input.replace(/[(]/g, ' ( ').replace(/[)]/g, ' ) ').split(" ").filter(x => x.length > 0),
        current: function () {
            return this.input[this.position];
        },
        next: function () {
            return this.input[this.position++];
        },
        isEOF: function () {
            return this.position === this.input.length;
        },
        test: function (token) {
            if (token === this.current()) {
                this.next();
                return true;
            } else {
                return false;
            }
        },
        expect: function (token) {
            if (token === this.current()) {
                this.next();
            } else {
                throw new MissingBracketError(this.position, this.current());
            }
        }
    }
}

function BaseParser(source, mode) {
    return {
        parse: function () {
            let result;
            // :NOTE: Дубль
            if (source.test('(')) {
                result = this.parseExpression();
                source.expect(')');
            } else {
                result = this.parseArguments();
                if (result.length > 1) {
                    source.position = 1;
                } else {
                    result = result[0];
                }
            }
            return source.isEOF() ? result : this.error(InvalidTokenError, source.position, source.current())
        },

        parseExpression: function () {
            // :NOTE: Упростить
            let operator = (mode === 'prefix') ? this.parseOperator() : this.parseArguments();
            let args = (mode === 'prefix') ? this.parseArguments() : this.parseOperator();
            if (mode === 'postfix') [operator, args] = [args, operator];
            return (args.length === operator.opLength || operator.opLength === 0) ? new operator(...args) :
                this.error(ArityOperationError, source.position, args.length, operator.opLength, operator.operand)
        },
        parseOperator: function () {
            return (source.current() in operatorsList) ? operatorsList[source.next()] :
                this.error(InvalidOperatorError, source.position, source.current())
        },
        parseArguments: function () {
            let args = [];
            while (!source.isEOF()) {
                if (source.current() in varsList) {
                    args.push(new Variable(source.next()));
                } else if (isFinite(source.current())) {
                    args.push(new Const(source.next()));
                } else if (source.test('(')) {
                    args.push(this.parseExpression());
                    source.expect(')');
                } else {
                    break;
                }
            }
            return args.length === 0 ? this.error(ArityOperationError, source.position, 0) : args;
        },
        error: (nameError, ...args) => {
            throw new nameError(...args);
        }
    }
}

// :NOTE: 'prefix' -> const
const parsePrefix = input => BaseParser(CharSource(input), 'prefix').parse();
const parsePostfix = input => BaseParser(CharSource(input), 'postfix').parse();
