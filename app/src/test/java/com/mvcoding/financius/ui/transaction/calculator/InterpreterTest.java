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

    @Test public void evaluate_returns3p5_whenExpressionIs1p2Plus2p3() {
        assertThat(interpreter.evaluate("1.2+1.3")).isEqualTo(new BigDecimal("3.5"));
    }
}