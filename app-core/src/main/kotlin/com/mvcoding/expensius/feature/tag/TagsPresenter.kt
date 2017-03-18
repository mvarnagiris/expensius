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
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.feature.LoadingView
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
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
        private val tagsWriter: DataWriter<Set<Tag>>,
        private val schedulers: RxSchedulers) : Presenter<TagsPresenter.View>() {

    private var ignoreNextMove: TagMove? = null

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)
        view.showLoading()
        if (modelDisplayType == VIEW_NOT_ARCHIVED) view.showArchivedTagsRequest()

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
                        is RealtimeData.MovedItems -> /*if (!shouldIgnoreMove(it))*/ view.showMovedItems(it.items, it.fromPosition, it.toPosition)
                    }
                }

        merge(view.tagSelects(), view.createTagRequests().map { noTag }).subscribeUntilDetached { view.displayTagEdit(it) }
        view.archivedTagsRequests().subscribeUntilDetached { view.displayArchivedTags() }
        view.tagMoves()
                .doOnNext { ignoreNextMove = it }
                .map { reorderTags(it) }
                .filter { it.isNotEmpty() }
                .subscribeUntilDetached { tagsWriter.write(it.toSet()) }
    }

    private fun reorderTags(tagMove: TagMove): List<Tag> {
        val fromPosition = tagMove.fromPosition
        val toPosition = tagMove.toPosition
        if (fromPosition == toPosition) return emptyList()

        val minPosition = min(fromPosition, toPosition)
        val maxPosition = max(fromPosition, toPosition)
        return tagMove.currentTags
                .subList(minPosition, maxPosition + 1)
                .map {
                    when (it.order.value) {
                        fromPosition -> it.copy(order = Order(toPosition))
                        in minPosition..maxPosition -> it.copy(order = Order(it.order.value + if (fromPosition > toPosition) 1 else -1))
                        else -> it.copy(order = Order(it.order.value))
                    }
                }
    }

    private fun shouldIgnoreMove(movedItems: RealtimeData.MovedItems<Tag>): Boolean {
        val ignoreNextMove = this.ignoreNextMove
        this.ignoreNextMove = null
        return ignoreNextMove != null && ignoreNextMove.fromPosition == movedItems.fromPosition && ignoreNextMove.toPosition == movedItems.toPosition
    }

    data class TagMove(val currentTags: List<Tag>, val fromPosition: Int, val toPosition: Int)

    interface View : Presenter.View, RealtimeItemsView<Tag>, LoadingView {
        fun archivedTagsRequests(): Observable<Unit>
        fun tagMoves(): Observable<TagMove>
        fun tagSelects(): Observable<Tag>
        fun createTagRequests(): Observable<Unit>

        fun showModelDisplayType(modelDisplayType: ModelDisplayType)
        fun showArchivedTagsRequest()

        fun displayTagEdit(tag: Tag)
        fun displayArchivedTags()
    }
}
