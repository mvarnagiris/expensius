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
    private String expression;

    @Inject public Calculator() {
        interpreter = new Interpreter();
        expression = "";
    }

    public void number0() {
    }

    public void number1() {
    }

    public void number2() {
    }

    public void number3() {
    }

    public void number4() {
    }

    public void number5() {
    }

    public void number6() {
    }

    public void number7() {
    }

    public void number8() {
    }

    public void number9() {
    }

    public void decimal() {
    }

    public void divide() {
    }

    public void multiply() {
    }

    public void subtract() {
    }

    public void add() {
    }

    public String getExpression() {
        return expression;
    }

    public void clear() {
        expression = "";
    }
}
