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

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.subjects.PublishSubject

class TagPresenterTest {
    val titleSubject = PublishSubject.create<String>()
    val colorSubject = PublishSubject.create<Int>()
    val saveSubject = PublishSubject.create<Unit>()
    val tag = aTag()
    val tagsCache = TagsProviderForTest()
    val view = mock(TagPresenter.View::class.java)
    val presenter = TagPresenter(tag, tagsCache)

    @Before
    fun setUp() {
        given(view.onTitleChanged()).willReturn(titleSubject)
        given(view.onColorChanged()).willReturn(colorSubject)
        given(view.onSave()).willReturn(saveSubject)
    }

    @Test
    fun showsInitialValues() {
        presenter.onAttachView(view)

        verify(view).showTitle(tag.title)
        verify(view).showColor(tag.color)
    }

    @Test
    fun showsUpdateValuesWhenValuesAreUpdated() {
        presenter.onAttachView(view)

        updateTitle("updatedTitle")
        updateColor(10)

        verify(view).showTitle("updatedTitle")
        verify(view).showColor(10)
    }

    @Test
    fun showsUpdatedValuesAfterReattach() {
        presenter.onAttachView(view)

        updateTitle("updatedTitle")
        updateColor(10)
        presenter.onDetachView(view)
        presenter.onAttachView(view)

        verify(view, times(2)).showTitle("updatedTitle")
        verify(view, times(2)).showColor(10)
    }

    @Test
    fun showsDefaultColorWhenCreatingNewTag() {
        val presenter = TagPresenter(Tag.noTag, tagsCache)
        presenter.onAttachView(view)

        verify(view).showColor(color(0x607d8b))
    }

    @Test
    fun doesNotTryToSaveAndShowsErrorWhenTitleIsEmptyOnSave() {
        presenter.onAttachView(view)
        updateTitle("")

        save()

        assertThat(tagsCache.lastSavedTags, nullValue())
        verify(view).showTitleCannotBeEmptyError()
    }

    @Test
    fun savesTagWhenValidationSucceeds() {
        presenter.onAttachView(view)

        save()

        assertThat(tagsCache.lastSavedTags!!.first(), equalTo(tag))
    }

    @Test
    fun trimsTitleWhenSaving() {
        presenter.onAttachView(view)
        updateTitle(" title ")

        save()

        assertThat(tagsCache.lastSavedTags!!.first().title, equalTo("title"))
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

    class TagsProviderForTest : TagsProvider {
        var lastSavedTags: Set<Tag>? = null

        override fun tags(): Observable<List<Tag>> {
            throw UnsupportedOperationException()
        }

        override fun archivedTags(): Observable<List<Tag>> {
            throw UnsupportedOperationException()
        }

        override fun save(tags: Set<Tag>) {
            lastSavedTags = tags
        }
    }
}
