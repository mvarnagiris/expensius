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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mvcoding.financius.R;
import com.mvcoding.financius.data.model.Tag;
import com.mvcoding.financius.ui.BaseAdapter;
import com.mvcoding.financius.ui.ClickableViewHolder;

import butterknife.Bind;
import rx.Observable;
import rx.subjects.PublishSubject;

class TagsAdapter extends BaseAdapter<Tag, TagsAdapter.ViewHolder> {
    private final PublishSubject<Integer> clickSubject = PublishSubject.create();

    private TagsPresenter.DisplayType displayType;

    @Override protected void onBindViewHolder(@NonNull ViewHolder holder, int position, Tag item) {
        holder.colorImageView.setColorFilter(item.getColor());
        holder.titleTextView.setText(item.getTitle());
        holder.checkBox.setVisibility(displayType == TagsPresenter.DisplayType.Select ? View.VISIBLE : View.GONE);
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false), clickSubject);
    }

    public Observable<Tag> getItemClickObservable() {
        return clickSubject.map(this::getItem);
    }

    public void setDisplayType(@NonNull TagsPresenter.DisplayType displayType) {
        this.displayType = displayType;
        notifyItemRangeChanged(0, getItemCount());
    }

    static class ViewHolder extends ClickableViewHolder {
        @Bind(R.id.colorImageView) ImageView colorImageView;
        @Bind(R.id.titleTextView) TextView titleTextView;
        @Bind(R.id.checkBox) CheckBox checkBox;

        ViewHolder(@NonNull View itemView, @NonNull PublishSubject<Integer> clickSubject) {
            super(itemView, clickSubject);
        }
    }
}
