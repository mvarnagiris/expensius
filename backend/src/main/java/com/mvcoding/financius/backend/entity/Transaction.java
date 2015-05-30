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
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.mvcoding.financius.core.model.TransactionState;
import com.mvcoding.financius.core.model.TransactionType;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Transaction extends BaseEntity {
    @ApiResourceProperty(name = "transactionType") private TransactionType transactionType;
    @ApiResourceProperty(name = "transactionState") private TransactionState transactionState;
    @ApiResourceProperty(name = "amount") private BigDecimal amount;
    @ApiResourceProperty(name = "place") @Ignore private Place place;
    @ApiResourceProperty(name = "tags") @Ignore private Set<Tag> tags;
    @ApiResourceProperty(name = "note") private String note;

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE) @Load private Ref<Place> placeRef;
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE) @Load private Set<Ref<Tag>> tagsRef;

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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
        placeRef = place == null ? null : Ref.create(place);
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
        if (tags == null) {
            tagsRef = null;
        } else {
            tagsRef = new HashSet<Ref<Tag>>();
            for (Tag tag : tags) {
                tagsRef.add(Ref.create(tag));
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
