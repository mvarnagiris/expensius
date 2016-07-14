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

import com.mvcoding.expensius.model.Transaction
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.model.aTransaction
import com.mvcoding.expensius.model.withTags
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.TagsService
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just

class QuickTagsPresenterTest {
    //    val toggleSelectableTagSubject = PublishSubject<SelectableTag>()
    //    val selectedTagsUpdatedSubject = BehaviorSubject<Set<Tag>>(setOf())
    val tags = listOf(aTag(), aTag())
    val transaction = aTransaction().withTags(setOf(tags.first()))
    val selectableTags = tags.map { SelectableTag(it, transaction.tags.contains(it)) }
    val tagsService: TagsService = mock()
    val view: QuickTagsPresenter.View = mock()
    val presenter = presenter()

    @Before
    fun setUp() {
        //        given(view.onSelectableTagToggled()).willReturn(toggleSelectableTagSubject)
        //        given(view.onShowSelectedTags()).willReturn(selectedTagsUpdatedSubject)

    }

    @Test
    fun initiallyShowsTagsSelectedForThatTransactionEvenIfTheyAreNotInTagsService() {
        val extraTag = aTag()
        val tags = listOf(aTag(), aTag())
        val transactionTags = setOf(extraTag, tags.first())
        val selectableTags = transactionTags.plus(tags).map { SelectableTag(it, transactionTags.contains(it)) }.sortedBy { it.tag.order }
        val transaction = aTransaction().withTags(transactionTags)
        whenever(tagsService.items()).thenReturn(just(tags))

        presenter(transaction).attach(view)

        verify(view).showSelectableTags(selectableTags)
    }

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

    private fun presenter(transaction: Transaction = aTransaction()) = QuickTagsPresenter(transaction, tagsService, rxSchedulers())
}