/*
 * Copyright (C) 2017 Mantas Varnagiris.
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

import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.feature.color
import com.mvcoding.expensius.feature.tag.TagPresenter.Companion.VERY_HIGH_ORDER
import com.mvcoding.expensius.model.*
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.NullModels.noTag
import com.mvcoding.expensius.model.extensions.aTag
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.PublishSubject

class TagPresenterTest {
    val titleChangesSubject = PublishSubject<String>()
    val colorChangesSubject = PublishSubject<Int>()
    val archiveToggleSubject = PublishSubject<Unit>()
    val saveRequestsSubject = PublishSubject<Unit>()
    val tag = aTag()

    val tagsWriter = mock<DataWriter<Set<Tag>>>()
    val createTagsWriter = mock<DataWriter<Set<CreateTag>>>()
    val view = mock<TagPresenter.View>()
    val presenter = TagPresenter(tag, createTagsWriter, tagsWriter)

    @Before
    fun setUp() {
        whenever(view.titleChanges()).thenReturn(titleChangesSubject)
        whenever(view.colorChanges()).thenReturn(colorChangesSubject)
        whenever(view.archiveToggles()).thenReturn(archiveToggleSubject)
        whenever(view.saveRequests()).thenReturn(saveRequestsSubject)
    }

    @Test
    fun `shows initial values`() {
        presenter.attach(view)

        verify(view).showModelState(tag.modelState)
        verify(view).showTitle(tag.title)
        verify(view).showColor(tag.color)
    }

    @Test
    fun `shows updated values when values are updated`() {
        presenter.attach(view)

        setTitle("updatedTitle")
        setColor(10)

        verify(view).showTitle(Title("updatedTitle"))
        verify(view).showColor(Color(10))
    }

    @Test
    fun `shows updated values after reattach`() {
        presenter.attach(view)

        setTitle("updatedTitle")
        setColor(10)
        presenter.detach(view)
        presenter.attach(view)

        verify(view, times(2)).showTitle(Title("updatedTitle"))
        verify(view, times(2)).showColor(Color(10))
    }

    @Test
    fun `shows default color when creating new tag`() {
        TagPresenter(noTag, createTagsWriter, tagsWriter).attach(view)

        verify(view).showColor(Color(color(0x607d8b)))
    }

    @Test
    fun `does not try to save and shows error when title is empty on save`() {
        presenter.attach(view)
        setTitle("")

        save()

        verify(tagsWriter, never()).write(any())
        verify(view).showTitleCannotBeEmptyError()
    }

    @Test
    fun `saves tag when validation succeeds`() {
        presenter.attach(view)

        save()

        verify(tagsWriter).write(argThat { first() == tag })
    }

    @Test
    fun `creates new tag with very high order`() {
        TagPresenter(noTag, createTagsWriter, tagsWriter).attach(view)
        setTitle("Updated title")
        setColor(10)

        save()

        verify(createTagsWriter).write(argThat { first() == CreateTag(Title("Updated title"), Color(10), Order(VERY_HIGH_ORDER)) })
    }

    @Test
    fun `trims title when saving`() {
        presenter.attach(view)
        setTitle(" title ")

        save()

        verify(tagsWriter).write(argThat { first().title.text == "title" })
    }

    @Test
    fun `displays result when tag is saved successfully`() {
        presenter.attach(view)

        save()

        verify(view).displayResult()
    }

    @Test
    fun `archive is disabled for new tag`() {
        val presenter = TagPresenter(noTag, createTagsWriter, tagsWriter)

        presenter.attach(view)

        verify(view).showArchiveEnabled(false)
    }

    @Test
    fun `archive is enabled for existing tag`() {
        presenter.attach(view)

        verify(view).showArchiveEnabled(true)
    }

    @Test
    fun `archives tag and displays result`() {
        val archivedTag = tag.copy(modelState = ARCHIVED)
        TagPresenter(tag.copy(modelState = NONE), createTagsWriter, tagsWriter).attach(view)

        toggleArchive()

        verify(tagsWriter).write(argThat { first() == archivedTag })
        verify(view).displayResult()
    }

    @Test
    fun `restores tag and displays result`() {
        val archivedTag = tag.copy(modelState = ARCHIVED)
        val restoredTag = tag.copy(modelState = NONE)
        val presenter = TagPresenter(archivedTag, createTagsWriter, tagsWriter)
        presenter.attach(view)

        toggleArchive()

        verify(tagsWriter).write(argThat { first() == restoredTag })
        verify(view).displayResult()
    }

    private fun setTitle(title: String) = titleChangesSubject.onNext(title)
    private fun setColor(color: Int) = colorChangesSubject.onNext(color)
    private fun toggleArchive() = archiveToggleSubject.onNext(Unit)
    private fun save() = saveRequestsSubject.onNext(Unit)
}
