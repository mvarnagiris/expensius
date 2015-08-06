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

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import lombok.Getter;
import rx.Observable;
import rx.subjects.PublishSubject;

class TagsAdapter extends BaseAdapter<Tag, TagsAdapter.ViewHolder> {
    private final PublishSubject<ClickableViewHolder.ViewHolderClickEvent> clickSubject = PublishSubject.create();
    private final Set<Tag> selectedItems = new HashSet<>();

    private TagsPresenter.DisplayType displayType;

    @Override protected void onBindViewHolder(@NonNull ViewHolder holder, int position, Tag item) {
        holder.colorImageView.setColorFilter(item.getColor());
        holder.titleTextView.setText(item.getTitle());
        holder.setDisplayTypeAndCheckState(displayType, selectedItems.contains(item));
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false), clickSubject);
    }

    public void setSelectedItems(@Nullable Set<Tag> selectedItems) {
        this.selectedItems.clear();
        if (selectedItems != null) {
            this.selectedItems.addAll(selectedItems);
        }
        notifyDataSetChanged();
    }

    public void setSelected(@NonNull Tag item, boolean isSelected) {
        if (isSelected) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }

        notifyItemChanged(getItems().indexOf(item));
    }

    public Observable<TagClickEvent> getItemClickObservable() {
        return clickSubject.map(event -> new TagClickEvent(event, getItem(event.getPosition())));
    }

    public void setDisplayType(@NonNull TagsPresenter.DisplayType displayType) {
        this.displayType = displayType;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Getter public static class TagClickEvent extends ClickableViewHolder.ViewHolderClickEvent {
        private final Tag tag;

        public TagClickEvent(@NonNull ClickableViewHolder.ViewHolderClickEvent event, @NonNull Tag tag) {
            this(event.getView(), event.getPosition(), tag);
        }

        public TagClickEvent(@NonNull View view, @IntRange(from = 0) int position, @NonNull Tag tag) {
            super(view, position);
            this.tag = tag;
        }
    }

    static class ViewHolder extends ClickableViewHolder {
        @Bind(R.id.colorImageView) ImageView colorImageView;
        @Bind(R.id.titleTextView) TextView titleTextView;
        @Bind(R.id.checkBox) CheckBox checkBox;

        ViewHolder(@NonNull View itemView, @NonNull PublishSubject<ViewHolderClickEvent> clickSubject) {
            super(itemView, clickSubject);
        }

        public void setDisplayTypeAndCheckState(@NonNull TagsPresenter.DisplayType displayType, boolean isSelected) {
            final int colorMarginLeft;
            final int titleMarginLeft;
            switch (displayType) {
                case View:
                    checkBox.setVisibility(View.GONE);
                    colorMarginLeft = 0;
                    titleMarginLeft = itemView.getResources().getDimensionPixelSize(R.dimen.keyline_2x);
                    break;
                case Select:
                    checkBox.setVisibility(View.GONE);
                    colorMarginLeft = 0;
                    titleMarginLeft = itemView.getResources().getDimensionPixelSize(R.dimen.keyline_2x);
                    break;
                case MultiChoice:
                    checkBox.setVisibility(View.VISIBLE);
                    colorMarginLeft = itemView.getResources().getDimensionPixelSize(R.dimen.keyline_2x);
                    titleMarginLeft = itemView.getResources().getDimensionPixelSize(R.dimen.space_normal);
                    break;
                default:
                    throw new IllegalArgumentException("DisplayType " + displayType + " is not supported.");
            }

            ((ViewGroup.MarginLayoutParams) colorImageView.getLayoutParams()).leftMargin = colorMarginLeft;
            ((ViewGroup.MarginLayoutParams) titleTextView.getLayoutParams()).leftMargin = titleMarginLeft;
            checkBox.setChecked(isSelected);
        }
    }
}
