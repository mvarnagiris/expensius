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

import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NON_ARCHIVED
import com.mvcoding.expensius.feature.Presenter
import rx.Observable
import rx.Observable.merge

class TagsPresenter(
        private val tagsProvider: TagsProvider,
        private val modelDisplayType: ModelDisplayType = VIEW_NON_ARCHIVED) : Presenter<TagsPresenter.View>() {

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.showModelDisplayType(modelDisplayType)

        unsubscribeOnDetach(tags().subscribe { view.showTags(it) })
        unsubscribeOnDetach(merge(view.onTagSelected(), view.onCreateTag().map { Tag() }).subscribe { view.displayTagEdit(it) })
        unsubscribeOnDetach(view.onDisplayArchivedTags().subscribe { view.displayArchivedTags() })
    }

    private fun tags() = if (modelDisplayType == VIEW_ARCHIVED) tagsProvider.archivedTags() else tagsProvider.tags()

    interface View : Presenter.View {
        fun showModelDisplayType(modelDisplayType: ModelDisplayType)
        fun showTags(tags: List<Tag>)

        fun onTagSelected(): Observable<Tag>
        fun onCreateTag(): Observable<Unit>
        fun onDisplayArchivedTags(): Observable<Unit>

        fun displayTagEdit(tag: Tag)
        fun displayArchivedTags()
    }
}
