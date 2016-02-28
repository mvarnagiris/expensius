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
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable.just
import rx.lang.kotlin.BehaviourSubject
import rx.lang.kotlin.PublishSubject

class QuickTagsPresenterTest {
    val toggleSelectableTagSubject = PublishSubject<SelectableTag>()
    val selectedTagsUpdatedSubject = BehaviourSubject<Set<Tag>>(setOf())
    val defaultTags = listOf(aTag(), aTag())
    val defaultSelectableTags = defaultTags.map { SelectableTag(it, false) }
    val tagsCache = mock(TagsProvider::class.java)
    val view = mock(QuickTagsPresenter.View::class.java)
    val presenter = QuickTagsPresenter(tagsCache)

    @Before
    fun setUp() {
        given(view.onSelectableTagToggled()).willReturn(toggleSelectableTagSubject)
        given(view.onShowSelectedTags()).willReturn(selectedTagsUpdatedSubject)
        given(tagsCache.tags()).willReturn(just(defaultTags))
    }

    @Test
    fun initiallyShowsAllTagsUnselected() {
        presenter.onViewAttached(view)

        verify(view).showSelectableTags(defaultSelectableTags)
    }

    @Test
    fun showsAllSelectedTagsEvenIfTheyAreNotPartOfTagsProvider() {
        val extraTag = aTag()
        presenter.onViewAttached(view)

        updateSelectedTags(setOf(defaultTags[1], extraTag))

        verify(view).showSelectableTags(listOf(SelectableTag(defaultTags[0], false),
                                               SelectableTag(defaultTags[1], true),
                                               SelectableTag(extraTag, true)))
    }

    @Test
    fun canSelectTag() {
        val selectableTag = defaultSelectableTags[0]
        presenter.onViewAttached(view)

        toggleSelectableTag(selectableTag)

        verify(view).showUpdatedSelectableTag(selectableTag, selectableTag.withSelected(true))
    }

    @Test
    fun canDeselectTag() {
        val selectableTag = defaultSelectableTags[0]
        presenter.onViewAttached(view)
        toggleSelectableTag(selectableTag)

        toggleSelectableTag(selectableTag.withSelected(true))

        verify(view).showUpdatedSelectableTag(selectableTag.withSelected(true), selectableTag)
    }

    @Test
    fun tagSelectionStateIsRestoredAfterReattach() {
        val selectableTag = defaultSelectableTags[0]
        val expectedSelectableTags = defaultSelectableTags.map { if (it == selectableTag) it.toggled() else it }
        presenter.onViewAttached(view)
        toggleSelectableTag(selectableTag)

        presenter.onViewDetached(view)
        presenter.onViewAttached(view)

        verify(view).showSelectableTags(expectedSelectableTags)
    }

    private fun toggleSelectableTag(selectableTag: SelectableTag) {
        toggleSelectableTagSubject.onNext(selectableTag)
    }

    private fun updateSelectedTags(selectedTags: Set<Tag>) {
        selectedTagsUpdatedSubject.onNext(selectedTags)
    }
}