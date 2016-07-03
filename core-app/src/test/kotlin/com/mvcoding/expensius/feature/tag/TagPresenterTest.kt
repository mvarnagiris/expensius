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

import com.mvcoding.expensius.feature.color
import com.mvcoding.expensius.feature.tag.TagPresenter.Companion.VERY_HIGH_ORDER
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argThat
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.times
import org.mockito.BDDMockito.verify
import rx.lang.kotlin.PublishSubject

class TagPresenterTest {
    val titleSubject = PublishSubject<String>()
    val colorSubject = PublishSubject<Int>()
    val archiveToggleSubject = PublishSubject<Unit>()
    val saveSubject = PublishSubject<Unit>()
    val tag = aTag()
    val newTag = aNewTag()
    val tagsProvider = mock<TagsProvider>()
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
        presenter.attach(view)

        verify(view).showTitle(tag.title)
        verify(view).showColor(tag.color)
        verify(view).showModelState(tag.modelState)
    }

    @Test
    fun showsUpdateValuesWhenValuesAreUpdated() {
        presenter.attach(view)

        setTitle("updatedTitle")
        setColor(10)

        verify(view).showTitle("updatedTitle")
        verify(view).showColor(10)
    }

    @Test
    fun showsUpdatedValuesAfterReattach() {
        presenter.attach(view)

        setTitle("updatedTitle")
        setColor(10)
        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showTitle("updatedTitle")
        verify(view, times(2)).showColor(10)
    }

    @Test
    fun showsDefaultColorWhenCreatingNewTag() {
        val presenter = TagPresenter(newTag, tagsProvider)

        presenter.attach(view)

        verify(view).showColor(color(0x607d8b))
    }

    @Test
    fun doesNotTryToSaveAndShowsErrorWhenTitleIsEmptyOnSave() {
        presenter.attach(view)
        setTitle("")

        save()

        verify(tagsProvider, never()).save(any())
        verify(view).showTitleCannotBeEmptyError()
    }

    @Test
    fun savesTagWhenValidationSucceeds() {
        presenter.attach(view)

        save()

        verify(tagsProvider).save(argThat { first() == tag })
    }

    @Test
    fun savesNewTagWithVeryHighOrder() {
        TagPresenter(newTag, tagsProvider).attach(view)
        setTitle("Updated title")
        setColor(10)

        save()

        verify(tagsProvider).save(argThat { first().order == VERY_HIGH_ORDER })
    }

    @Test
    fun trimsTitleWhenSaving() {
        presenter.attach(view)
        setTitle(" title ")

        save()

        verify(tagsProvider).save(argThat { first().title == "title" })
    }

    @Test
    fun displaysResultWhenTagIsSavedSuccessfully() {
        presenter.attach(view)

        save()

        verify(view).displayResult(tag)
    }

    @Test
    fun archiveIsDisabledForNewTag() {
        val presenter = TagPresenter(newTag, tagsProvider)

        presenter.attach(view)

        verify(view).showArchiveEnabled(false)
    }

    @Test
    fun archiveIsEnabledForExistingTag() {
        presenter.attach(view)

        verify(view).showArchiveEnabled(true)
    }

    @Test
    fun archivesTagAndDisplaysResult() {
        val archivedTag = tag.withModelState(ARCHIVED)
        presenter.attach(view)

        toggleArchive()

        verify(tagsProvider).save(argThat { first() == archivedTag })
        verify(view).displayResult(archivedTag)
    }

    @Test
    fun restoresTagAndDisplaysResult() {
        val archivedTag = tag.withModelState(ARCHIVED)
        val restoredTag = tag.withModelState(NONE)
        val presenter = TagPresenter(archivedTag, tagsProvider)
        presenter.attach(view)

        toggleArchive()

        verify(tagsProvider).save(argThat { first() == restoredTag })
        verify(view).displayResult(restoredTag)
    }

    private fun setTitle(title: String) {
        titleSubject.onNext(title)
    }

    private fun setColor(color: Int) {
        colorSubject.onNext(color)
    }

    private fun toggleArchive() {
        archiveToggleSubject.onNext(Unit)
    }

    private fun save() {
        saveSubject.onNext(Unit)
    }
}
