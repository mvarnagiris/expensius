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

import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.tag.TagsPresenter.TagMove
import com.mvcoding.expensius.model.NullModels.noTag
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.model.withOrder
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.AddedItems
import com.mvcoding.expensius.service.ChangedItems
import com.mvcoding.expensius.service.MovedItem
import com.mvcoding.expensius.service.RemovedItems
import com.mvcoding.expensius.service.TagsService
import com.mvcoding.expensius.service.TagsWriteService
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.InOrder
import rx.Observable.empty
import rx.Observable.from
import rx.Observable.just
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class TagsPresenterTest {
    val someTags = listOf(aTag(), aTag(), aTag())
    val someOtherTags = listOf(aTag(), aTag())

    val itemsSubject = BehaviorSubject(someTags)
    val tagSelectsSubject = PublishSubject<Tag>()
    val createTagRequestsSubject = PublishSubject<Unit>()
    val tagMovesSubject = PublishSubject<TagMove>()

    val displayArchivedTagsSubject = PublishSubject<Unit>()
    val tagsService: TagsService = mock()
    val tagsWriteService: TagsWriteService = mock()
    val view: TagsPresenter.View = mock()
    val inOrder: InOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(view.tagSelects()).thenReturn(tagSelectsSubject)
        whenever(view.createTagRequests()).thenReturn(createTagRequestsSubject)
        whenever(view.archivedTagsRequests()).thenReturn(displayArchivedTagsSubject)
        whenever(view.tagMoves()).thenReturn(tagMovesSubject)
        whenever(tagsService.items()).thenReturn(itemsSubject)
        whenever(tagsService.addedItems()).thenReturn(empty())
        whenever(tagsService.changedItems()).thenReturn(empty())
        whenever(tagsService.removedItems()).thenReturn(empty())
        whenever(tagsService.movedItem()).thenReturn(empty())
        whenever(tagsWriteService.saveTags(any())).thenReturn(just(Unit))
    }

    @Test
    fun showsModelDisplayType() {
        presenterWithModelDisplayTypeView().attach(view)

        verify(view).showModelDisplayType(VIEW_NOT_ARCHIVED)
    }

    @Test
    fun showsInitialTagsOnlyOnce() {
        presenterWithModelDisplayTypeView().attach(view)
        tags(someOtherTags)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showItems(someTags)
        verify(view, times(1)).showItems(any())
    }

    @Test
    fun showsArchivedTagsOnlyOnce() {
        presenterWithModelDisplayTypeArchived().attach(view)
        tags(someOtherTags)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showItems(someTags)
        verify(view, times(1)).showItems(any())
    }

    @Test
    fun showsAddedTags() {
        val someItemsAdded = AddedItems(0, someTags)
        val someOtherItemsAdded = AddedItems(someTags.size, someOtherTags)
        whenever(tagsService.addedItems()).thenReturn(from(listOf(someItemsAdded, someOtherItemsAdded)))

        presenterWithModelDisplayTypeView().attach(view)

        inOrder.verify(view).showAddedItems(someItemsAdded.position, someItemsAdded.items)
        inOrder.verify(view).showAddedItems(someOtherItemsAdded.position, someOtherItemsAdded.items)
    }

    @Test
    fun showsChangedTags() {
        val someItemsChanged = ChangedItems(0, someTags)
        val someOtherItemsChanged = ChangedItems(someTags.size, someOtherTags)
        whenever(tagsService.changedItems()).thenReturn(from(listOf(someItemsChanged, someOtherItemsChanged)))

        presenterWithModelDisplayTypeView().attach(view)

        inOrder.verify(view).showChangedItems(someItemsChanged.position, someItemsChanged.items)
        inOrder.verify(view).showChangedItems(someOtherItemsChanged.position, someOtherItemsChanged.items)
    }

    @Test
    fun showsRemovedTags() {
        val someItemsRemoved = RemovedItems(0, someTags)
        val someOtherItemsRemoved = RemovedItems(someTags.size, someOtherTags)
        whenever(tagsService.removedItems()).thenReturn(from(listOf(someItemsRemoved, someOtherItemsRemoved)))

        presenterWithModelDisplayTypeView().attach(view)

        inOrder.verify(view).showRemovedItems(someItemsRemoved.position, someItemsRemoved.items)
        inOrder.verify(view).showRemovedItems(someOtherItemsRemoved.position, someOtherItemsRemoved.items)
    }

    @Test
    fun showsMovedTags() {
        val inOrder = inOrder(view)
        val someTag = aTag()
        val someOtherTag = aTag()
        val movedTags = listOf(MovedItem(0, 1, someTag), MovedItem(1, 2, someOtherTag))
        whenever(tagsService.movedItem()).thenReturn(from(movedTags))

        presenterWithModelDisplayTypeView().attach(view)

        inOrder.verify(view).showMovedItem(0, 1, someTag)
        inOrder.verify(view).showMovedItem(1, 2, someOtherTag)
    }

    @Test
    fun displaysTagEditWhenSelectingATagAndDisplayTypeIsView() {
        val tag = aTag()
        presenterWithModelDisplayTypeView().attach(view)

        selectTag(tag)

        verify(view).displayTagEdit(tag)
    }

    @Test
    fun displaysTagEditOnCreateTag() {
        presenterWithModelDisplayTypeView().attach(view)

        requestCreateTag()

        verify(view).displayTagEdit(noTag)
    }

    @Test
    fun displaysArchivedTags() {
        presenterWithModelDisplayTypeView().attach(view)

        requestArchivedTags()

        verify(view).displayArchivedTags()
    }

    @Test
    fun reordersAllTagsWhenOneTagWasMovedUp() {
        val tags = listOf(aTag(), aTag(), aTag(), aTag())
        val reorderedTags = listOf(tags[2].withOrder(0), tags[0].withOrder(1), tags[1].withOrder(2), tags[3].withOrder(3))
        whenever(tagsService.items()).thenReturn(just(tags))
        presenterWithModelDisplayTypeView().attach(view)

        moveTag(2, 0)

        verify(tagsWriteService).saveTags(reorderedTags.toSet())
    }

    @Test
    fun reordersAllTagsWhenOneTagWasMovedDown() {
        val tags = listOf(aTag(), aTag(), aTag(), aTag())
        val reorderedTags = listOf(tags[1].withOrder(0), tags[2].withOrder(1), tags[0].withOrder(2), tags[3].withOrder(3))
        whenever(tagsService.items()).thenReturn(just(tags))
        presenterWithModelDisplayTypeView().attach(view)

        moveTag(0, 2)

        verify(tagsWriteService).saveTags(reorderedTags.toSet())
    }

    private fun tags(tags: List<Tag>) = itemsSubject.onNext(tags)

    private fun selectTag(tagToSelect: Tag) = tagSelectsSubject.onNext(tagToSelect)
    private fun requestArchivedTags() = displayArchivedTagsSubject.onNext(Unit)
    private fun requestCreateTag() = createTagRequestsSubject.onNext(Unit)
    private fun moveTag(fromPosition: Int, toPosition: Int) = tagMovesSubject.onNext(TagMove(fromPosition, toPosition))
    private fun presenterWithModelDisplayTypeView() = TagsPresenter(VIEW_NOT_ARCHIVED, tagsService, tagsWriteService, rxSchedulers())
    private fun presenterWithModelDisplayTypeArchived() = TagsPresenter(VIEW_ARCHIVED, tagsService, tagsWriteService, rxSchedulers())
}

