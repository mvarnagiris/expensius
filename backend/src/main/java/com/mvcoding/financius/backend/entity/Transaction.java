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

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.mvcoding.financius.core.endpoints.body.TransactionBody;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity public class Transaction extends BaseEntity {
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE) @Ignore private UserAccount userAccount;
    @ApiResourceProperty(name = "transactionType") private TransactionType transactionType;
    @ApiResourceProperty(name = "transactionState") private TransactionState transactionState;
    @ApiResourceProperty(name = "date") private long date;
    @ApiResourceProperty(name = "amount") private BigDecimal amount;
    @ApiResourceProperty(name = "currency") private String currency;
    @ApiResourceProperty(name = "place") @Ignore private Place place;
    @ApiResourceProperty(name = "tags") @Ignore private Set<Tag> tags;
    @ApiResourceProperty(name = "note") private String note;

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE) @Load @Index private Ref<UserAccount> userAccountRef;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE) @Load private Ref<Place> placeRef;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE) @Load private Set<Ref<Tag>> tagsRef;

    public static Transaction from(UserAccount userAccount, TransactionBody body) {
        final Transaction transaction = new Transaction();
        transaction.setId(body.getId());
        transaction.setUserAccount(userAccount);
        transaction.setModelState(body.getModelState());
        transaction.setTransactionType(body.getTransactionType());
        transaction.setTransactionState(body.getTransactionState());
        transaction.setDate(body.getDate());
        transaction.setAmount(body.getAmount());
        transaction.setCurrency(body.getCurrency());
        transaction.setPlaceId(body.getPlaceId());
        transaction.setTagIds(body.getTagIds());
        transaction.setNote(body.getNote());
        return transaction;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
        userAccountRef = Ref.create(userAccount);
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
        placeRef = place == null ? null : Ref.create(place);
    }

    public void setPlaceId(String placeId) {
        placeRef = placeId == null ? null : Ref.create(Key.create(Place.class, placeId));
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
        if (tags == null || tags.isEmpty()) {
            tagsRef = null;
        } else {
            tagsRef = new HashSet<Ref<Tag>>();
            for (Tag tag : tags) {
                tagsRef.add(Ref.create(tag));
            }
        }
    }

    public void setTagIds(Set<String> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            tagsRef = null;
        } else {
            tagsRef = new HashSet<Ref<Tag>>();
            for (String tagId : tagIds) {
                Ref<Tag> tagRef = tagId == null ? null : Ref.create(Key.create(Tag.class, tagId));
                if (tagRef != null) {
                    tagsRef.add(tagRef);
                }
            }

            if (tagsRef.isEmpty()) {
                tagsRef = null;
            }
        }
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @OnLoad void deref() {
        userAccount = userAccountRef.get();

        if (placeRef != null) {
            place = placeRef.get();
        }

        if (tagsRef != null) {
            tags = new HashSet<Tag>();
            for (Ref<Tag> tagRef : tagsRef) {
                tags.add(tagRef.get());
            }
        }
    }
}
