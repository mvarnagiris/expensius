/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.calculator

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.math.BigDecimal.*

class InterpreterTest {
    val interpreter = Interpreter()

    @Test
    fun returns0WhenExpressionIsBlank() {
        assertThat(interpreter.evaluate(""), equalTo(ZERO))
    }

    @Test
    fun returnsSameDigitWhenExpressionContainsOnlyThatDigit() {
        assertThat(interpreter.evaluate("1"), equalTo(ONE))
    }

    @Test
    fun returnsSameNumberWhenExpressionContainsOnlyThatNumber() {
        assertThat(interpreter.evaluate("10"), equalTo(TEN))
    }

    @Test
    fun returns0WhenExpressionContainsOnlySubtractOperator() {
        assertThat(interpreter.evaluate("-"), equalTo(ZERO))
    }

    @Test
    fun returns0WhenExpressionContainsOnlyDecimal() {
        assertThat(interpreter.evaluate("."), equalTo(ZERO))
    }

    @Test
    fun returnsCorrectResultWhenAddingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2+2.3"), equalTo(BigDecimal("3.5")))
    }

    @Test
    fun returnsCorrectResultWhenSubtractingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2-2.3"), equalTo(BigDecimal("-1.1")))
    }

    @Test
    fun returnsCorrectResultWhenMultiplyingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2*2.3"), equalTo(BigDecimal("2.76")))
    }

    @Test
    fun returnsCorrectResultWhenDividingTwoNumbers() {
        assertThat(interpreter.evaluate("1.2/2.3"), equalTo(BigDecimal("0.5217391304")))
    }

    @Test
    fun returns0WhenDividingByZero() {
        assertThat(interpreter.evaluate("1/0"), equalTo(ZERO))
        assertThat(interpreter.evaluate("1+2+3/0"), equalTo(ZERO))
    }

    @Test
    fun returnsCorrectResultWhenDoingMoreComplexOperations() {
        assertThat(interpreter.evaluate("-5*2"), equalTo(BigDecimal("-10")))
        assertThat(interpreter.evaluate("1+3*2"), equalTo(BigDecimal("7")))
        assertThat(interpreter.evaluate("1+3*2-3"), equalTo(BigDecimal("4")))
        assertThat(interpreter.evaluate("1+3*2/2"), equalTo(BigDecimal("4")))
        assertThat(interpreter.evaluate("1+4/2"), equalTo(BigDecimal("3")))
        assertThat(interpreter.evaluate("1+4/2+2*4-6/3"), equalTo(BigDecimal("9")))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsTrueWhenExpressionIsEmpty() {
        assertThat(interpreter.isEmptyOrSingleNumber(""), `is`(true))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsTrueWhenExpressionContainsOnlyOneNumber() {
        assertThat(interpreter.isEmptyOrSingleNumber("1"), `is`(true))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsTrueWhenExpressionContainsOnlyADecimal() {
        assertThat(interpreter.isEmptyOrSingleNumber("."), `is`(true))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsTrueWhenExpressionContainsOnlyASubtractOperator() {
        assertThat(interpreter.isEmptyOrSingleNumber("-"), `is`(true))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsTrueWhenExpressionContainsOnlyANegativeNumber() {
        assertThat(interpreter.isEmptyOrSingleNumber("-1"), `is`(true))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsTrueWhenExpressionContainsOnlyASingleNumberAndSingleOperator() {
        assertThat(interpreter.isEmptyOrSingleNumber("1+"), `is`(true))
    }

    @Test
    fun isEmptyOrSingleNumberReturnsFalseWhenExpressionContainsTwoNumbers() {
        assertThat(interpreter.isEmptyOrSingleNumber("1+1"), `is`(false))
    }
}