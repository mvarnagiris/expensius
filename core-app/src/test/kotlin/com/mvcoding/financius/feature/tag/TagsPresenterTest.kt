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

import com.mvcoding.financius.feature.tag.TagsPresenter.BatchOperationMode.HIDDEN
import com.mvcoding.financius.feature.tag.TagsPresenter.BatchOperationMode.VISIBLE
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.subjects.PublishSubject

class TagsPresenterTest {
    val batchOperationsSubject = PublishSubject.create<TagsPresenter.BatchOperationMode>()
    val tagSelectedSubject = PublishSubject.create<Tag>()
    val tagCreateSubject = PublishSubject.create<Unit>()
    val saveSubject = PublishSubject.create<Unit>()
    val archiveSubject = PublishSubject.create<Unit>()
    val tagsCache = mock(TagsCache::class.java)
    val view = mock(TagsPresenter.View::class.java)

    @Before
    fun setUp() {
        given(view.onBatchOperationModeChanged()).willReturn(batchOperationsSubject)
        given(view.onTagSelected()).willReturn(tagSelectedSubject)
        given(view.onCreateTag()).willReturn(tagCreateSubject)
        given(view.onSave()).willReturn(saveSubject)
        given(view.onArchive()).willReturn(archiveSubject)
        given(tagsCache.tags()).willReturn(Observable.empty())
    }

    @Test
    fun setsDisplayType() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view).setDisplayType(TagsPresenter.DisplayType.VIEW)
    }

    @Test
    fun doesNotShowSelectedTagsWhenDisplayTypeIsView() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view, never()).showSelectedTags(anySetOf(Tag::class.java))
    }

    @Test
    fun showsSelectedTagsWhenDisplayTypeIsMultiChoice() {
        val selectedTags = setOf(aTag(), aTag())
        val presenter = TagsPresenter(tagsCache, TagsPresenter.DisplayType.MULTI_CHOICE, selectedTags)
        presenter.onAttachView(view)

        verify(view).showSelectedTags(selectedTags)
    }

    @Test
    fun showsSelectedTagWhenTagIsSelectedAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)

        verify(view).showTagSelected(tag, true)
    }

    @Test
    fun showsDeselectedTagWhenTagIsDeselectedAndDisplayTypeIsMultiChoice() {
        val presenter = presenterWithDisplayTypeMultiChoice()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)
        selectTag(tag)

        verify(view).showTagSelected(tag, false)
    }

    @Test
    fun showsTagsFromTagsRepository() {
        val presenter = presenterWithDisplayTypeView()
        val tags = listOf(aTag(), aTag(), aTag())
        given(tagsCache.tags()).willReturn(Observable.just(tags))

        presenter.onAttachView(view)

        verify(view).showTags(tags)
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
        verify(view).showSelectedTags(setOf(tag1, tag2))
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
        verify(view).showSelectedTags(setOf(tag1))
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

        verify(view).startTagEdit(Tag.noTag)
    }

    @Test
    fun initiallyDoNoShowBatchOperationMode() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view).showBatchOperationMode(HIDDEN)
    }

    @Test
    fun showsBatchOperationModeWhenStartingBatchOperations() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        setBatchOperationMode(VISIBLE)

        verify(view).showBatchOperationMode(VISIBLE)
    }

    @Test
    fun showsBatchOperationModeAfterReattachWhenItWasAlreadyStarted() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        setBatchOperationMode(VISIBLE)

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showBatchOperationMode(VISIBLE)
    }

    @Test
    fun hidesBatchOperationModeWhenItWasStartedAndNowIsBeingHidden() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        setBatchOperationMode(VISIBLE)

        setBatchOperationMode(HIDDEN)

        verify(view, times(2)).showBatchOperationMode(HIDDEN)
    }

    @Test
    fun canSelectMultipleTagsInBatchOperationMode() {
        val tag1 = aTag();
        val tag2 = aTag();
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        setBatchOperationMode(VISIBLE)

        selectTag(tag1)
        selectTag(tag2)

        verify(view).showTagSelected(tag1, true)
        verify(view).showTagSelected(tag2, true)
    }

    @Test
    fun showsSelectedTagsInBatchOperationModeAfterReattach() {
        val tag1 = aTag();
        val tag2 = aTag();
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        setBatchOperationMode(VISIBLE)
        selectTag(tag1)
        selectTag(tag2)

        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view).showSelectedTags(setOf(tag1, tag2))
    }

    @Test
    fun archivesSelectedTagsInBatchOperationMode() {
        val tag1 = aTag();
        val tag2 = aTag();
        val archivedTags = setOf(tag1, tag2)
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)
        setBatchOperationMode(VISIBLE)
        selectTag(tag1)
        selectTag(tag2)

        archive()

        verify(tagsCache).archive(archivedTags)
        verify(view).remove(archivedTags)
        verify(view, times(2)).showBatchOperationMode(HIDDEN)
    }

    private fun archive() {
        archiveSubject.onNext(Unit)
    }

    private fun setBatchOperationMode(batchOperationMode: TagsPresenter.BatchOperationMode) {
        batchOperationsSubject.onNext(batchOperationMode)
    }

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)

    private fun save() = saveSubject.onNext(Unit)

    private fun createTag() {
        tagCreateSubject.onNext(Unit)
    }

    private fun presenterWithDisplayTypeView() = TagsPresenter(tagsCache, TagsPresenter.DisplayType.VIEW)

    private fun presenterWithDisplayTypeMultiChoice() = TagsPresenter(tagsCache, TagsPresenter.DisplayType.MULTI_CHOICE)
}

