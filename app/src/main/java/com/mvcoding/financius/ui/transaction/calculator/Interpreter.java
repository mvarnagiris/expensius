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

import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

class Interpreter {
    public BigDecimal evaluate(String expression) {
        if (Strings.isNullOrEmpty(expression) || expression.equals("-")) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.ZERO;
    }

    private List<Token> split(String expression) {
        final String operators = "-=*/";
        final boolean isFirstNumberNegative = expression.startsWith("-");
        final String[] split = (isFirstNumberNegative ? expression.substring(1) : expression).split("[+\\-*/]");
        final List<Token> tokens = new ArrayList<>();

        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {

        }

        return tokens;
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

    private boolean isHigherPrecedence(OperatorToken token, Token stackHeadToken) {
        return (stackHeadToken instanceof OperatorToken && ((OperatorToken) stackHeadToken).operator.precedence >= token.operator.precedence);
    }

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);

        private final int precedence;

        Operator(int precedence) {
            this.precedence = precedence;
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
