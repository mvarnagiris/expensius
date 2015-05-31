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

import com.mvcoding.financius.BaseTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CalculatorTest extends BaseTest {
    private Calculator calculator;

    @Override public void setUp() {
        super.setUp();
        calculator = new Calculator();
    }

    @Test public void number0To9_addsNumber_whenCurrentExpressionIsEmpty() {
        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number1();
        assertThat(calculator.getExpression()).isEqualTo("1");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number2();
        assertThat(calculator.getExpression()).isEqualTo("2");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number3();
        assertThat(calculator.getExpression()).isEqualTo("3");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number4();
        assertThat(calculator.getExpression()).isEqualTo("4");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number5();
        assertThat(calculator.getExpression()).isEqualTo("5");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number6();
        assertThat(calculator.getExpression()).isEqualTo("6");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number7();
        assertThat(calculator.getExpression()).isEqualTo("7");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number8();
        assertThat(calculator.getExpression()).isEqualTo("8");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.number9();
        assertThat(calculator.getExpression()).isEqualTo("9");
    }

    @Test public void number0To9_addsNumber_whenCurrentExpressionEndsWithNumber() {
        fail("Not implemented");
    }

    @Test public void number0To9_addsNumber_whenCurrentExpressionEndsWithDecimal() {
        fail("Not implemented");
    }

    @Test public void number0To9_addsNumber_whenCurrentExpressionEndsWithOperator() {
        fail("Not implemented");
    }

    @Test public void divide_isIgnored_whenCurrentExpressionIsEmpty() {
        fail("Not implemented");
    }

    @Test public void divide_replacesPreviousOperator_whenCurrentExpressionEndsWithOperator() {
        fail("Not implemented");
    }

    @Test public void divide_addsDivideOperator_whenCurrentExpressionEndsWithNumber() {
        fail("Not implemented");
    }

    @Test public void divide_addsDivideOperator_whenCurrentExpressionEndsWithDecimal() {
        fail("Not implemented");
    }

    @Test public void multiply_isIgnored_whenCurrentExpressionIsEmpty() {
        fail("Not implemented");
    }

    @Test public void subtract_addsSubtractOperator_whenCurrentExpressionIsEmpty() {
        fail("Not implemented");
    }

    @Test public void add_isIgnored_whenCurrentExpressionIsEmpty() {
        fail("Not implemented");
    }

    @Test public void clear_clearsTheExpression_whenExpressionIsNotEmpty() {
        fail("Not implemented");
    }
}
