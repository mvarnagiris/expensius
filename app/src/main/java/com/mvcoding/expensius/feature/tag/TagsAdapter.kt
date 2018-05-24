/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

//class TagsAdapter : BaseClickableAdapter<Tag, ViewHolder>() {
//
//    var showArchivedTagsRequest: Boolean = false
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }
//
//    fun isTagPosition(position: Int) = !showArchivedTagsRequest || position < itemCount - 1
//    override fun getItemCount(): Int = super.getItemCount() + if (showArchivedTagsRequest && super.getItemCount() > 0) 1 else 0
//    override fun getItemViewType(position: Int): Int = if (isTagPosition(position)) R.layout.item_view_tag else R.layout.item_view_archived
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, clickSubject: PublishSubject<Pair<View, Int>>) =
//            ClickableViewHolder(parent.inflate(viewType), clickSubject)
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (holder.itemView is TagItemView) holder.itemView.setTag(getItem(position))
//    }
//}