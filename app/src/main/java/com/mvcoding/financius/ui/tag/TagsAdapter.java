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

package com.mvcoding.financius.ui.tag;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvcoding.financius.R;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.ui.BaseAdapter;
import com.mvcoding.financius.ui.BaseViewHolder;

import butterknife.Bind;

class TagsAdapter extends BaseAdapter<Tag, TagsAdapter.ViewHolder> {
    @Override protected void onBindViewHolder(@NonNull ViewHolder holder, int position, Tag item) {
        holder.colorImageView.setColorFilter(item.getColor());
        holder.titleTextView.setText(item.getTitle());
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false));
    }

    static class ViewHolder extends BaseViewHolder {
        @Bind(R.id.colorImageView) ImageView colorImageView;
        @Bind(R.id.titleTextView) TextView titleTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
