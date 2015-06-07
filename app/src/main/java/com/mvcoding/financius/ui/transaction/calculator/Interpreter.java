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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkArgument;

class Interpreter {
    private static final Pattern operatorPattern = Pattern.compile("[+\\-*/]");

    private final MathContext mathContext = new MathContext(10, RoundingMode.HALF_UP);

    @Inject public Interpreter() {
    }

    public BigDecimal evaluate(@Nullable String expression) {
        if (Strings.isNullOrEmpty(expression) || expression.equals("-")) {
            return BigDecimal.ZERO;
        }

        final List<Token> tokens = split(expression);

        // If expression ends with an operator, just remove it.
        if (tokens.get(tokens.size() - 1) instanceof OperatorToken) {
            tokens.remove(tokens.size() - 1);
        }

        // If we only have one
        if (tokens.size() == 1) {
            if (tokens.get(0) instanceof NumberToken) {
                return ((NumberToken) tokens.get(0)).number;
            }
            return BigDecimal.ZERO;
        }

        final Stack<Token> postfix = toPostfix(tokens);
        return calculatePostfix(postfix).stripTrailingZeros();
    }

    public boolean isEmptyOrSingleNumber(@Nullable String expression) {
        return Strings.isNullOrEmpty(expression) || expression.equals("-") || split(expression).size() == 1;
    }

    private List<Token> split(String expression) {
        final List<Token> tokens = new ArrayList<>();
        final Matcher matcher = operatorPattern.matcher(expression);
        int lastOperatorPosition = -1;
        while (matcher.find(lastOperatorPosition + 1)) {
            final int oldLastOperatorPosition = lastOperatorPosition == 0 ? -1 : lastOperatorPosition;
            lastOperatorPosition = matcher.start();

            // Ignore if first symbol is an operator.
            if (lastOperatorPosition == 0) {
                continue;
            }

            tokens.add(new NumberToken(normalizeNumber(expression, oldLastOperatorPosition + 1, lastOperatorPosition)));
            tokens.add(new OperatorToken(Operator.from(expression.substring(lastOperatorPosition, lastOperatorPosition + 1))));
        }

        final String normalizedNumber = normalizeNumber(expression, lastOperatorPosition + 1, expression.length());
        if (!normalizedNumber.isEmpty()) {
            tokens.add(new NumberToken(normalizedNumber));
        }

        if (!tokens.isEmpty() && tokens.get(tokens.size() - 1) instanceof OperatorToken) {
            tokens.remove(tokens.size() - 1);
        }

        return tokens;
    }

    private String normalizeNumber(@NonNull String expression, int start, int end) {
        String number = expression.substring(start, end);
        if (number.startsWith(".")) {
            number = "0" + number;
        }

        if (number.endsWith(".")) {
            number = number.substring(0, number.length() - 1);
        }

        return number;
    }

    private Stack<Token> toPostfix(@NonNull List<Token> tokens) {
        final Stack<Token> output = new Stack<>();
        final Deque<Token> stack = new LinkedList<>();

        for (Token token : tokens) {
            // Operator
            if (token instanceof OperatorToken) {
                while (!stack.isEmpty() && isHigherPrecedence((OperatorToken) token, stack.peek())) {
                    output.add(stack.pop());
                }
                stack.push(token);
                continue;
            }

            // Left parenthesis
            if (token instanceof ParenthesisToken && ((ParenthesisToken) token).isOpening()) {
                stack.push(token);
                continue;
            }

            // Right parenthesis
            if (token instanceof ParenthesisToken && !((ParenthesisToken) token).isOpening()) {
                while (!(stack.peek() instanceof ParenthesisToken && ((ParenthesisToken) stack.peek()).isOpening())) {
                    output.add(stack.pop());
                }
                stack.pop();
                continue;
            }

            // Digit
            output.add(token);
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }

    private BigDecimal calculatePostfix(@NonNull Stack<Token> tokens) {
        final Stack<BigDecimal> resultStack = new Stack<>();

        for (Token token : tokens) {
            if (token instanceof NumberToken) {
                resultStack.push(((NumberToken) token).number);
                continue;
            }

            checkArgument(token instanceof OperatorToken, "Token must be an operator token.");
            final BigDecimal secondNumber = resultStack.pop();
            final BigDecimal firstNumber = resultStack.pop();
            switch (((OperatorToken) token).operator) {
                case ADD:
                    resultStack.push(firstNumber.add(secondNumber, mathContext));
                    break;
                case SUBTRACT:
                    resultStack.push(firstNumber.subtract(secondNumber, mathContext));
                    break;
                case MULTIPLY:
                    resultStack.push(firstNumber.multiply(secondNumber, mathContext));
                    break;
                case DIVIDE:
                    try {
                        resultStack.push(firstNumber.divide(secondNumber, mathContext));
                    } catch (ArithmeticException e) {
                        return BigDecimal.ZERO;
                    }
                    break;
                default:
                    throw new IllegalStateException("Operator is not supported.");
            }
        }

        return resultStack.pop();
    }

    private boolean isHigherPrecedence(OperatorToken token, Token stackHeadToken) {
        return (stackHeadToken instanceof OperatorToken && ((OperatorToken) stackHeadToken).operator.precedence >= token.operator.precedence);
    }

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);

        private final int precedence;

        Operator(int precedence) {
            this.precedence = precedence;
        }

        public static Operator from(String operator) {
            switch (operator) {
                case "+":
                    return ADD;
                case "-":
                    return SUBTRACT;
                case "*":
                    return MULTIPLY;
                case "/":
                    return DIVIDE;
                default:
                    throw new IllegalArgumentException("Operator " + operator + " is not supported.");
            }
        }
    }

    private interface Token {
    }

    private static class NumberToken implements Token {
        private final BigDecimal number;

        private NumberToken(String number) {
            this.number = new BigDecimal(number);
        }
    }

    private static class OperatorToken implements Token {
        private final Operator operator;

        private OperatorToken(Operator operator) {
            this.operator = operator;
        }
    }

    /**
     * Our expressions do not have parenthesis at the moment so this is not implemented.
     */
    private static class ParenthesisToken implements Token {
        public boolean isOpening() {
            return true;
        }
    }
}
