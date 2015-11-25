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
import java.math.MathContext
import java.math.RoundingMode
import java.util.*
import java.util.regex.Pattern

class Interpreter {
    private val operatorPattern = Pattern.compile("[+\\-*/]")
    private val mathContext = MathContext(10, RoundingMode.HALF_UP)

    fun evaluate(expression: String): BigDecimal {
        if (expression.isBlank() || expression.equals("-")) {
            return BigDecimal.ZERO
        }

        val tokens = split(expression)

        // If expression ends with an operator, just remove it.
        if (tokens.last() is OperatorToken) {
            tokens.removeAt(tokens.lastIndex)
        }

        // If we only have one
        if (tokens.size == 1) {
            val firstToken = tokens.first()
            if (firstToken is NumberToken) {
                return firstToken.number
            }
            return BigDecimal.ZERO
        }

        val postfix = toPostfix(tokens)
        return calculatePostfix(postfix).stripTrailingZeros()
    }

    fun isEmptyOrSingleNumber(expression: String?): Boolean {
        return expression == null || expression.isBlank() || expression == "-" || split(expression).size == 1
    }

    private fun split(expression: String): MutableList<Token> {
        val tokens = arrayListOf<Token>()
        val matcher = operatorPattern.matcher(expression)
        var lastOperatorPosition = -1
        while (matcher.find(lastOperatorPosition + 1)) {
            val oldLastOperatorPosition = if (lastOperatorPosition == 0) -1 else lastOperatorPosition
            lastOperatorPosition = matcher.start()

            // Ignore if first symbol is an operator.
            if (lastOperatorPosition == 0) {
                continue
            }

            tokens.add(NumberToken(normalizeNumber(expression, oldLastOperatorPosition + 1, lastOperatorPosition)))
            tokens.add(OperatorToken(Operator.from(expression.substring(lastOperatorPosition, lastOperatorPosition + 1))))
        }

        val normalizedNumber = normalizeNumber(expression, lastOperatorPosition + 1, expression.length)
        if (!normalizedNumber.isEmpty()) {
            tokens.add(NumberToken(normalizedNumber))
        }

        if (!tokens.isEmpty() && tokens[tokens.size - 1] is OperatorToken) {
            tokens.removeAt(tokens.size - 1)
        }

        return tokens
    }

    private fun normalizeNumber(expression: String, start: Int, end: Int): String {
        var number = expression.substring(start, end)
        if (number.startsWith(".")) {
            number = "0" + number
        }

        if (number.endsWith(".")) {
            number = number.substring(0, number.length - 1)
        }

        return number
    }

    private fun toPostfix(tokens: List<Token>): Stack<Token> {
        val output = Stack<Token>()
        val stack = LinkedList<Token>()

        for (token in tokens) {
            // Operator
            if (token is OperatorToken) {
                while (!stack.isEmpty() && isHigherPrecedence(token, stack.peek())) {
                    output.add(stack.pop())
                }
                stack.push(token)
                continue
            }

            // Left parenthesis
            if (token is ParenthesisToken && token.isOpening) {
                stack.push(token)
                continue
            }

            // Right parenthesis
            if (token is ParenthesisToken && !token.isOpening) {
                while (!(stack.peek() is ParenthesisToken && (stack.peek() as ParenthesisToken).isOpening)) {
                    output.add(stack.pop())
                }
                stack.pop()
                continue
            }

            // Digit
            output.add(token)
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop())
        }

        return output
    }

    private fun calculatePostfix(tokens: Stack<Token>): BigDecimal {
        val resultStack = Stack<BigDecimal>()

        for (token in tokens) {
            if (token is NumberToken) {
                resultStack.push(token.number)
                continue
            }

            if (token !is OperatorToken) {
                throw IllegalArgumentException("Token must be an operator token.")
            }
            val secondNumber = resultStack.pop()
            val firstNumber = resultStack.pop()
            when (token.operator) {
                Interpreter.Operator.ADD -> resultStack.push(firstNumber.add(secondNumber, mathContext))
                Interpreter.Operator.SUBTRACT -> resultStack.push(firstNumber.subtract(secondNumber, mathContext))
                Interpreter.Operator.MULTIPLY -> resultStack.push(firstNumber.multiply(secondNumber, mathContext))
                Interpreter.Operator.DIVIDE -> try {
                    resultStack.push(firstNumber.divide(secondNumber, mathContext))
                } catch (e: ArithmeticException) {
                    return BigDecimal.ZERO
                }

                else -> throw IllegalStateException("Operator is not supported.")
            }
        }

        return resultStack.pop()
    }

    private fun isHigherPrecedence(token: OperatorToken, stackHeadToken: Token): Boolean {
        return stackHeadToken is OperatorToken && stackHeadToken.operator.precedence >= token.operator.precedence
    }

    private enum class Operator(val precedence: Int) {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);

        companion object {
            fun from(operator: String): Operator {
                when (operator) {
                    "+" -> return ADD
                    "-" -> return SUBTRACT
                    "*" -> return MULTIPLY
                    "/" -> return DIVIDE
                    else -> throw IllegalArgumentException("Operator $operator is not supported.")
                }
            }
        }
    }

    private interface Token

    private class NumberToken(number: String) : Token {
        val number = BigDecimal(number)
    }

    private class OperatorToken(val operator: Operator) : Token

    /**
     * Our expressions do not have parenthesis at the moment so this is not implemented.
     */
    private class ParenthesisToken : Token {
        val isOpening: Boolean
            get() = true
    }
}