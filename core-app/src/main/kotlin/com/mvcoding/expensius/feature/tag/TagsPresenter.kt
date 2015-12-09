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

import com.mvcoding.expensius.ModelState.ARCHIVED
import com.mvcoding.expensius.feature.Presenter
import com.mvcoding.expensius.feature.tag.Tag.Companion.noTag
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.MULTI_CHOICE
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW
import rx.Observable
import rx.Observable.merge

class TagsPresenter(
        private val tagsCache: TagsCache,
        private val displayType: TagsPresenter.DisplayType = VIEW,
        private var selectedTags: Set<Tag> = setOf()) : Presenter<TagsPresenter.View>() {
    private var tags = listOf<Tag>()
    private var archivedTag = noTag
    private var archivedTagPosition = 0

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setDisplayType(displayType)
        if (displayType === MULTI_CHOICE) {
            view.setSelectedTags(selectedTags.orEmpty())
        }

        if (!archivedTag.equals(noTag)) {
            view.showUndoForArchivedTag()
        }

        unsubscribeOnDetach(tagsCache.tags().doOnNext { tags = it }.subscribe { view.setTags(it) })
        unsubscribeOnDetach(merge(view.onTagSelected(), view.onCreateTag().map { noTag }).subscribe { selectTag(view, it) })
        unsubscribeOnDetach(view.onSave().map { selectedTags }.subscribe { view.startResult(it) })
        unsubscribeOnDetach(view.onArchiveTag().subscribe { archive(view, it) })
        unsubscribeOnDetach(view.onCommitArchive().subscribe { commitArchive(view) })
        unsubscribeOnDetach(view.onUndoArchive().subscribe { undoArchive(view) })
    }

    private fun selectTag(view: View, tag: Tag) {
        when (displayType) {
            VIEW -> view.startTagEdit(tag)
            MULTI_CHOICE -> selectedTags = if (selectedTags.contains(tag)) {
                view.setTagSelected(tag, false)
                selectedTags.minus(tag)
            } else {
                view.setTagSelected(tag, true)
                selectedTags.plus(tag)
            }
            else -> throw IllegalArgumentException("Display type $displayType is not supported.")
        }
    }

    private fun archive(view: View, tag: Tag) {
        view.removeTag(tag)
        view.showUndoForArchivedTag()
        archivedTag = tag;
        archivedTagPosition = tags.indexOf(tag)
    }

    private fun commitArchive(view: View) {
        if (archivedTag.equals(noTag)) return
        tagsCache.save(setOf(archivedTag.withModelState(ARCHIVED)))
        archivedTag = noTag
        view.hideUndoForArchivedTag()
    }

    private fun undoArchive(view: View) {
        if (archivedTag.equals(noTag)) return
        view.insertTag(archivedTag, archivedTagPosition)
        archivedTag = noTag
        view.hideUndoForArchivedTag()
    }

    interface View : Presenter.View {
        fun setDisplayType(displayType: DisplayType)
        fun setSelectedTags(selectedTags: Set<Tag>)
        fun setTagSelected(tag: Tag, selected: Boolean)
        fun setTags(tags: List<Tag>)
        fun removeTag(tag: Tag)
        fun insertTag(tag: Tag, position: Int)
        fun showUndoForArchivedTag()
        fun hideUndoForArchivedTag()

        fun onTagSelected(): Observable<Tag>
        fun onCreateTag(): Observable<Unit>
        fun onSave(): Observable<Unit>
        fun onArchiveTag(): Observable<Tag>
        fun onCommitArchive(): Observable<Unit>
        fun onUndoArchive(): Observable<Unit>

        fun startTagEdit(tag: Tag)
        fun startResult(tags: Set<Tag>)
    }

    enum class DisplayType {
        VIEW, MULTI_CHOICE
    }
}
