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

class QuickTagsPresenterTest {
    //    val toggleSelectableTagSubject = PublishSubject<SelectableTag>()
    //    val selectedTagsUpdatedSubject = BehaviorSubject<Set<Tag>>(setOf())
    //    val defaultTags = listOf(aTag(), aTag())
    //    val defaultSelectableTags = defaultTags.map { SelectableTag(it, false) }
    //    val tagsCache = mock(TagsProvider::class.java)
    //    val view = mock(QuickTagsPresenter.View::class.java)
    //    val presenter = QuickTagsPresenter(tagsCache, rxSchedulers())
    //
    //    @Before
    //    fun setUp() {
    //        given(view.onSelectableTagToggled()).willReturn(toggleSelectableTagSubject)
    //        given(view.onShowSelectedTags()).willReturn(selectedTagsUpdatedSubject)
    //        given(tagsCache.tags()).willReturn(just(defaultTags))
    //    }
    //
    //    @Test
    //    fun initiallyShowsAllTagsUnselected() {
    //        presenter.attach(view)
    //
    //        verify(view).showSelectableTags(defaultSelectableTags)
    //    }
    //
    //    @Test
    //    fun showsAllSelectedTagsEvenIfTheyAreNotPartOfTagsProvider() {
    //        val extraTag = aTag()
    //        presenter.attach(view)
    //
    //        updateSelectedTags(setOf(defaultTags[1], extraTag))
    //
    //        verify(view).showSelectableTags(listOf(SelectableTag(defaultTags[0], false),
    //                SelectableTag(defaultTags[1], true),
    //                SelectableTag(extraTag, true)))
    //    }
    //
    //    @Test
    //    fun canSelectTag() {
    //        val selectableTag = defaultSelectableTags[0]
    //        presenter.attach(view)
    //
    //        toggleSelectableTag(selectableTag)
    //
    //        verify(view).showUpdatedSelectableTag(selectableTag, selectableTag.withSelected(true))
    //    }
    //
    //    @Test
    //    fun canDeselectTag() {
    //        val selectableTag = defaultSelectableTags[0]
    //        presenter.attach(view)
    //        toggleSelectableTag(selectableTag)
    //
    //        toggleSelectableTag(selectableTag.withSelected(true))
    //
    //        verify(view).showUpdatedSelectableTag(selectableTag.withSelected(true), selectableTag)
    //    }
    //
    //    @Test
    //    fun tagSelectionStateIsRestoredAfterReattach() {
    //        val selectableTag = defaultSelectableTags[0]
    //        val expectedSelectableTags = defaultSelectableTags.map { if (it == selectableTag) it.toggled() else it }
    //        presenter.attach(view)
    //        toggleSelectableTag(selectableTag)
    //
    //        presenter.detach(view)
    //        presenter.attach(view)
    //
    //        verify(view).showSelectableTags(expectedSelectableTags)
    //    }
    //
    //    private fun toggleSelectableTag(selectableTag: SelectableTag) {
    //        toggleSelectableTagSubject.onNext(selectableTag)
    //    }
    //
    //    private fun updateSelectedTags(selectedTags: Set<Tag>) {
    //        selectedTagsUpdatedSubject.onNext(selectedTags)
    //    }
}