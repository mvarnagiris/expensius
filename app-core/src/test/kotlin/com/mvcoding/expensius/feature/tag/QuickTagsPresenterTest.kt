/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.rxSchedulers
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
    val defaultSelectableTags = defaultTags.map { SelectableTag(it, false) }.sortedBy { it.tag.order }
    val tagsSource = mock<DataSource<List<Tag>>>()
    val view = mock<QuickTagsPresenter.View>()
    val presenter = QuickTagsPresenter(tagsSource, rxSchedulers())

    @Before
    fun setUp() {
        whenever(view.selectableTagToggles()).thenReturn(toggleSelectableTagSubject)
        whenever(view.selectedTagsUpdates()).thenReturn(selectedTagsUpdatedSubject)
        whenever(tagsSource.data()).thenReturn(just(defaultTags))
    }

    @Test
    fun `initially shows all tags unselected`() {
        presenter.attach(view)

        verify(view).showSelectableTags(defaultSelectableTags)
    }

    @Test
    fun `shows all selected tags even if they are not part of tags provider`() {
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
    fun `can select tag`() {
        val selectableTag = defaultSelectableTags[0]
        presenter.attach(view)

        toggleSelectableTag(selectableTag)

        verify(view).showUpdatedSelectableTag(selectableTag, selectableTag.withSelected(true))
    }

    @Test
    fun `can deselect tag`() {
        val selectableTag = defaultSelectableTags[0]
        presenter.attach(view)
        toggleSelectableTag(selectableTag)

        toggleSelectableTag(selectableTag.withSelected(true))

        verify(view).showUpdatedSelectableTag(selectableTag.withSelected(true), selectableTag)
    }

    @Test
    fun `tag selection state is restored after reattach`() {
        val selectableTag = defaultSelectableTags[0]
        val expectedSelectableTags = defaultSelectableTags.map { if (it == selectableTag) it.toggled() else it }.sortedBy { it.tag.order }
        presenter.attach(view)
        toggleSelectableTag(selectableTag)

        presenter.detach(view)
        presenter.attach(view)

        verify(view).showSelectableTags(expectedSelectableTags)
    }

    private fun toggleSelectableTag(selectableTag: SelectableTag) = toggleSelectableTagSubject.onNext(selectableTag)
    private fun updateSelectedTags(selectedTags: Set<Tag>) = selectedTagsUpdatedSubject.onNext(selectedTags)
    private fun SelectableTag.withSelected(selected: Boolean) = copy(isSelected = selected)
}