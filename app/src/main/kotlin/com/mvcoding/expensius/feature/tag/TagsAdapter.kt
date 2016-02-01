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

import android.view.ViewGroup
import com.mvcoding.expensius.feature.BaseClickableAdapter
import com.mvcoding.expensius.feature.ClickableViewHolder
import rx.subjects.PublishSubject

class TagsAdapter() : BaseClickableAdapter<Tag, ClickableViewHolder<TagItemView>>() {
    var displayType: TagsPresenter.DisplayType = TagsPresenter.DisplayType.VIEW
    var selectedTags: Set<Tag> = setOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, positionClickedSubject: PublishSubject<Int>) =
            ClickableViewHolder(TagItemView.inflate(parent), positionClickedSubject)

    override fun onBindViewHolder(holder: ClickableViewHolder<TagItemView>, position: Int) {
        holder.view.setTag(getItem(position))
    }

    fun setTagSelected(tag: Tag, selected: Boolean) {
        if (selected) {
            selectedTags = selectedTags.plus(tag)
        } else {
            selectedTags = selectedTags.minus(tag)
        }
    }
}