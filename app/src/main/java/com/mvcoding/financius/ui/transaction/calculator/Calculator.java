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

import javax.inject.Inject;

import bsh.Interpreter;

public class Calculator {
    private final Interpreter interpreter;
    private final StringBuilder expression;

    @Inject public Calculator() {
        interpreter = new Interpreter();
        expression = new StringBuilder();
    }

    public void number0() {
        expression.append("0");
    }

    public void number1() {
        expression.append("1");
    }

    public void number2() {
        expression.append("2");
    }

    public void number3() {
        expression.append("3");
    }

    public void number4() {
        expression.append("4");
    }

    public void number5() {
        expression.append("5");
    }

    public void number6() {
        expression.append("6");
    }

    public void number7() {
        expression.append("7");
    }

    public void number8() {
        expression.append("8");
    }

    public void number9() {
        expression.append("9");
    }

    public void decimal() {
        expression.append(".");
    }

    public void divide() {
        expression.append("/");
    }

    public void multiply() {
        if (isEmpty()) {
            return;
        }

        if (isLastOperator() && !isLengthOne()) {
            replaceLast("*");
        } else {
            expression.append("*");
        }
    }

    public void subtract() {
        if (isLastOperator()) {
            replaceLast("-");
        } else {
            expression.append("-");
        }
    }

    public void add() {
        expression.append("+");
    }

    public String getExpression() {
        return expression.toString();
    }

    public void clear() {
        expression.delete(0, expression.length());
    }

    private void replaceLast(String replaceWith) {
        expression.replace(expression.length() - 1, expression.length(), replaceWith);
    }

    private boolean isEmpty() {
        return expression.length() == 0;
    }

    private boolean isLengthOne() {
        return expression.length() == 0;
    }

    private boolean isLastOperator() {
        return !isEmpty() && expression.substring(expression.length() - 1).matches("[+\\-*/]");
    }

    private boolean isLastDecimal() {
        return !isEmpty() && expression.substring(expression.length() - 1).matches("\\.");
    }
}
