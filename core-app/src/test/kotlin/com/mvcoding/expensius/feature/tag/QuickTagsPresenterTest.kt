/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.TagsService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class QuickTagsPresenterTest {
    val toggleSelectableTagSubject = PublishSubject<SelectableTag>()
    val selectedTagsUpdatedSubject = BehaviorSubject<Set<Tag>>(setOf())
    val defaultTags = listOf(aTag(), aTag())
    val defaultSelectableTags = defaultTags.map { SelectableTag(it, false) }
    val tagsService: TagsService = mock()
    val view: QuickTagsPresenter.View = mock()
    val presenter = QuickTagsPresenter(tagsService, rxSchedulers())

    @Before
    fun setUp() {
        whenever(view.selectableTagToggles()).thenReturn(toggleSelectableTagSubject)
        whenever(view.selectedTagsUpdates()).thenReturn(selectedTagsUpdatedSubject)
        whenever(tagsService.items()).thenReturn(just(defaultTags))
    }

    @Test
    fun initiallyShowsAllTagsUnselected() {
        presenter.attach(view)

        verify(view).showSelectableTags(defaultSelectableTags)
    }

    @Test
    fun showsAllSelectedTagsEvenIfTheyAreNotPartOfTagsProvider() {
        val extraTag = aTag()
        presenter.attach(view)

        updateSelectedTags(setOf(defaultTags[1], extraTag))

        verify(view).showSelectableTags(listOf(
                SelectableTag(defaultTags[0], false),
                SelectableTag(defaultTags[1], true),
                SelectableTag(extraTag, true))
                .sortedBy { it.tag.order })
    }

    @Test
    fun canSelectTag() {
        val selectableTag = defaultSelectableTags[0]
        presenter.attach(view)

        toggleSelectableTag(selectableTag)

        verify(view).showUpdatedSelectableTag(selectableTag, selectableTag.withSelected(true))
    }

    @Test
    fun canDeselectTag() {
        val selectableTag = defaultSelectableTags[0]
        presenter.attach(view)
        toggleSelectableTag(selectableTag)

        toggleSelectableTag(selectableTag.withSelected(true))

        verify(view).showUpdatedSelectableTag(selectableTag.withSelected(true), selectableTag)
    }

    @Test
    fun tagSelectionStateIsRestoredAfterReattach() {
        val selectableTag = defaultSelectableTags[0]
        val expectedSelectableTags = defaultSelectableTags.map { if (it == selectableTag) it.toggled() else it }.sortedBy { it.tag.order }
        presenter.attach(view)
        toggleSelectableTag(selectableTag)

        presenter.detach(view)
        presenter.attach(view)

        verify(view).showSelectableTags(expectedSelectableTags)
    }

    @Test
    fun closesTagsServiceOnDestroy() {
        presenter.onDestroy()

        verify(tagsService).close()
    }

    private fun toggleSelectableTag(selectableTag: SelectableTag) = toggleSelectableTagSubject.onNext(selectableTag)
    private fun updateSelectedTags(selectedTags: Set<Tag>) = selectedTagsUpdatedSubject.onNext(selectedTags)
    private fun SelectableTag.withSelected(selected: Boolean) = copy(isSelected = selected)
}