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
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Interpreter {
    private static final Pattern operatorPattern = Pattern.compile("[+\\-*/]");

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

        return calculatePostfix(toPostfix(tokens)).stripTrailingZeros();
    }

    private List<Token> split(String expression) {
        final List<Token> tokens = new ArrayList<>();
        final Matcher matcher = operatorPattern.matcher(expression);
        int lastOperatorPosition = -1;
        while (matcher.find(lastOperatorPosition + 1)) {
            final int oldLastOperatorPosition = lastOperatorPosition;
            lastOperatorPosition = matcher.start();

            // Ignore if first symbol is an operator.
            if (lastOperatorPosition == 0) {
                continue;
            }

            tokens.add(new NumberToken(normalizeNumber(expression, oldLastOperatorPosition + 1, lastOperatorPosition)));
            tokens.add(new OperatorToken(Operator.from(expression.substring(lastOperatorPosition, lastOperatorPosition + 1))));
        }

        tokens.add(new NumberToken(normalizeNumber(expression, lastOperatorPosition + 1, expression.length())));

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
        final Token firstToken = tokens.remove(0);
        final Token secondToken = tokens.remove(0);
        if (!(firstToken instanceof NumberToken) || !(secondToken instanceof NumberToken)) {
            throw new IllegalArgumentException("First two tokens must always be numbers.");
        }

        return calculatePostfixStep(((NumberToken) firstToken).number, ((NumberToken) secondToken).number, tokens);
    }

    private BigDecimal calculatePostfixStep(@NonNull BigDecimal numberOne, @NonNull BigDecimal numberTwo, @NonNull Stack<Token> tokens) {
        Token nextToken = tokens.remove(0);
        BigDecimal result = numberTwo;
        while (nextToken instanceof NumberToken) {
            result = calculatePostfixStep(result, ((NumberToken) nextToken).number, tokens);
            if (!tokens.isEmpty()) {
                nextToken = tokens.remove(0);
            } else {
                break;
            }
        }

        if (nextToken instanceof OperatorToken) {
            switch (((OperatorToken) nextToken).operator) {
                case ADD:
                    return numberOne.add(result);
                case SUBTRACT:
                    return numberOne.subtract(result);
                case MULTIPLY:
                    return numberOne.multiply(result);
                case DIVIDE:
                    return numberOne.divide(result, 10, BigDecimal.ROUND_HALF_UP);
                default:
                    throw new IllegalStateException("Operator is not supported.");
            }
        } else {
            throw new IllegalArgumentException("Token needs to be an operator.");
        }
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
