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

package com.mvcoding.expensius.feature.calculator

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.TEN

class CalculatorTest {
    val calculator = Calculator(Interpreter())

    @Test
    fun setsExpressionToANumberWhenItIsEmpty() {
        calculator.setNumber(TEN)

        assertThat(calculator.getExpression(), equalTo("10"))
    }

    @Test
    fun setsExpressionToANumberWhenThereAlreadyIsAnExpression() {
        calculator.setNumber(TEN)

        calculator.setNumber(ONE)

        assertThat(calculator.getExpression(), equalTo("1"))
    }

    @Test
    fun clearsExpression() {
        calculator.setNumber(TEN)

        calculator.clear()

        assertThat(calculator.getExpression(), equalTo(""))
    }

    @Test
    fun deletesLastSymbolFromExpression() {
        calculator.setNumber(TEN)

        calculator.delete()

        assertThat(calculator.getExpression(), equalTo("1"))
    }

    @Test
    fun deletesLastSymbol() {
        calculator.setNumber(ONE)

        calculator.delete()

        assertThat(calculator.getExpression(), equalTo(""))
    }

    @Test
    fun addsDigitWhenExpressionIsEmpty() {
        calculator.clear()
        calculator.digit0()
        assertThat(calculator.getExpression(), equalTo("0"))

        calculator.clear()
        calculator.digit1()
        assertThat(calculator.getExpression(), equalTo("1"))

        calculator.clear()
        calculator.digit2()
        assertThat(calculator.getExpression(), equalTo("2"))

        calculator.clear()
        calculator.digit3()
        assertThat(calculator.getExpression(), equalTo("3"))

        calculator.clear()
        calculator.digit4()
        assertThat(calculator.getExpression(), equalTo("4"))

        calculator.clear()
        calculator.digit5()
        assertThat(calculator.getExpression(), equalTo("5"))

        calculator.clear()
        calculator.digit6()
        assertThat(calculator.getExpression(), equalTo("6"))

        calculator.clear()
        calculator.digit7()
        assertThat(calculator.getExpression(), equalTo("7"))

        calculator.clear()
        calculator.digit8()
        assertThat(calculator.getExpression(), equalTo("8"))

        calculator.clear()
        calculator.digit9()
        assertThat(calculator.getExpression(), equalTo("9"))
    }

    @Test
    fun addsDigitWhenExpressionEndsWithNumber() {
        calculator.clear()
        calculator.digit0()
        calculator.digit0()
        assertThat(calculator.getExpression(), equalTo("00"))

        calculator.clear()
        calculator.digit0()
        calculator.digit1()
        assertThat(calculator.getExpression(), equalTo("01"))

        calculator.clear()
        calculator.digit0()
        calculator.digit2()
        assertThat(calculator.getExpression(), equalTo("02"))

        calculator.clear()
        calculator.digit0()
        calculator.digit3()
        assertThat(calculator.getExpression(), equalTo("03"))

        calculator.clear()
        calculator.digit0()
        calculator.digit4()
        assertThat(calculator.getExpression(), equalTo("04"))

        calculator.clear()
        calculator.digit0()
        calculator.digit5()
        assertThat(calculator.getExpression(), equalTo("05"))

        calculator.clear()
        calculator.digit0()
        calculator.digit6()
        assertThat(calculator.getExpression(), equalTo("06"))

        calculator.clear()
        calculator.digit0()
        calculator.digit7()
        assertThat(calculator.getExpression(), equalTo("07"))

        calculator.clear()
        calculator.digit0()
        calculator.digit8()
        assertThat(calculator.getExpression(), equalTo("08"))

        calculator.clear()
        calculator.digit0()
        calculator.digit9()
        assertThat(calculator.getExpression(), equalTo("09"))
    }

    @Test
    fun addsDecimalWhenExpressionIsEmpty() {
        calculator.decimal()

        assertThat(calculator.getExpression(), equalTo("."))
    }

    @Test
    fun addsDigitWhenExpressionEndsWithDecimal() {
        calculator.clear()
        calculator.decimal()
        calculator.digit0()
        assertThat(calculator.getExpression(), equalTo(".0"))

        calculator.clear()
        calculator.decimal()
        calculator.digit1()
        assertThat(calculator.getExpression(), equalTo(".1"))

        calculator.clear()
        calculator.decimal()
        calculator.digit2()
        assertThat(calculator.getExpression(), equalTo(".2"))

        calculator.clear()
        calculator.decimal()
        calculator.digit3()
        assertThat(calculator.getExpression(), equalTo(".3"))

        calculator.clear()
        calculator.decimal()
        calculator.digit4()
        assertThat(calculator.getExpression(), equalTo(".4"))

        calculator.clear()
        calculator.decimal()
        calculator.digit5()
        assertThat(calculator.getExpression(), equalTo(".5"))

        calculator.clear()
        calculator.decimal()
        calculator.digit6()
        assertThat(calculator.getExpression(), equalTo(".6"))

        calculator.clear()
        calculator.decimal()
        calculator.digit7()
        assertThat(calculator.getExpression(), equalTo(".7"))

        calculator.clear()
        calculator.decimal()
        calculator.digit8()
        assertThat(calculator.getExpression(), equalTo(".8"))

        calculator.clear()
        calculator.decimal()
        calculator.digit9()
        assertThat(calculator.getExpression(), equalTo(".9"))
    }

    @Test
    fun addsSubtractOperatorWhenExpressionIsEmpty() {
        calculator.subtract()

        assertThat(calculator.getExpression(), equalTo("-"))
    }

    @Test
    fun addsDigitWhenExpressionEndsWithOperator() {
        calculator.clear()
        calculator.subtract()
        calculator.digit0()
        assertThat(calculator.getExpression(), equalTo("-0"))

        calculator.clear()
        calculator.subtract()
        calculator.digit1()
        assertThat(calculator.getExpression(), equalTo("-1"))

        calculator.clear()
        calculator.subtract()
        calculator.digit2()
        assertThat(calculator.getExpression(), equalTo("-2"))

        calculator.clear()
        calculator.subtract()
        calculator.digit3()
        assertThat(calculator.getExpression(), equalTo("-3"))

        calculator.clear()
        calculator.subtract()
        calculator.digit4()
        assertThat(calculator.getExpression(), equalTo("-4"))

        calculator.clear()
        calculator.subtract()
        calculator.digit5()
        assertThat(calculator.getExpression(), equalTo("-5"))

        calculator.clear()
        calculator.subtract()
        calculator.digit6()
        assertThat(calculator.getExpression(), equalTo("-6"))

        calculator.clear()
        calculator.subtract()
        calculator.digit7()
        assertThat(calculator.getExpression(), equalTo("-7"))

        calculator.clear()
        calculator.subtract()
        calculator.digit8()
        assertThat(calculator.getExpression(), equalTo("-8"))

        calculator.clear()
        calculator.subtract()
        calculator.digit9()
        assertThat(calculator.getExpression(), equalTo("-9"))
    }

    @Test
    fun divideIsIgnoredWhenExpressionIsEmpty() {
        calculator.divide()

        assertThat(calculator.getExpression(), equalTo(""))
    }

    @Test
    fun divideIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        calculator.subtract()

        calculator.divide()

        assertThat(calculator.getExpression(), equalTo("-"))
    }

    @Test
    fun addsSubtractOperatorWhenExpressionEndsWithNumber() {
        calculator.digit1()

        calculator.subtract()

        assertThat(calculator.getExpression(), equalTo("1-"))
    }

    @Test
    fun addsDivideOperatorWhenExpressionEndsWithNumber() {
        calculator.digit1()

        calculator.divide()

        assertThat(calculator.getExpression(), equalTo("1/"))
    }

    @Test
    fun divideReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        calculator.digit1()
        calculator.subtract()

        calculator.divide()

        assertThat(calculator.getExpression(), equalTo("1/"))
    }

    @Test
    fun addsDivideOperatorWhenExpressionEndsWithDecimal() {
        calculator.decimal()

        calculator.divide()

        assertThat(calculator.getExpression(), equalTo("./"))
    }

    @Test
    fun multiplyIsIgnoredWhenExpressionIsEmpty() {
        calculator.multiply()

        assertThat(calculator.getExpression(), equalTo(""))
    }

    @Test
    fun multiplyIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        calculator.subtract()

        calculator.multiply()

        assertThat(calculator.getExpression(), equalTo("-"))
    }

    @Test
    fun multiplyReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        calculator.digit1()
        calculator.subtract()

        calculator.multiply()

        assertThat(calculator.getExpression(), equalTo("1*"))
    }

    @Test
    fun addsMultiplyOperatorWhenExpressionEndsWithNumber() {
        calculator.digit1()

        calculator.multiply()

        assertThat(calculator.getExpression(), equalTo("1*"))
    }

    @Test
    fun addsMultiplyOperatorWhenExpressionEndsWithDecimal() {
        calculator.decimal()

        calculator.multiply()

        assertThat(calculator.getExpression(), equalTo(".*"))
    }

    @Test
    fun subtractReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        calculator.digit1()
        calculator.multiply()

        calculator.subtract()

        assertThat(calculator.getExpression(), equalTo("1-"))
    }

    @Test
    fun addsSubtractOperatorWhenExpressionEndsWithDecimal() {
        calculator.decimal()

        calculator.subtract()

        assertThat(calculator.getExpression(), equalTo(".-"))
    }

    @Test
    fun addIsIgnoredWhenExpressionIsEmpty() {
        calculator.add()

        assertThat(calculator.getExpression(), equalTo(""))
    }

    @Test
    fun addIsIgnoredWhenExpressionIsOnlySubtractOperator() {
        calculator.subtract()

        calculator.add()

        assertThat(calculator.getExpression(), equalTo("-"))
    }

    @Test
    fun addReplacesPreviousOperatorWhenExpressionEndsWithOperator() {
        calculator.digit1()
        calculator.subtract()

        calculator.add()

        assertThat(calculator.getExpression(), equalTo("1+"))
    }

    @Test
    fun addsAddOperatorWhenExpressionEndsWithNumber() {
        calculator.digit1()

        calculator.add()

        assertThat(calculator.getExpression(), equalTo("1+"))
    }

    @Test
    fun addsAddOperatorWhenExpressionEndsWithDecimal() {
        calculator.decimal()

        calculator.add()

        assertThat(calculator.getExpression(), equalTo(".+"))
    }

    @Test
    fun addsDecimalWhenExpressionHasOneNumberWithoutDecimal() {
        calculator.digit1()

        calculator.decimal()

        assertThat(calculator.getExpression(), equalTo("1."))
    }

    @Test
    fun decimalIsIgnoredWhenExpressionHasOneNumberThatAlreadyHasDecimal() {
        calculator.digit1()
        calculator.decimal()
        calculator.digit2()

        calculator.decimal()

        assertThat(calculator.getExpression(), equalTo("1.2"))
    }

    @Test
    fun addsDecimalWhenExpressionHasMoreThanOneNumberAndLastOneIsWithoutDecimal() {
        calculator.digit1()
        calculator.decimal()
        calculator.digit2()
        calculator.add()
        calculator.digit1()

        calculator.decimal()

        assertThat(calculator.getExpression(), equalTo("1.2+1."))
    }

    @Test
    fun decimalIsIgnoredWhenExpressionHasMoreThanOneNumberAndLastNumberAlreadyHasDecimal() {
        calculator.digit1()
        calculator.decimal()
        calculator.digit2()
        calculator.add()
        calculator.digit1()
        calculator.decimal()
        calculator.digit2()

        calculator.decimal()

        assertThat(calculator.getExpression(), equalTo("1.2+1.2"))
    }

    @Test
    fun addsDecimalWhenExpressionEndsWithAnOperator() {
        calculator.subtract()

        calculator.decimal()

        assertThat(calculator.getExpression(), equalTo("-."))
    }

    @Test
    fun calculateEvaluatesExpression() {
        calculator.digit1()
        calculator.decimal()
        calculator.digit2()
        calculator.add()
        calculator.digit3()
        calculator.decimal()
        calculator.digit4()

        val result = calculator.calculate()

        assertThat(result, `is`(BigDecimal("4.6")))
    }
}