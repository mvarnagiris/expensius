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
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.model.Tag
import com.mvcoding.mvp.Presenter
import rx.Observable
import rx.Observable.merge
import java.lang.Math.max
import java.lang.Math.min

class TagsPresenter(
        private val tagsProvider: TagsProvider,
        private val modelDisplayType: ModelDisplayType,
        private val schedulers: RxSchedulers) : Presenter<TagsPresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showModelDisplayType(modelDisplayType)

        val tags = if (modelDisplayType == VIEW_ARCHIVED) tagsProvider.archivedTags().cache() else tagsProvider.tags().cache()

        unsubscribeOnDetach(view.onTagMoved()
                .withLatestFrom(tags, { tagMove, tags -> reorderTags(tagMove, tags) })
                .subscribe { tagsProvider.save(it.toSet()) })
        unsubscribeOnDetach(tags.subscribeOn(schedulers.io).observeOn(schedulers.main).subscribe { view.showTags(it) })
        unsubscribeOnDetach(merge(view.onTagSelected(), view.onCreateTag().map { Tag() }).subscribe { view.displayTagEdit(it) })
        unsubscribeOnDetach(view.onDisplayArchivedTags().subscribe { view.displayArchivedTags() })
    }

    private fun reorderTags(tagMove: TagMove, tags: List<Tag>): List<Tag> {
        val fromPosition = tagMove.fromPosition
        val toPosition = tagMove.toPosition
        val minPosition = min(fromPosition, toPosition)
        val maxPosition = max(fromPosition, toPosition)
        return tags.mapIndexed { position, tag ->
            when {
                position == fromPosition -> tag.withOrder(toPosition)
                position >= minPosition && position <= maxPosition -> tag.withOrder(position + if (fromPosition > toPosition) 1 else -1)
                else -> tag.withOrder(position)
            }
        }
    }

    data class TagMove(val fromPosition: Int, val toPosition: Int)

    interface View : Presenter.View {
        fun showModelDisplayType(modelDisplayType: ModelDisplayType)
        fun showTags(tags: List<Tag>)

        fun onTagSelected(): Observable<Tag>
        fun onTagMoved(): Observable<TagMove>
        fun onCreateTag(): Observable<Unit>
        fun onDisplayArchivedTags(): Observable<Unit>

        fun displayTagEdit(tag: Tag)
        fun displayArchivedTags()
    }
}
