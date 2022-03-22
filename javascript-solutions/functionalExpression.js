"use strict";

const variable = letter => {
    return (x, y, z) => {
        switch (letter) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
        }
    };
}

const cnst = x => {
    return () => x;
}

const add = (l, r) => {
    return (x, y, z) => (l(x, y, z) + r(x, y, z));
}

const subtract = (l, r) => {
    return (x, y, z) => (l(x, y, z) - r(x, y, z));
}

const multiply = (l, r) => {
    return (x, y, z) => (l(x, y, z) * r(x, y, z));
}

const divide = (l, r) => {
    return (x, y, z) => (l(x, y, z) / r(x, y, z));
}

const negate = expr => {
    return (x, y, z) => -(expr(x, y, z));
}

const operationNames = {
    "+": [add, 2],
    "-": [subtract, 2],
    "*": [multiply, 2],
    "/": [divide, 2],
    "negate": [negate, 1]
};

function parse(expression) {
    let stack = [];
    for (let i = 0; i < expression.length; i++) {
        if (expression[i] === " ") {
            continue;
        }
        let start = i;
        while (i < expression.length && expression[i] !== " ") {
            i++;
        }
        let token = expression.substring(start, i);
        if (token in operationNames) {
            stack.push(operationNames[token][0](...stack.splice(-operationNames[token][1])));
        } else if (token === 'x' || token === 'y' || token === 'z') {
            stack.push(variable(token));
        } else {
            stack.push(cnst(parseInt(token)));
        }
    }
    return stack.pop();
}
