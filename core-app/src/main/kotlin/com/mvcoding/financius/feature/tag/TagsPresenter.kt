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

package com.mvcoding.financius.feature.tag

import com.mvcoding.financius.feature.Presenter
import rx.Observable

class TagsPresenter(
        private val tagsRepository: TagsRepository,
        private val displayType: TagsPresenter.DisplayType = TagsPresenter.DisplayType.VIEW,
        private var selectedTags: Set<Tag> = setOf()) : Presenter<TagsPresenter.View>() {

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setDisplayType(displayType)
        if (displayType === DisplayType.MULTI_CHOICE) {
            view.showSelectedTags(selectedTags.orEmpty())
        }

        unsubscribeOnDetach(tagsRepository.observeTags().subscribe { view.showTags(it) })
        unsubscribeOnDetach(view.onTagSelected().subscribe { selectTag(view, it) })
        unsubscribeOnDetach(view.onSave().map { selectedTags }.subscribe { view.startResult(it) })
    }

    private fun selectTag(view: View, tag: Tag) {
        when (displayType) {
            DisplayType.VIEW -> view.startTagEdit(tag)
            DisplayType.MULTI_CHOICE -> selectedTags = if (selectedTags.contains(tag)) {
                view.showTagSelected(tag, false)
                selectedTags.minus(tag)
            } else {
                view.showTagSelected(tag, true)
                selectedTags.plus(tag)
            }
            else -> throw IllegalArgumentException("Display type $displayType is not supported.")
        }
    }

    interface View : Presenter.View {
        fun onTagSelected(): Observable<Tag>
        fun onSave(): Observable<Unit>
        fun setDisplayType(displayType: DisplayType)
        fun showSelectedTags(selectedTags: Set<Tag>)
        fun showTagSelected(tag: Tag, selected: Boolean)
        fun showTags(tags: List<Tag>)
        fun startTagEdit(tag: Tag)
        fun startResult(tag: Set<Tag>)
    }

    enum class DisplayType {
        VIEW, MULTI_CHOICE
    }
}