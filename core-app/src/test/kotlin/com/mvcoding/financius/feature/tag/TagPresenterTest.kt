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
import rx.subjects.PublishSubject

class TagPresenterTest {
    val titleSubject = PublishSubject.create<String>()
    val colorSubject = PublishSubject.create<Int>()
    val saveSubject = PublishSubject.create<Unit>()
    val tag = aTag()
    val tagsRepository = mock(TagsCache::class.java)
    val view = mock(TagPresenter.View::class.java)
    val presenter = TagPresenter(tag, tagsRepository)

    @Before
    fun setUp() {
        given(view.onTitleChanged()).willReturn(titleSubject)
        given(view.onColorChanged()).willReturn(colorSubject)
        given(view.onSave()).willReturn(saveSubject)
    }

    @Test
    fun showsTitle() {
        presenter.onAttachView(view)

        verify(view).showTitle(tag.title)
    }

    @Test
    fun showsUpdatedTitleWhenTitleIsUpdated() {
        presenter.onAttachView(view)

        updateTitle("updatedTitle")

        verify(view).showTitle("updatedTitle")
    }

    @Test
    fun showsUpdatedTitleAfterReattach() {
        presenter.onAttachView(view)

        updateTitle("updatedTitle")
        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showTitle("updatedTitle")
    }

    @Test
    fun showsColor() {
        presenter.onAttachView(view)

        verify(view).showColor(tag.color)
    }

    @Test
    fun showsUpdatedColorWhenColorIsUpdated() {
        presenter.onAttachView(view)

        updateColor(10)

        verify(view).showColor(10)
    }

    @Test
    fun showsUpdatedColorAfterReattach() {
        presenter.onAttachView(view)

        updateColor(10)
        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showColor(10)
    }

    @Test
    fun doesNotTryToSaveAndShowsErrorWhenTitleIsEmptyOnSave() {
        presenter.onAttachView(view)
        updateTitle("")

        save()

        verifyZeroInteractions(tagsRepository)
        verify(view).showTitleCannotBeEmptyError()
    }

    @Test
    fun savesTagWhenValidationSucceeds() {
        presenter.onAttachView(view)

        save()

        verify(tagsRepository).save(tag)
    }

    @Test
    fun startsResultWhenTagIsSavedSuccessfully() {
        presenter.onAttachView(view)

        save()

        verify(view).startResult(tag)
    }

    private fun updateTitle(title: String) {
        titleSubject.onNext(title)
    }

    private fun updateColor(color: Int) {
        colorSubject.onNext(color)
    }

    private fun save() {
        saveSubject.onNext(Unit)
    }
}
