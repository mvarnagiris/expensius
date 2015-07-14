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

import com.google.common.base.Strings;
import com.mvcoding.financius.core.model.ModelState;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import java.math.BigDecimal;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class TransactionBody implements Body {
    private String id;
    private ModelState modelState;
    private TransactionType transactionType;
    private TransactionState transactionState;
    private long date;
    private BigDecimal amount;
    private String currency;
    private String placeId;
    private Set<String> tagIds;
    private String note;

    @Override public void validate() throws RuntimeException {
        checkState(!Strings.isNullOrEmpty(id), "Id cannot be empty.");
        checkNotNull(modelState, "Model state cannot be null.");
        checkNotNull(transactionType, "Transaction type cannot be null.");
        checkNotNull(transactionState, "Transaction state cannot be null.");
        checkNotNull(amount, "Amount cannot be null.");
        checkState(currency != null && currency.length() == 3, "Currency length needs to be 3.");
        checkState(amount.compareTo(BigDecimal.ZERO) >= 0, "Amount needs to be >=0.");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ModelState getModelState() {
        return modelState;
    }

    public void setModelState(ModelState modelState) {
        this.modelState = modelState;
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
