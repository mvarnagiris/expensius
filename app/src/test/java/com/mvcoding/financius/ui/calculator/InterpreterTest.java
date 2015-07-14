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

import com.mvcoding.financius.BaseTest;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class InterpreterTest extends BaseTest {
    private final Interpreter interpreter = new Interpreter();

    @Test public void evaluate_returns0_whenExpressionIsNull() {
        assertThat(interpreter.evaluate(null)).isEqualTo(BigDecimal.ZERO);
    }

    @Test public void evaluate_returns0_whenExpressionIsEmpty() {
        assertThat(interpreter.evaluate("")).isEqualTo(BigDecimal.ZERO);
    }

    @Test public void evaluate_returnsSameDigit_whenExpressionContainsOnlyOneDigit() {
        assertThat(interpreter.evaluate("1")).isEqualTo(BigDecimal.ONE);
    }

    @Test public void evaluate_returnsSameNumber_whenExpressionContainsOnlyThatNumber() {
        assertThat(interpreter.evaluate("10")).isEqualTo(BigDecimal.TEN);
    }

    @Test public void evaluate_returns0_whenExpressionContainsOnlySubtractOperator() {
        assertThat(interpreter.evaluate("-")).isEqualTo(BigDecimal.ZERO);
    }

    @Test public void evaluate_returns0_whenExpressionContainsOnlyDecimal() {
        assertThat(interpreter.evaluate(".")).isEqualTo(BigDecimal.ZERO);
    }

    @Test public void evaluate_returnsCorrectResult_whenAddingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2+2.3")).isEqualTo(new BigDecimal("3.5"));
    }

    @Test public void evaluate_returnsCorrectResult_whenSubtractingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2-2.3")).isEqualTo(new BigDecimal("-1.1"));
    }

    @Test public void evaluate_returnsCorrectResult_whenMultiplyingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2*2.3")).isEqualTo(new BigDecimal("2.76"));
    }

    @Test public void evaluate_returnsCorrectResult_whenDividingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2/2.3")).isEqualTo(new BigDecimal("0.5217391304"));
    }

    @Test public void evaluate_returns0_whenDividingByZero() {
        assertThat(interpreter.evaluate("1/0")).isEqualTo(new BigDecimal("0"));
        assertThat(interpreter.evaluate("1+1+1/0")).isEqualTo(new BigDecimal("0"));
    }

    @Test public void evaluate_returnsCorrectResult_whenDoingMoreComplexOperations() {
        assertThat(interpreter.evaluate("-5*2").toPlainString()).isEqualTo("-10");
        assertThat(interpreter.evaluate("1+3*2")).isEqualTo(new BigDecimal("7"));
        assertThat(interpreter.evaluate("1+3*2-3")).isEqualTo(new BigDecimal("4"));
        assertThat(interpreter.evaluate("1+3*2/2")).isEqualTo(new BigDecimal("4"));
        assertThat(interpreter.evaluate("1+4/2")).isEqualTo(new BigDecimal("3"));
        assertThat(interpreter.evaluate("1+4/2+2*4-6/3")).isEqualTo(new BigDecimal("9"));
    }

    @Test public void isSingleNumber_returnsTrue_whenExpressionIsEmpty() {
        assertThat(interpreter.isEmptyOrSingleNumber("")).isTrue();
    }

    @Test public void isSingleNumber_returnsTrue_whenExpressionContainsOnlyOneNumber() {
        assertThat(interpreter.isEmptyOrSingleNumber("1")).isTrue();
    }

    @Test public void isSingleNumber_returnsTrue_whenExpressionContainsOnlyADecimal() {
        assertThat(interpreter.isEmptyOrSingleNumber(".")).isTrue();
    }

    @Test public void isSingleNumber_returnsTrue_whenContainsOnlySubtractSymbol() {
        assertThat(interpreter.isEmptyOrSingleNumber("-")).isTrue();
    }

    @Test public void isSingleNumber_returnsTrue_whenExpressionContainsOnlyANegativeNumber() {
        assertThat(interpreter.isEmptyOrSingleNumber("-1")).isTrue();
    }

    @Test public void isSingleNumber_returnsTrue_whenExpressionContainsSingleNumberAndSingleOperatorAfterTheNumber() {
        assertThat(interpreter.isEmptyOrSingleNumber("1+")).isTrue();
    }

    @Test public void isSingleNumber_returnsFalse_whenExpressionContainsTwoNumbers() {
        assertThat(interpreter.isEmptyOrSingleNumber("1+1")).isFalse();
    }
}