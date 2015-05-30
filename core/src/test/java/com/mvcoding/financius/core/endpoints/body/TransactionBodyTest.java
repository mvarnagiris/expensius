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

package com.mvcoding.financius.core.endpoints.body;

import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class TransactionBodyTest {
    private TransactionBody body;

    @Before public void setUp() {
        body = new TransactionBody();
    }

    @Test public void validate_doesNotThrowException_whenAllFieldsAreValid() {
        makeAllFieldsValid(body);

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenIdIsNull() {
        makeAllFieldsValid(body);
        body.setId(null);

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenIdIsEmpty() {
        makeAllFieldsValid(body);
        body.setId("");

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenModelStateIsNull() {
        makeAllFieldsValid(body);
        body.setModelState(null);

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenTransactionTypeIsNull() {
        makeAllFieldsValid(body);
        body.setTransactionType(null);

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenTransactionStateIsNull() {
        makeAllFieldsValid(body);
        body.setTransactionState(null);

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenAmountIsNull() {
        makeAllFieldsValid(body);
        body.setAmount(null);

        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenAmountIsLessThan0() {
        makeAllFieldsValid(body);
        body.setAmount(BigDecimal.ONE.negate());

        body.validate();
    }

    private void makeAllFieldsValid(TransactionBody body) {
        body.setId("any");
        body.setModelState(ModelState.Normal);
        body.setTransactionType(TransactionType.Expense);
        body.setTransactionState(TransactionState.Confirmed);
        body.setAmount(BigDecimal.ZERO);
    }
}
