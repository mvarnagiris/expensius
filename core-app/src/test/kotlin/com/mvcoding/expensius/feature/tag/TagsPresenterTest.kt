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
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.rxSchedulers
import com.mvcoding.expensius.service.TagsService
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
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class TagsPresenterTest {
    val someTags = listOf(aTag(), aTag(), aTag().withModelState(ARCHIVED))
    val someOtherTags = listOf(aTag(), aTag().withModelState(ARCHIVED))

    val itemsSubject = BehaviorSubject(someTags)
    val tagSelectedSubject = PublishSubject<Tag>()
    val tagCreateSubject = PublishSubject<Unit>()
    val tagMoveSubject = PublishSubject<TagsPresenter.TagMove>()

    val displayArchivedTagsSubject = PublishSubject<Unit>()
    val tagsService: TagsService = mock()
    val tagsProvider = mock<TagsProvider>()
    val view: TagsPresenter.View = mock()
    val inOrder: InOrder = inOrder(view)

    @Before
    fun setUp() {
        //        whenever(view.onTagSelected()).thenReturn(tagSelectedSubject)
        //        whenever(view.onCreateTag()).thenReturn(tagCreateSubject)
        //        whenever(view.onDisplayArchivedTags()).thenReturn(displayArchivedTagsSubject)
        //        whenever(view.onTagMoved()).thenReturn(tagMoveSubject)
        whenever(tagsProvider.tags()).thenReturn(empty())
        whenever(tagsService.items()).thenReturn(itemsSubject)
        whenever(tagsService.addedItems()).thenReturn(empty())
        whenever(tagsService.changedItems()).thenReturn(empty())
        whenever(tagsService.removedItems()).thenReturn(empty())
        whenever(tagsService.movedItems()).thenReturn(empty())
        whenever(tagsProvider.archivedTags()).thenReturn(empty())
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
        inOrder.verify(view).showItems(someTags.filter { it.modelState == NONE }.sortedBy { it.order })
        verify(view, times(1)).showItems(any())
    }

    @Test
    fun showsArchivedTagsOnlyNce() {
        presenterWithModelDisplayTypeArchived().attach(view)
        tags(someOtherTags)

        inOrder.verify(view).showLoading()
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showItems(someTags.filter { it.modelState == ARCHIVED }.sortedBy { it.order })
        verify(view, times(1)).showItems(any())
    }

    //    @Test
    //    fun displaysTagEditWhenSelectingATagAndDisplayTypeIsView() {
    //        val presenter = presenterWithModelDisplayTypeView()
    //        val tag = aTag()
    //        presenter.attach(view)
    //
    //        selectTag(tag)
    //
    //        verify(view).displayTagEdit(tag)
    //    }

    //    @Test
    //    fun displaysTagEditOnCreateTag() {
    //        val presenter = presenterWithModelDisplayTypeView()
    //        presenter.attach(view)
    //
    //        createTag()
    //
    //        verify(view).displayTagEdit(aNewTag())
    //    }
    //
    //    @Test
    //    fun displaysArchivedTagsOnArchivedTags() {
    //        val presenter = presenterWithModelDisplayTypeView()
    //        presenter.attach(view)
    //
    //        archivedTags()
    //
    //        verify(view).displayArchivedTags()
    //    }

    //    @Test
    //    fun reordersAllTagsWhenOneTagWasMovedUp() {
    //        val tags = listOf(aTag(), aTag(), aTag(), aTag())
    //        val reorderedTags = listOf(tags[2].withOrder(0), tags[0].withOrder(1), tags[1].withOrder(2), tags[3].withOrder(3))
    //        whenever(tagsProvider.tags()).thenReturn(just(tags))
    //        presenterWithModelDisplayTypeView().attach(view)
    //
    //        moveTag(2, 0)
    //
    //        verify(tagsProvider).save(reorderedTags.toSet())
    //    }
    //
    //    @Test
    //    fun reordersAllTagsWhenOneTagWasMovedDown() {
    //        val tags = listOf(aTag(), aTag(), aTag(), aTag())
    //        val reorderedTags = listOf(tags[1].withOrder(0), tags[2].withOrder(1), tags[0].withOrder(2), tags[3].withOrder(3))
    //        whenever(tagsProvider.tags()).thenReturn(just(tags))
    //        presenterWithModelDisplayTypeView().attach(view)
    //
    //        moveTag(0, 2)
    //
    //        verify(tagsProvider).save(reorderedTags.toSet())
    //    }

    private fun tags(tags: List<Tag>) = itemsSubject.onNext(tags)

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)
    private fun archivedTags() = displayArchivedTagsSubject.onNext(Unit)
    private fun createTag() = tagCreateSubject.onNext(Unit)
    private fun moveTag(fromPosition: Int, toPosition: Int) = tagMoveSubject.onNext(TagsPresenter.TagMove(fromPosition, toPosition))
    private fun presenterWithModelDisplayTypeView() = TagsPresenter(VIEW_NOT_ARCHIVED, tagsService, rxSchedulers())
    private fun presenterWithModelDisplayTypeArchived() = TagsPresenter(VIEW_ARCHIVED, tagsService, rxSchedulers())
}

