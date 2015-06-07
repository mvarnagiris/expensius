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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class CalculatorTest extends BaseTest {
    private Calculator calculator;
    private Interpreter interpreter;

    @Override public void setUp() {
        super.setUp();
        interpreter = spy(new Interpreter());
        calculator = new Calculator(interpreter);
    }

    @Test public void setNumber_clearsExpression_whenNumberIsNull() {
        calculator.setNumber(null);

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void setNumber_setsExpressionToBeThatNumber_whenNumberIsNotNull() {
        calculator.setNumber(BigDecimal.TEN);

        assertThat(calculator.getExpression()).isEqualTo("10");
    }

    @Test public void digit0To9_addsDigit_whenExpressionIsEmpty() {
        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit1();
        assertThat(calculator.getExpression()).isEqualTo("1");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit2();
        assertThat(calculator.getExpression()).isEqualTo("2");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit3();
        assertThat(calculator.getExpression()).isEqualTo("3");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit4();
        assertThat(calculator.getExpression()).isEqualTo("4");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit5();
        assertThat(calculator.getExpression()).isEqualTo("5");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit6();
        assertThat(calculator.getExpression()).isEqualTo("6");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit7();
        assertThat(calculator.getExpression()).isEqualTo("7");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit8();
        assertThat(calculator.getExpression()).isEqualTo("8");

        calculator.clear();
        assertThat(calculator.getExpression()).isEmpty();
        calculator.digit9();
        assertThat(calculator.getExpression()).isEqualTo("9");
    }

    @Test public void digit0To9_addsDigit_whenExpressionEndsWithNumber() {
        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("00");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit1();
        assertThat(calculator.getExpression()).isEqualTo("01");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit2();
        assertThat(calculator.getExpression()).isEqualTo("02");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit3();
        assertThat(calculator.getExpression()).isEqualTo("03");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit4();
        assertThat(calculator.getExpression()).isEqualTo("04");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit5();
        assertThat(calculator.getExpression()).isEqualTo("05");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit6();
        assertThat(calculator.getExpression()).isEqualTo("06");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit7();
        assertThat(calculator.getExpression()).isEqualTo("07");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit8();
        assertThat(calculator.getExpression()).isEqualTo("08");

        calculator.clear();
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("0");
        calculator.digit9();
        assertThat(calculator.getExpression()).isEqualTo("09");
    }

    @Test public void digit0To9_addsDigit_whenExpressionEndsWithDecimal() {
        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo(".0");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit1();
        assertThat(calculator.getExpression()).isEqualTo(".1");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit2();
        assertThat(calculator.getExpression()).isEqualTo(".2");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit3();
        assertThat(calculator.getExpression()).isEqualTo(".3");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit4();
        assertThat(calculator.getExpression()).isEqualTo(".4");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit5();
        assertThat(calculator.getExpression()).isEqualTo(".5");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit6();
        assertThat(calculator.getExpression()).isEqualTo(".6");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit7();
        assertThat(calculator.getExpression()).isEqualTo(".7");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit8();
        assertThat(calculator.getExpression()).isEqualTo(".8");

        calculator.clear();
        calculator.decimal();
        assertThat(calculator.getExpression()).isEqualTo(".");
        calculator.digit9();
        assertThat(calculator.getExpression()).isEqualTo(".9");
    }

    @Test public void digit0To9_addsDigit_whenExpressionEndsWithOperator() {
        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit0();
        assertThat(calculator.getExpression()).isEqualTo("-0");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit1();
        assertThat(calculator.getExpression()).isEqualTo("-1");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit2();
        assertThat(calculator.getExpression()).isEqualTo("-2");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit3();
        assertThat(calculator.getExpression()).isEqualTo("-3");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit4();
        assertThat(calculator.getExpression()).isEqualTo("-4");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit5();
        assertThat(calculator.getExpression()).isEqualTo("-5");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit6();
        assertThat(calculator.getExpression()).isEqualTo("-6");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit7();
        assertThat(calculator.getExpression()).isEqualTo("-7");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit8();
        assertThat(calculator.getExpression()).isEqualTo("-8");

        calculator.clear();
        calculator.subtract();
        assertThat(calculator.getExpression()).isEqualTo("-");
        calculator.digit9();
        assertThat(calculator.getExpression()).isEqualTo("-9");
    }

    @Test public void divide_isIgnored_whenExpressionIsEmpty() {
        calculator.divide();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void divide_isIgnored_whenExpressionEndsWithOperatorWhichIsTheOnlySymbolInExpression() {
        calculator.subtract();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void divide_replacesPreviousOperator_whenExpressionEndsWithOperatorWhichIsNotTheOnlySymbolInExpression() {
        calculator.digit0();
        calculator.subtract();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("0/");
    }

    @Test public void divide_addsDivideOperator_whenExpressionEndsWithDigit() {
        calculator.digit0();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("0/");
    }

    @Test public void divide_addsDivideOperator_whenExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.divide();

        assertThat(calculator.getExpression()).isEqualTo("./");
    }

    @Test public void multiply_isIgnored_whenExpressionIsEmpty() {
        calculator.multiply();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void multiply_isIgnored_whenExpressionEndsWithOperatorWhichIsTheOnlySymbolInExpression() {
        calculator.subtract();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void multiply_replacesPreviousOperator_whenExpressionEndsWithOperatorWhichIsNotTheOnlySymbolInExpression() {
        calculator.digit0();
        calculator.subtract();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo("0*");
    }

    @Test public void multiply_addsMultiplyOperator_whenExpressionEndsWithDigit() {
        calculator.digit0();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo("0*");
    }

    @Test public void multiply_addsMultiplyOperator_whenExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.multiply();

        assertThat(calculator.getExpression()).isEqualTo(".*");
    }

    @Test public void subtract_addsSubtractOperator_whenExpressionIsEmpty() {
        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void subtract_replacesPreviousOperator_whenExpressionEndsWithOperator() {
        calculator.digit0();
        calculator.add();

        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo("0-");
    }

    @Test public void subtract_addsSubtractOperator_whenExpressionEndsWithDigit() {
        calculator.digit0();

        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo("0-");
    }

    @Test public void subtract_addsSubtractOperator_whenExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.subtract();

        assertThat(calculator.getExpression()).isEqualTo(".-");
    }

    @Test public void add_isIgnored_whenExpressionIsEmpty() {
        calculator.add();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void add_isIgnored_whenExpressionEndsWithOperatorWhichIsTheOnlySymbolInExpression() {
        calculator.subtract();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo("-");
    }

    @Test public void add_replacesPreviousOperator_whenExpressionEndsWithOperatorWhichIsNotTheOnlySymbolInExpression() {
        calculator.digit0();
        calculator.subtract();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo("0+");
    }

    @Test public void add_addsAddOperator_whenExpressionEndsWithDigit() {
        calculator.digit0();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo("0+");
    }

    @Test public void add_addsAddOperator_whenExpressionEndsWithDecimal() {
        calculator.decimal();

        calculator.add();

        assertThat(calculator.getExpression()).isEqualTo(".+");
    }

    @Test public void decimal_isIgnored_whenExpressionHasOneNumberAndAlreadyHasDecimal() {
        calculator.digit0();
        calculator.decimal();
        calculator.digit0();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0.0");
    }

    @Test public void decimal_isIgnored_whenExpressionHasMoreThanOneNumberAndLastNumberAlreadyHasDecimal() {
        calculator.digit0();
        calculator.decimal();
        calculator.digit0();
        calculator.add();
        calculator.digit0();
        calculator.decimal();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0.0+0.");
    }

    @Test public void decimal_addsDecimal_whenExpressionAlreadyHasAtLeastOneDecimalButNumberAtTheEndDoesNot() {
        calculator.digit0();
        calculator.decimal();
        calculator.digit0();
        calculator.add();
        calculator.digit0();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0.0+0.");
    }

    @Test public void decimal_addsDecimal_whenExpressionIsEmpty() {
        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo(".");
    }

    @Test public void decimal_addsDecimal_whenExpressionEndsWithDigitAndThereAreNoDecimalsAlready() {
        calculator.digit0();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0.");
    }

    @Test public void decimal_addsDecimal_whenExpressionEndsWithAnOperator() {
        calculator.digit0();
        calculator.subtract();

        calculator.decimal();

        assertThat(calculator.getExpression()).isEqualTo("0-.");
    }

    @Test public void clear_clearsTheExpression_whenExpressionIsNotEmpty() {
        calculator.digit0();

        calculator.clear();

        assertThat(calculator.getExpression()).isEmpty();
    }

    @Test public void delete_deletesOneSymbolInTheEnd_whenExpressionIsNotEmpty() {
        calculator.digit0();
        calculator.digit1();

        calculator.delete();

        assertThat(calculator.getExpression()).isEqualTo("0");
    }

    @Test public void isEmptyOrSingleNumber_callsInterpreter() {
        calculator.isEmptyOrSingleNumber();

        verify(interpreter).isEmptyOrSingleNumber(any(String.class));
    }

    @Test public void calculate_addsTwoNumbers_whenExpressionHasOnlyTwoNumbersAndAddOperator() {
        calculator.digit1();
        calculator.decimal();
        calculator.digit2();
        calculator.add();
        calculator.digit2();
        calculator.decimal();
        calculator.digit3();

        final BigDecimal result = calculator.calculate();

        assertThat(result).isEqualTo(BigDecimal.valueOf(3.5));
    }

    @Test public void calculate_subtractsTwoNumbers_whenExpressionHasOnlyTwoNumbersAndSubtractOperator() {
        calculator.digit1();
        calculator.decimal();
        calculator.digit2();
        calculator.subtract();
        calculator.digit2();
        calculator.decimal();
        calculator.digit3();

        final BigDecimal result = calculator.calculate();

        assertThat(result).isEqualTo(BigDecimal.valueOf(-1.1));
    }

    @Test public void calculate_multipliesTwoNumbers_whenExpressionHasOnlyTwoNumbersAndMultiplyOperator() {
        calculator.digit1();
        calculator.decimal();
        calculator.digit2();
        calculator.multiply();
        calculator.digit2();
        calculator.decimal();
        calculator.digit3();

        final BigDecimal result = calculator.calculate();

        assertThat(result).isEqualTo(BigDecimal.valueOf(2.76));
    }

    @Test public void calculate_dividesTwoNumbers_whenExpressionHasOnlyTwoNumbersAndDivideOperator() {
        calculator.digit1();
        calculator.decimal();
        calculator.digit2();
        calculator.divide();
        calculator.digit2();
        calculator.decimal();
        calculator.digit3();

        final BigDecimal result = calculator.calculate();

        assertThat(result).isEqualTo(BigDecimal.valueOf(0.5217391304));
    }
}
