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

package com.mvcoding.financius.data.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.mvcoding.financius.UserSettings;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Transaction extends Model<TransactionBody> {
    @SerializedName("transactionType") private TransactionType transactionType;
    @SerializedName("transactionState") private TransactionState transactionState;
    @SerializedName("date") private long date;
    @SerializedName("amount") private BigDecimal amount;
    @SerializedName("currency") private String currency;
    @SerializedName("place") private Place place;
    @SerializedName("tags") private Set<Tag> tags;
    @SerializedName("note") private String note;

    @NonNull @Override public TransactionBody toBody() {
        final TransactionBody body = super.toBody();
        body.setTransactionType(transactionType);
        body.setTransactionState(transactionState);
        body.setDate(date);
        body.setAmount(amount);
        body.setCurrency(currency);
        body.setNote(note);

        if (place != null) {
            body.setPlaceId(place.getId());
        }

        if (tags != null) {
            final Set<String> tagIds = new HashSet<>();
            for (Tag tag : tags) {
                tagIds.add(tag.getId());
            }
            body.setTagIds(tagIds);
        }

        return body;
    }

    @NonNull @Override public Transaction withDefaultValues() {
        super.withDefaultValues();
        transactionType = TransactionType.Expense;
        transactionState = TransactionState.Confirmed;
        amount = BigDecimal.ZERO;
        date = System.currentTimeMillis();
        return this;
    }

    @NonNull public Transaction withDefaultValues(@NonNull UserSettings userSettings) {
        withDefaultValues();
        currency = userSettings.getCurrency();
        return this;
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @NonNull @Override protected TransactionBody createBody() {
        return new TransactionBody();
    }
}
