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

import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.subjects.PublishSubject

class TagsPresenterTest {
    val tagSelectedSubject = PublishSubject.create<Tag>()
    val saveSubject = PublishSubject.create<Unit>()
    val tagsRepository = mock(TagsRepository::class.java)
    val view = mock(TagsPresenter.View::class.java)

    @Before
    fun setUp() {
        given(view.onTagSelected()).willReturn(tagSelectedSubject)
        given(view.onSave()).willReturn(saveSubject)
        given(tagsRepository.observeTags()).willReturn(Observable.empty())
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

        verify(view, never()).showSelectedTags(any())
    }

    @Test
    fun showsSelectedTagsWhenDisplayTypeIsMultiChoice() {
        val selectedTags = setOf(aTag(), aTag())
        val presenter = TagsPresenter(tagsRepository, TagsPresenter.DisplayType.MULTI_CHOICE, selectedTags)
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
        given(tagsRepository.observeTags()).willReturn(Observable.just(tags))

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

    private fun selectTag(tagToSelect: Tag) = tagSelectedSubject.onNext(tagToSelect)

    private fun save() = saveSubject.onNext(Unit)

    private fun presenterWithDisplayTypeView() = TagsPresenter(tagsRepository, TagsPresenter.DisplayType.VIEW)

    private fun presenterWithDisplayTypeMultiChoice() = TagsPresenter(tagsRepository, TagsPresenter.DisplayType.MULTI_CHOICE)
}

