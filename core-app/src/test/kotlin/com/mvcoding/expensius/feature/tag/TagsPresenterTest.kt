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
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.empty
import rx.Observable.just
import rx.lang.kotlin.PublishSubject

class TagsPresenterTest {
    val tagSelectedSubject = PublishSubject<Tag>()
    val tagCreateSubject = PublishSubject<Unit>()
    val tagMoveSubject = PublishSubject<TagsPresenter.TagMove>()

    val displayArchivedTagsSubject = PublishSubject<Unit>()
    val tagsProvider = mock<TagsProvider>()
    val view = mock<TagsPresenter.View>()

    @Before
    fun setUp() {
        whenever(view.onTagSelected()).thenReturn(tagSelectedSubject)
        whenever(view.onCreateTag()).thenReturn(tagCreateSubject)
        whenever(view.onDisplayArchivedTags()).thenReturn(displayArchivedTagsSubject)
        whenever(view.onTagMoved()).thenReturn(tagMoveSubject)
        whenever(tagsProvider.tags()).thenReturn(empty())
        whenever(tagsProvider.archivedTags()).thenReturn(empty())
    }

    @Test
    fun showsModelDisplayType() {
        val presenter = presenterWithModelDisplayTypeView()

        presenter.onViewAttached(view)

        verify(view).showModelDisplayType(VIEW_NOT_ARCHIVED)
    }

    @Test
    fun showsTags() {
        val presenter = presenterWithModelDisplayTypeView()
        val tags = listOf(aTag(), aTag(), aTag())
        whenever(tagsProvider.tags()).thenReturn(just(tags))

        presenter.onViewAttached(view)

        verify(view).showTags(tags)
    }

    @Test
    fun showsArchivedTags() {
        val presenter = presenterWithModelDisplayTypeArchived()
        val tags = listOf(aTag(), aTag(), aTag())
        whenever(tagsProvider.archivedTags()).thenReturn(just(tags))

        presenter.onViewAttached(view)

        verify(view).showTags(tags)
    }

    @Test
    fun displaysTagEditWhenSelectingATagAndDisplayTypeIsView() {
        val presenter = presenterWithModelDisplayTypeView()
        val tag = aTag()
        presenter.onViewAttached(view)

        selectTag(tag)

        verify(view).displayTagEdit(tag)
    }

    @Test
    fun displaysTagEditOnCreateTag() {
        val presenter = presenterWithModelDisplayTypeView()
        presenter.onViewAttached(view)

        createTag()

        verify(view).displayTagEdit(aNewTag())
    }

    @Test
    fun displaysArchivedTagsOnArchivedTags() {
        val presenter = presenterWithModelDisplayTypeView()
        presenter.onViewAttached(view)

        archivedTags()

        verify(view).displayArchivedTags()
    }

    @Test
    fun reordersAllTagsWhenOneTagWasMovedUp() {
        val tags = listOf(aTag(), aTag(), aTag(), aTag())
        val reorderedTags = listOf(tags[2].withOrder(0), tags[0].withOrder(1), tags[1].withOrder(2), tags[3].withOrder(3))
        whenever(tagsProvider.tags()).thenReturn(just(tags))
        presenterWithModelDisplayTypeView().onViewAttached(view)

        moveTag(2, 0)

        verify(tagsProvider).save(reorderedTags.toSet())
    }

    @Test
    fun reordersAllTagsWhenOneTagWasMovedDown() {
        val tags = listOf(aTag(), aTag(), aTag(), aTag())
        val reorderedTags = listOf(tags[1].withOrder(0), tags[2].withOrder(1), tags[0].withOrder(2), tags[3].withOrder(3))
        whenever(tagsProvider.tags()).thenReturn(just(tags))
        presenterWithModelDisplayTypeView().onViewAttached(view)

        moveTag(0, 2)

        verify(tagsProvider).save(reorderedTags.toSet())
    }

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)
    private fun archivedTags() = displayArchivedTagsSubject.onNext(Unit)
    private fun createTag() = tagCreateSubject.onNext(Unit)
    private fun moveTag(fromPosition: Int, toPosition: Int) = tagMoveSubject.onNext(TagsPresenter.TagMove(fromPosition, toPosition))
    private fun presenterWithModelDisplayTypeView() = TagsPresenter(tagsProvider, VIEW_NOT_ARCHIVED, rxSchedulers())
    private fun presenterWithModelDisplayTypeArchived() = TagsPresenter(tagsProvider, VIEW_ARCHIVED, rxSchedulers())
}

