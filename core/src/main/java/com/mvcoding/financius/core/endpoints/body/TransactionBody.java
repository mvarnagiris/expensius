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

import java.math.BigDecimal;
import java.util.Set;

public class TransactionBody extends ModelBody {
    private TransactionType transactionType;
    private TransactionState transactionState;
    private long date;
    private BigDecimal amount;
    private String currency;
    private String placeId;
    private Set<String> tagIds;
    private String note;

    @Override public void validate() throws RuntimeException {
        super.validate();
        validateTransactionType();
        validateTransactionState();
        validateAmount();
        validateCurrency();
    }

    public void validateTransactionType() throws ValidationException {
        if (!NotNullValidator.get().isValid(transactionType)) {
            throw new ValidationException("Transaction type cannot be null.");
        }
    }

    public void validateTransactionState() throws ValidationException {
        if (!NotNullValidator.get().isValid(transactionState)) {
            throw new ValidationException("Transaction state cannot be null.");
        }
    }

    public void validateAmount() throws ValidationException {
        if (!NotNullValidator.get().isValid(amount) || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Amount cannot be null and must be >= 0.");
        }
    }

    public void validateCurrency() throws ValidationException {
        if (!NotEmptyValidator.get().isValid(currency) || currency.length() != 3) {
            throw new ValidationException("Currency length needs to be 3.");
        }
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionState getTransactionState() {
        return transactionState;
    }

    public void setTransactionState(TransactionState transactionState) {
        this.transactionState = transactionState;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Set<String> getTagIds() {
        return tagIds;
    }

    public void setTagIds(Set<String> tagIds) {
        this.tagIds = tagIds;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
