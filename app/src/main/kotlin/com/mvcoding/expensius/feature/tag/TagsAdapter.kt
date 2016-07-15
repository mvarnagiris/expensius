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

package com.mvcoding.expensius.feature.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.BaseClickableAdapter
import com.mvcoding.expensius.feature.ClickableViewHolder
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.model.Tag
import rx.subjects.PublishSubject

class TagsAdapter() : BaseClickableAdapter<Tag, ClickableViewHolder<View>>() {
    private val VIEW_TYPE_DEFAULT = 0
    private val VIEW_TYPE_ARCHIVED = 1

    var modelDisplayType = VIEW_NOT_ARCHIVED
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun isTagPosition(position: Int) = position < itemCount - 1 || modelDisplayType != VIEW_NOT_ARCHIVED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (modelDisplayType == VIEW_NOT_ARCHIVED) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (isTagPosition(position)) VIEW_TYPE_DEFAULT else VIEW_TYPE_ARCHIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, positionClickedSubject: PublishSubject<Int>): ClickableViewHolder<View> =
            ClickableViewHolder(
                    if (viewType == VIEW_TYPE_DEFAULT) TagItemView.inflate(parent)
                    else LayoutInflater.from(parent.context).inflate(R.layout.item_view_archived, parent, false),
                    positionClickedSubject)

    override fun onBindViewHolder(holder: ClickableViewHolder<View>, position: Int) {
        if (holder.view is TagItemView) {
            holder.view.setTag(getItem(position))
        }
    }
}