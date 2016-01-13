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
import com.mvcoding.expensius.feature.tag.Tag.Companion.noTag
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.MULTI_CHOICE
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.Observable.empty
import rx.subjects.PublishSubject

class TagsPresenterTest {
    val tagSelectedSubject = PublishSubject.create<Tag>()
    val tagCreateSubject = PublishSubject.create<Unit>()
    val saveSubject = PublishSubject.create<Unit>()
    val archivedTagsSubject = PublishSubject.create<Unit>()
    val removeSubject = PublishSubject.create<Tag>()
    val commitSubject = PublishSubject.create<Unit>()
    val undoSubject = PublishSubject.create<Unit>()
    val tagsCache = mock(TagsProvider::class.java)
    val view = mock(TagsPresenter.View::class.java)

    @Before
    fun setUp() {
        given(view.onTagSelected()).willReturn(tagSelectedSubject)
        given(view.onCreateTag()).willReturn(tagCreateSubject)
        given(view.onSave()).willReturn(saveSubject)
        given(view.onArchivedTags()).willReturn(archivedTagsSubject)
        given(view.onRemoveTag()).willReturn(removeSubject)
        given(view.onCommitRemove()).willReturn(commitSubject)
        given(view.onUndoRemove()).willReturn(undoSubject)
        given(tagsCache.tags()).willReturn(empty())
        given(tagsCache.archivedTags()).willReturn(empty())
    }

    @Test
    fun setsDisplayType() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view).setDisplayType(VIEW)
    }

    @Test
    fun doesNotSetSelectedTagsWhenDisplayTypeIsView() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view, never()).setSelectedTags(anySetOf(Tag::class.java))
    }

    @Test
    fun setsSelectedTagsWhenDisplayTypeIsMultiChoice() {
        val selectedTags = setOf(aTag(), aTag())
        val presenter = TagsPresenter(tagsCache, MULTI_CHOICE, selectedTags)

        presenter.onAttachView(view)

        verify(view).setSelectedTags(selectedTags)
    }

    @Test
    fun showsSelectedTagWhenTagIsSelectedAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)

        verify(view).setTagSelected(tag, true)
    }

    @Test
    fun showsDeselectedTagWhenTagIsDeselectedAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)
        selectTag(tag)

        verify(view).setTagSelected(tag, false)
    }

    @Test
    fun showsTagsFromTagsCache() {
        val presenter = presenterWithDisplayTypeView()
        val tags = listOf(aTag(), aTag(), aTag())
        given(tagsCache.tags()).willReturn(Observable.just(tags))

        presenter.onAttachView(view)

        verify(view).setTags(tags)
    }

    @Test
    fun startsTagEditWhenSelectingATagAndDisplayTypeIsView() {
        val presenter = presenterWithDisplayTypeView()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)

        verify(view).startTagEdit(tag)
    }

    @Test
    fun canSelectMultipleTagsWhenDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag1 = aTag()
        val tag2 = aTag()
        presenter.onAttachView(view)

        selectTag(tag1)
        selectTag(tag2)

        presenter.onDetachView(view)
        presenter.onAttachView(view)
        verify(view).setSelectedTags(setOf(tag1, tag2))
    }

    @Test
    fun selectingAlreadySelectedItemDeselectsItWhenDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag1 = aTag()
        val tag2 = aTag()
        presenter.onAttachView(view)

        selectTag(tag1)
        selectTag(tag2)
        selectTag(tag2)

        presenter.onDetachView(view)
        presenter.onAttachView(view)
        verify(view).setSelectedTags(setOf(tag1))
    }

    @Test
    fun startsResultWithSelectedTagsWhenSavingAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag1 = aTag()
        val tag2 = aTag()
        presenter.onAttachView(view)
        selectTag(tag1)
        selectTag(tag2)

        save()

        verify(view).startResult(setOf(tag1, tag2))
    }

    @Test
    fun startsTagEditOnCreateTag() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        createTag()

        verify(view).startTagEdit(noTag)
    }

    @Test
    fun startsArchivedTagsOnArchivedTags() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        archivedTags()

        verify(view).startArchivedTags()
    }

    @Test
    fun removesTagAndShowsUndoForRemovedTagWhenRemovingATag() {
        val tag = aTag()
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        remove(tag)

        verify(view).removeTag(tag)
        verify(view).showUndoForRemovedTag()
    }

    @Test
    fun showsUndoForRemovedTagAfterReattach() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        remove(aTag())
        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showUndoForRemovedTag()
    }

    @Test
    fun doesNotShowUndoForRemovedTagAfterReattachWhenItWasCommitted() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        remove(aTag())
        commit()

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(1)).showUndoForRemovedTag()
    }

    @Test
    fun doesNotShowUndoForRemovedTagAfterReattachWhenItWasUndone() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        remove(aTag())
        undo()

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(1)).showUndoForRemovedTag()
    }

    @Test
    fun archivesATagOnRemoveCommitWhenDisplayTypeIsNotArchived() {
        val tag = aTag()
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        remove(tag)

        commit()

        verify(view).hideUndoForRemovedTag()
        verify(tagsCache).save(setOf(tag.withModelState(ARCHIVED)))
    }

    @Test
    fun archivesATagOnArchivingAnotherTagWhenDisplayTypeIsNotArchived() {
        val firstTag = aTag()
        val secondTag = aTag()
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        remove(firstTag)

        remove(secondTag)

        verify(view).hideUndoForRemovedTag()
        verify(tagsCache).save(setOf(firstTag.withModelState(ARCHIVED)))
    }

    @Test
    fun unArchivesATagOnRemoveCommitWhenDisplayTypeIsArchived() {
        val tag = aTag().withModelState(ARCHIVED)
        val presenter = presenterWithDisplayTypeArchived()
        presenter.onAttachView(view)
        remove(tag)

        commit()

        verify(view).hideUndoForRemovedTag()
        verify(tagsCache).save(setOf(tag.withModelState(NONE)))
    }

    @Test
    fun insertsTagBackOnUndoArchive() {
        val tag = aTag()
        val tags = listOf(aTag(), tag, aTag())
        given(tagsCache.tags()).willReturn(Observable.just(tags))
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        remove(tag)

        undo()

        verify(view).hideUndoForRemovedTag()
        verify(view).insertTag(tag, 1)
    }

    @Test
    fun showsArchivedTagsFromTagsCache() {
        val presenter = presenterWithDisplayTypeArchived()
        val tags = listOf(aTag(), aTag(), aTag())
        given(tagsCache.archivedTags()).willReturn(Observable.just(tags))

        presenter.onAttachView(view)

        verify(view).setTags(tags)
    }

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)

    private fun save() = saveSubject.onNext(Unit)

    private fun archivedTags() = archivedTagsSubject.onNext(Unit)

    private fun remove(tag: Tag) {
        removeSubject.onNext(tag)
    }

    private fun commit() {
        commitSubject.onNext(Unit)
    }

    private fun undo() {
        undoSubject.onNext(Unit)
    }

    private fun createTag() {
        tagCreateSubject.onNext(Unit)
    }

    private fun presenterWithDisplayTypeView() = TagsPresenter(tagsCache, VIEW)

    private fun presenterWithDisplayTypeMultiChoice() = TagsPresenter(tagsCache, MULTI_CHOICE)

    private fun presenterWithDisplayTypeArchived() = TagsPresenter(tagsCache, TagsPresenter.DisplayType.ARCHIVED)
}

