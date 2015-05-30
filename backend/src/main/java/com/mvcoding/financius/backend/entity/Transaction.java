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

package com.mvcoding.financius.backend.entity;

import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Load;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import java.math.BigDecimal;
import java.util.Set;

@Entity
public class Transaction extends BaseEntity {
    @ApiResourceProperty(name = "transactionType") private TransactionType transactionType;
    @ApiResourceProperty(name = "transactionState") private TransactionState transactionState;
    @ApiResourceProperty(name = "amount") private BigDecimal amount;
    @ApiResourceProperty(name = "place") @Load private Ref<Place> place;
    @ApiResourceProperty(name = "tags") @Load private Set<Ref<Tag>> tags;
    @ApiResourceProperty(name = "note") private String note;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Ref<Place> getPlace() {
        return place;
    }

    public void setPlace(Ref<Place> place) {
        this.place = place;
    }

    public Set<Ref<Tag>> getTags() {
        return tags;
    }

    public void setTags(Set<Ref<Tag>> tags) {
        this.tags = tags;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
