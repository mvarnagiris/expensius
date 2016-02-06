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

import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.tag.Tag.Companion.noTag
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW_ARCHIVED
import rx.Observable
import rx.Observable.merge

class TagsPresenter(
        private val tagsProvider: TagsProvider,
        private val displayType: TagsPresenter.DisplayType = VIEW) : Presenter<TagsPresenter.View>() {

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.showDisplayType(displayType)

        unsubscribeOnDetach(tags().subscribe { view.showTags(it) })
        unsubscribeOnDetach(merge(view.onTagSelected(), view.onCreateTag().map { noTag }).subscribe { view.displayTagEdit(it) })
        unsubscribeOnDetach(view.onDisplayArchivedTags().subscribe { view.displayArchivedTags() })
    }

    private fun tags() = if (displayType == VIEW_ARCHIVED) tagsProvider.archivedTags() else tagsProvider.tags()

    interface View : Presenter.View {
        fun showDisplayType(displayType: DisplayType)
        fun showTags(tags: List<Tag>)

        fun onTagSelected(): Observable<Tag>
        fun onCreateTag(): Observable<Unit>
        fun onDisplayArchivedTags(): Observable<Unit>

        fun displayTagEdit(tag: Tag)
        fun displayArchivedTags()
    }

    enum class DisplayType {
        VIEW, VIEW_ARCHIVED
    }
}
