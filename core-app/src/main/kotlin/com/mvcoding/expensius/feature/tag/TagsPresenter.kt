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
import com.mvcoding.expensius.ModelState.NONE
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
    private var removedTag = noTag
    private var removedTagPosition = 0

    override fun onAttachView(view: View) {
        super.onAttachView(view)

        view.setDisplayType(displayType)
        if (displayType === MULTI_CHOICE) {
            view.setSelectedTags(selectedTags.orEmpty())
        }

        if (!removedTag.equals(noTag)) {
            view.showUndoForRemovedTag()
        }

        unsubscribeOnDetach(tags().doOnNext { tags = it }.subscribe { view.setTags(it) })
        unsubscribeOnDetach(merge(view.onTagSelected(), view.onCreateTag().map { noTag }).subscribe { selectTag(view, it) })
        unsubscribeOnDetach(view.onSave().map { selectedTags }.subscribe { view.startResult(it) })
        unsubscribeOnDetach(view.onArchivedTags().subscribe { view.startArchivedTags() })
        unsubscribeOnDetach(view.onRemoveTag().doOnNext { commitRemove(view) }.subscribe { remove(view, it) })
        unsubscribeOnDetach(view.onCommitRemove().subscribe { commitRemove(view) })
        unsubscribeOnDetach(view.onUndoRemove().subscribe { undoRemove(view) })
    }

    private fun tags() = if (displayType == DisplayType.ARCHIVED) tagsCache.archivedTags() else tagsCache.tags()

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

    private fun remove(view: View, tag: Tag) {
        view.removeTag(tag)
        view.showUndoForRemovedTag()
        removedTag = tag;
        removedTagPosition = tags.indexOf(tag)
    }

    private fun commitRemove(view: View) {
        if (removedTag.equals(noTag)) return
        tagsCache.save(setOf(removedTag.withModelState(if (displayType == DisplayType.ARCHIVED) NONE else ARCHIVED)))
        removedTag = noTag
        view.hideUndoForRemovedTag()
    }

    private fun undoRemove(view: View) {
        if (removedTag.equals(noTag)) return
        view.insertTag(removedTag, removedTagPosition)
        removedTag = noTag
        view.hideUndoForRemovedTag()
    }

    interface View : Presenter.View {
        fun setDisplayType(displayType: DisplayType)
        fun setSelectedTags(selectedTags: Set<Tag>)
        fun setTagSelected(tag: Tag, selected: Boolean)
        fun setTags(tags: List<Tag>)
        fun removeTag(tag: Tag)
        fun insertTag(tag: Tag, position: Int)
        fun showUndoForRemovedTag()
        fun hideUndoForRemovedTag()

        fun onTagSelected(): Observable<Tag>
        fun onCreateTag(): Observable<Unit>
        fun onSave(): Observable<Unit>
        fun onArchivedTags(): Observable<Unit>
        fun onRemoveTag(): Observable<Tag>
        fun onCommitRemove(): Observable<Unit>
        fun onUndoRemove(): Observable<Unit>

        fun startTagEdit(tag: Tag)
        fun startResult(tags: Set<Tag>)
        fun startArchivedTags()
    }

    enum class DisplayType {
        VIEW, MULTI_CHOICE, ARCHIVED
    }
}
