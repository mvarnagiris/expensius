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

import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW_ARCHIVED
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.Observable.empty
import rx.subjects.PublishSubject

class TagsPresenterTest {
    val tagSelectedSubject = PublishSubject.create<Tag>()
    val tagCreateSubject = PublishSubject.create<Unit>()
    val displayArchivedTagsSubject = PublishSubject.create<Unit>()
    val tagsCache = mock(TagsProvider::class.java)
    val view = mock(TagsPresenter.View::class.java)

    @Before
    fun setUp() {
        given(view.onTagSelected()).willReturn(tagSelectedSubject)
        given(view.onCreateTag()).willReturn(tagCreateSubject)
        given(view.onDisplayArchivedTags()).willReturn(displayArchivedTagsSubject)
        given(tagsCache.tags()).willReturn(empty())
        given(tagsCache.archivedTags()).willReturn(empty())
    }

    @Test
    fun setsDisplayType() {
        val presenter = presenterWithDisplayTypeView()

        presenter.onAttachView(view)

        verify(view).showDisplayType(VIEW)
    }

    @Test
    fun showsTagsFromTagsCache() {
        val presenter = presenterWithDisplayTypeView()
        val tags = listOf(aTag(), aTag(), aTag())
        given(tagsCache.tags()).willReturn(Observable.just(tags))

        presenter.onAttachView(view)

        verify(view).showTags(tags)
    }

    @Test
    fun displaysTagEditWhenSelectingATagAndDisplayTypeIsView() {
        val presenter = presenterWithDisplayTypeView()
        val tag = aTag()
        presenter.onAttachView(view)

        selectTag(tag)

        verify(view).displayTagEdit(tag)
    }

    @Test
    fun displaysTagEditOnCreateTag() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        createTag()

        verify(view).displayTagEdit(aNewTag())
    }

    @Test
    fun displaysArchivedTagsOnArchivedTags() {
        val presenter = presenterWithDisplayTypeView()
        presenter.onAttachView(view)

        archivedTags()

        verify(view).displayArchivedTags()
    }

    @Test
    fun showsArchivedTagsFromTagsCache() {
        val presenter = presenterWithDisplayTypeArchived()
        val tags = listOf(aTag(), aTag(), aTag())
        given(tagsCache.archivedTags()).willReturn(Observable.just(tags))

        presenter.onAttachView(view)

        verify(view).showTags(tags)
    }

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)

    private fun archivedTags() = displayArchivedTagsSubject.onNext(Unit)

    private fun createTag() {
        tagCreateSubject.onNext(Unit)
    }

    private fun presenterWithDisplayTypeView() = TagsPresenter(tagsCache, VIEW)

    private fun presenterWithDisplayTypeArchived() = TagsPresenter(tagsCache, VIEW_ARCHIVED)
}

