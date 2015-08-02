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

package com.mvcoding.financius.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<T> items = new ArrayList<>();

    @Override public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, position, getItem(position));
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(@Nullable List<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void insertItems(int position, @Nullable T... items) {
        if (items == null) {
            return;
        }

        this.items.addAll(position, Arrays.asList(items));
        notifyItemRangeInserted(position, items.length);
    }

    public void insertItems(int position, @Nullable List<T> items) {
        if (items == null) {
            return;
        }

        this.items.addAll(position, items);
        notifyItemRangeInserted(position, items.size());
    }

    public void updateItems(int position, @Nullable List<T> items) {
        if (items == null) {
            return;
        }

        final List<T> toReplace = this.items.subList(position, position + items.size());
        toReplace.clear();
        toReplace.addAll(items);
        notifyItemRangeChanged(position, items.size());
    }

    protected abstract void onBindViewHolder(@NonNull VH holder, int position, T item);
}
