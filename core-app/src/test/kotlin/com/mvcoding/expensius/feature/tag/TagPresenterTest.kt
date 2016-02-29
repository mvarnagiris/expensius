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

import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.*
import rx.Observable
import rx.lang.kotlin.PublishSubject

class TagPresenterTest {
    val titleSubject = PublishSubject<String>()
    val colorSubject = PublishSubject<Int>()
    val archiveToggleSubject = PublishSubject<Unit>()
    val saveSubject = PublishSubject<Unit>()
    val tag = aTag()
    val newTag = aNewTag()
    val tagsProvider = TagsProviderForTest()
    val view = mock(TagPresenter.View::class.java)
    val presenter = TagPresenter(tag, tagsProvider)

    @Before
    fun setUp() {
        given(view.onTitleChanged()).willReturn(titleSubject)
        given(view.onColorChanged()).willReturn(colorSubject)
        given(view.onToggleArchive()).willReturn(archiveToggleSubject)
        given(view.onSave()).willReturn(saveSubject)
    }

    @Test
    fun showsInitialValues() {
        presenter.onViewAttached(view)

        verify(view).showTitle(tag.title)
        verify(view).showColor(tag.color)
        verify(view).showModelState(tag.modelState)
    }

    @Test
    fun showsUpdateValuesWhenValuesAreUpdated() {
        presenter.onViewAttached(view)

        updateTitle("updatedTitle")
        updateColor(10)

        verify(view).showTitle("updatedTitle")
        verify(view).showColor(10)
    }

    @Test
    fun showsUpdatedValuesAfterReattach() {
        presenter.onViewAttached(view)

        updateTitle("updatedTitle")
        updateColor(10)
        presenter.onViewDetached(view)
        presenter.onViewAttached(view)

        verify(view, times(2)).showTitle("updatedTitle")
        verify(view, times(2)).showColor(10)
    }

    @Test
    fun showsDefaultColorWhenCreatingNewTag() {
        val presenter = TagPresenter(newTag, tagsProvider)

        presenter.onViewAttached(view)

        verify(view).showColor(color(0x607d8b))
    }

    @Test
    fun doesNotTryToSaveAndShowsErrorWhenTitleIsEmptyOnSave() {
        presenter.onViewAttached(view)
        updateTitle("")

        save()

        assertThat(tagsProvider.lastSavedTags, nullValue())
        verify(view).showTitleCannotBeEmptyError()
    }

    @Test
    fun savesTagWhenValidationSucceeds() {
        presenter.onViewAttached(view)

        save()

        assertThat(tagsProvider.lastSavedTags!!.first(), equalTo(tag))
    }

    @Test
    fun trimsTitleWhenSaving() {
        presenter.onViewAttached(view)
        updateTitle(" title ")

        save()

        assertThat(tagsProvider.lastSavedTags!!.first().title, equalTo("title"))
    }

    @Test
    fun displaysResultWhenTagIsSavedSuccessfully() {
        presenter.onViewAttached(view)

        save()

        verify(view).displayResult(tag)
    }

    @Test
    fun archiveIsDisabledForNewTag() {
        val presenter = TagPresenter(newTag, tagsProvider)

        presenter.onViewAttached(view)

        verify(view).showArchiveEnabled(false)
    }

    @Test
    fun archiveIsEnabledForExistingTag() {
        presenter.onViewAttached(view)

        verify(view).showArchiveEnabled(true)
    }

    @Test
    fun archivesTagAndDisplaysResult() {
        val archivedTag = tag.withModelState(ARCHIVED)
        presenter.onViewAttached(view)

        toggleArchive()

        assertThat(tagsProvider.lastSavedTags?.first(), equalTo(archivedTag))
        verify(view).displayResult(archivedTag)
    }

    @Test
    fun restoresTagAndDisplaysResult() {
        val archivedTag = tag.withModelState(ARCHIVED)
        val restoredTag = tag.withModelState(NONE)
        val presenter = TagPresenter(archivedTag, tagsProvider)
        presenter.onViewAttached(view)

        toggleArchive()

        assertThat(tagsProvider.lastSavedTags?.first(), equalTo(restoredTag))
        verify(view).displayResult(restoredTag)
    }

    private fun updateTitle(title: String) {
        titleSubject.onNext(title)
    }

    private fun updateColor(color: Int) {
        colorSubject.onNext(color)
    }

    private fun toggleArchive() {
        archiveToggleSubject.onNext(Unit)
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
