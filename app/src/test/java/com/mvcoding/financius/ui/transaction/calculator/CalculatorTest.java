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
        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("00");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number1();
        assertThat(calculator.getExpression()).isEqualTo("01");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number2();
        assertThat(calculator.getExpression()).isEqualTo("02");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number3();
        assertThat(calculator.getExpression()).isEqualTo("03");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number4();
        assertThat(calculator.getExpression()).isEqualTo("04");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number5();
        assertThat(calculator.getExpression()).isEqualTo("05");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number6();
        assertThat(calculator.getExpression()).isEqualTo("06");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number7();
        assertThat(calculator.getExpression()).isEqualTo("07");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number8();
        assertThat(calculator.getExpression()).isEqualTo("08");

        calculator.clear();
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.number9();
        assertThat(calculator.getExpression()).isEqualTo("09");
    }

    @Test public void number0To9_addsNumber_whenCurrentExpressionEndsWithDecimal() {
        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo(".0");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number1();
        assertThat(calculator.getExpression()).isEqualTo(".1");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number2();
        assertThat(calculator.getExpression()).isEqualTo(".2");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number3();
        assertThat(calculator.getExpression()).isEqualTo(".3");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number4();
        assertThat(calculator.getExpression()).isEqualTo(".4");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number5();
        assertThat(calculator.getExpression()).isEqualTo(".5");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number6();
        assertThat(calculator.getExpression()).isEqualTo(".6");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number7();
        assertThat(calculator.getExpression()).isEqualTo(".7");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number8();
        assertThat(calculator.getExpression()).isEqualTo(".8");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.number9();
        assertThat(calculator.getExpression()).isEqualTo(".9");
    }

    @Test public void number0To9_addsNumber_whenCurrentExpressionEndsWithOperator() {
        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number0();
        assertThat(calculator.getExpression()).isEqualTo("-0");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number1();
        assertThat(calculator.getExpression()).isEqualTo("-1");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number2();
        assertThat(calculator.getExpression()).isEqualTo("-2");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number3();
        assertThat(calculator.getExpression()).isEqualTo("-3");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number4();
        assertThat(calculator.getExpression()).isEqualTo("-4");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number5();
        assertThat(calculator.getExpression()).isEqualTo("-5");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number6();
        assertThat(calculator.getExpression()).isEqualTo("-6");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number7();
        assertThat(calculator.getExpression()).isEqualTo("-7");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number8();
        assertThat(calculator.getExpression()).isEqualTo("-8");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.number9();
        assertThat(calculator.getExpression()).isEqualTo("-9");
    }

    @Test public void divide_isIgnored_whenCurrentExpressionIsEmpty() {
        calculator.divide();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void divide_isIgnored_whenCurrentExpressionEndsWithOperatorWhichIsTheOnlySymbolInExpression() {
        calculator.subtract();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void divide_replacesPreviousOperator_whenCurrentExpressionEndsWithOperatorWhichIsNotTheOnlySymbolInExpression() {
        calculator.number0();
        calculator.subtract();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("0/");
    }

    @Test public void divide_addsDivideOperator_whenCurrentExpressionEndsWithNumber() {
        calculator.number0();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("0/");
    }

    @Test public void divide_addsDivideOperator_whenCurrentExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("./");
    }

    @Test public void multiply_isIgnored_whenCurrentExpressionIsEmpty() {
        calculator.multiply();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void multiply_isIgnored_whenCurrentExpressionEndsWithOperatorWhichIsTheOnlySymbolInExpression() {
        calculator.subtract();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void multiply_replacesPreviousOperator_whenCurrentExpressionEndsWithOperatorWhichIsNotTheOnlySymbolInExpression() {
        calculator.number0();
        calculator.subtract();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo("0*");
    }

    @Test public void multiply_addsMultiplyOperator_whenCurrentExpressionEndsWithNumber() {
        calculator.number0();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo("0*");
    }

    @Test public void multiply_addsMultiplyOperator_whenCurrentExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo(".*");
    }

    @Test public void subtract_addsSubtractOperator_whenCurrentExpressionIsEmpty() {
        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void subtract_replacesPreviousOperator_whenCurrentExpressionEndsWithOperator() {
        calculator.number0();
        calculator.add();

        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo("0-");
    }

    @Test public void subtract_addsSubtractOperator_whenCurrentExpressionEndsWithNumber() {
        calculator.number0();

        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo("0-");
    }

    @Test public void subtract_addsSubtractOperator_whenCurrentExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo(".-");
    }

    @Test public void add_isIgnored_whenCurrentExpressionIsEmpty() {
        calculator.add();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void add_isIgnored_whenCurrentExpressionEndsWithOperatorWhichIsTheOnlySymbolInExpression() {
        calculator.subtract();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void add_replacesPreviousOperator_whenCurrentExpressionEndsWithOperatorWhichIsNotTheOnlySymbolInExpression() {
        calculator.number0();
        calculator.subtract();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo("0+");
    }

    @Test public void add_addsAddOperator_whenCurrentExpressionEndsWithNumber() {
        calculator.number0();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo("0+");
    }

    @Test public void add_addsAddOperator_whenCurrentExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo(".+");
    }

    @Test public void decimal_isIgnored_whenCurrentExpressionAlreadyHasDecimal() {
        calculator.number0();
        calculator.decimal();
        calculator.number0();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0.0");
    }

    @Test public void decimal_addsDecimal_whenCurrentExpressionIsEmpty() {
        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo(".");
    }

    @Test public void decimal_addsDecimal_whenCurrentExpressionEndsWithNumberAndThereAreNoDecimalsAlready() {
        calculator.number0();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0.");
    }

    @Test public void decimal_addsDecimal_whenCurrentExpressionEndsWithAnOperatorAndThereAreNoDecimalsAlready() {
        calculator.number0();
        calculator.subtract();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0-.");
    }

    @Test public void clear_clearsTheExpression_whenExpressionIsNotEmpty() {
        calculator.number0();

        calculator.clear();

        assertThat(calculator.getExpression()).isEmpty();
    }
}
