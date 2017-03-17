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

import com.mvcoding.expensius.RxSchedulers
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.feature.LoadingView
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.RealtimeItemsView
import com.mvcoding.expensius.model.NullModels.noTag
import com.mvcoding.expensius.model.Order
import com.mvcoding.expensius.model.Tag
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.merge
import java.lang.Math.max
import java.lang.Math.min

class TagsPresenter(
        private val modelDisplayType: ModelDisplayType,
        private val tagsSource: DataSource<RealtimeData<Tag>>,
        private val schedulers: RxSchedulers) : Presenter<TagsPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)
        view.showLoading()
        tagsSource.data()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoading() }
                .subscribeUntilDetached {
                    when (it) {
                        is RealtimeData.AllItems -> view.showItems(it.items)
                        is RealtimeData.AddedItems -> view.showAddedItems(it.items, it.position)
                        is RealtimeData.ChangedItems -> view.showChangedItems(it.items, it.position)
                        is RealtimeData.RemovedItems -> view.showRemovedItems(it.items, it.position)
                        is RealtimeData.MovedItems -> view.showMovedItems(it.items, it.fromPosition, it.toPosition)
                    }
                }

        merge(view.tagSelects(), view.createTagRequests().map { noTag }).subscribeUntilDetached { view.displayTagEdit(it) }
//        view.archivedTagsRequests().subscribeUntilDetached { view.displayArchivedTags() }
    }

    private fun reorderTags(tagMove: TagMove, tags: List<Tag>): List<Tag> {
        val fromPosition = tagMove.fromPosition
        val toPosition = tagMove.toPosition
        val minPosition = min(fromPosition, toPosition)
        val maxPosition = max(fromPosition, toPosition)
        return tags.mapIndexed { position, tag ->
            when {
                position == fromPosition -> tag.copy(order = Order(toPosition))
                position >= minPosition && position <= maxPosition -> tag.copy(order = Order(position + if (fromPosition > toPosition) 1 else -1))
                else -> tag.copy(order = Order(position))
            }
        }
    }

    data class TagMove(val fromPosition: Int, val toPosition: Int)

    interface View : Presenter.View, RealtimeItemsView<Tag>, LoadingView {
        fun archivedTagsRequests(): Observable<Unit>
        fun tagMoves(): Observable<TagMove>
        fun tagSelects(): Observable<Tag>
        fun createTagRequests(): Observable<Unit>

        fun showModelDisplayType(modelDisplayType: ModelDisplayType)

        fun displayTagEdit(tag: Tag)
        fun displayArchivedTags()
    }
}
