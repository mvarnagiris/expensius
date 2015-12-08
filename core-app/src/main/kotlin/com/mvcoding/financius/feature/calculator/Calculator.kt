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

package com.mvcoding.financius.feature.calculator

import java.math.BigDecimal
import kotlin.text.Regex

class Calculator(private val interpreter: Interpreter) {
    private val operatorRegex = Regex("[+\\-*/]")
    private var expression = ""

    fun setNumber(number: BigDecimal) {
        expression = number.toPlainString()
    }

    fun clear() {
        expression = ""
    }

    fun delete() {
        if (expression.length > 1) {
            expression = expression.substring(0, expression.length - 1)
        }
    }

    fun getExpression() = expression

    fun digit0() {
        expression += "0"
    }

    fun digit1() {
        expression += "1"
    }

    fun digit2() {
        expression += "2"
    }

    fun digit3() {
        expression += "3"
    }

    fun digit4() {
        expression += "4"
    }

    fun digit5() {
        expression += "5"
    }

    fun digit6() {
        expression += "6"
    }

    fun digit7() {
        expression += "7"
    }

    fun digit8() {
        expression += "8"
    }

    fun digit9() {
        expression += "9"
    }

    fun decimal() {
        if (lastNumberHasDecimal() && expressionEndsWithOperator().not()) {
            return
        }

        expression += "."
    }

    fun add() {
        operator("+")
    }

    fun subtract() {
        if (expressionEndsWithOperator()) {
            replaceOperatorAtTheEnd("-")
        } else {
            expression += "-"
        }
    }

    fun multiply() {
        operator("*")
    }

    fun divide() {
        operator("/")
    }

    private fun operator(operator: String) {
        if (expressionIsEmpty() || (expressionLengthIsOne() && expressionEndsWithOperator())) {
            return
        }

        if (expressionEndsWithOperator()) {
            replaceOperatorAtTheEnd(operator)
        } else {
            expression += operator
        }
    }

    private fun replaceOperatorAtTheEnd(operator: String) {
        expression = expression.replaceRange(expression.length - 1, expression.length, operator)
    }

    private fun expressionIsEmpty() = expression.length == 0

    private fun expressionLengthIsOne() = expression.length == 1

    private fun expressionEndsWithOperator() = expressionIsEmpty().not() && expression.substring(expression.length - 1).matches(operatorRegex)

    private fun lastNumberHasDecimal() = expressionIsEmpty().not() && expression.split(operatorRegex).last().contains(".")
}