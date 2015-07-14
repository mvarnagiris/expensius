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

package com.mvcoding.financius.ui.calculator;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.math.BigDecimal;

import javax.inject.Inject;

class Calculator {
    private final Interpreter interpreter;
    private final StringBuilder expression;

    @Inject public Calculator(@NonNull Interpreter interpreter) {
        this.interpreter = interpreter;
        expression = new StringBuilder();
    }

    public BigDecimal calculate() {
        return interpreter.evaluate(expression.toString());
    }

    public void setNumber(@Nullable BigDecimal number) {
        clear();
        if (number != null) {
            expression.append(number.toPlainString());
        }
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
        if (lastNumberHasDecimal() && !isLastOperator()) {
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

    @NonNull public String getExpression() {
        return expression.toString();
    }

    public void delete() {
        if (expression.length() == 0) {
            return;
        }

        expression.delete(expression.length() - 1, expression.length());
    }

    public void clear() {
        expression.delete(0, expression.length());
    }

    public boolean isEmptyOrSingleNumber() {
        return interpreter.isEmptyOrSingleNumber(expression.toString());
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
