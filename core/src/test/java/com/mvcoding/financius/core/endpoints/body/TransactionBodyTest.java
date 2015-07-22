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

import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

public class TransactionBodyTest extends BaseModelBodyTest<TransactionBody> {
    @Override protected TransactionBody createBody() {
        return new TransactionBody();
    }

    @Test public void validate_doesNotThrowException_whenAllFieldsAreValid() {
        makeAllFieldsValid(body);
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenTransactionTypeIsNull() {
        body.setTransactionType(null);
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenTransactionStateIsNull() {
        body.setTransactionState(null);
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenAmountIsNull() {
        body.setAmount(null);
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenAmountIsLessThan0() {
        body.setAmount(BigDecimal.ONE.negate());
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenCurrencyIsNull() {
        body.setCurrency(null);
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenCurrencyLengthIsLessThan3() {
        body.setCurrency("GB");
        body.validate();
    }

    @Test(expected = RuntimeException.class) public void validate_throwsRuntimeException_whenCurrencyLengthIsMoreThan3() {
        body.setCurrency("GBPP");
        body.validate();
    }

    @Override protected void makeAllFieldsValid(TransactionBody body) {
        super.makeAllFieldsValid(body);
        body.setTransactionType(TransactionType.Expense);
        body.setTransactionState(TransactionState.Confirmed);
        body.setDate(System.currentTimeMillis());
        body.setAmount(BigDecimal.ONE);
        body.setCurrency("GBP");
        body.setPlaceId("placeId");
        body.setTagIds(Collections.<String>emptySet());
        body.setNote("note");
    }
}
