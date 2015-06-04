/*
 * Copyright (C) 2015 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.financius.ui.transaction.calculator;

import java.math.BigDecimal;

import javax.inject.Inject;

import bsh.EvalError;
import bsh.Interpreter;

class Calculator {
    private final Interpreter interpreter;
    private final StringBuilder expression;

    @Inject public Calculator() {
        interpreter = new Interpreter();
        expression = new StringBuilder();
    }

    public BigDecimal calculate() {
        try {
            return new BigDecimal(interpreter.eval(expression.toString()).toString());
        } catch (EvalError evalError) {
            evalError.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    public void digit0() {
        expression.append("0");
    }

    public void digit1() {
        expression.append("1");
    }

    public void digit2() {
        expression.append("2");
    }

    public void digit3() {
        expression.append("3");
    }

    public void digit4() {
        expression.append("4");
    }

    public void digit5() {
        expression.append("5");
    }

    public void digit6() {
        expression.append("6");
    }

    public void digit7() {
        expression.append("7");
    }

    public void digit8() {
        expression.append("8");
    }

    public void digit9() {
        expression.append("9");
    }

    public void decimal() {
        if (lastNumberHasDecimal()) {
            return;
        }

        expression.append(".");
    }

    public void divide() {
        operator("/");
    }

    public void multiply() {
        operator("*");
    }

    public void subtract() {
        if (isLastOperator()) {
            replaceLast("-");
        } else {
            expression.append("-");
        }
    }

    public void add() {
        operator("+");
    }

    public String getExpression() {
        return expression.toString();
    }

    public void clear() {
        expression.delete(0, expression.length());
    }

    private void operator(String operator) {
        if (isEmpty() || (isLengthOne() && isLastOperator())) {
            return;
        }

        if (isLastOperator()) {
            replaceLast(operator);
        } else {
            expression.append(operator);
        }
    }

    private void replaceLast(String replaceWith) {
        expression.replace(expression.length() - 1, expression.length(), replaceWith);
    }

    private boolean isEmpty() {
        return expression.length() == 0;
    }

    private boolean isLengthOne() {
        return expression.length() == 1;
    }

    private boolean isLastOperator() {
        return !isEmpty() && expression.substring(expression.length() - 1).matches("[+\\-*/]");
    }

    private boolean lastNumberHasDecimal() {
        if (isEmpty()) {
            return false;
        }

        final String[] split = expression.toString().split("[+\\-*/]");
        final String lastNumber = split[split.length - 1];

        return lastNumber.contains(".");
    }
}
