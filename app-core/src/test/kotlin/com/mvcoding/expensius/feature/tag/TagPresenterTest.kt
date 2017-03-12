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

class TagPresenterTest {
//    val titleChangesSubject = PublishSubject<String>()
//    val colorChangesSubject = PublishSubject<Int>()
//    val archiveToggleSubject = PublishSubject<Unit>()
//    val saveRequestsSubject = PublishSubject<Unit>()
//    val tag = aTag()
//    val tagsWriteService: TagsWriteService = mock()
//    val view: TagPresenter.View = mock()
//    val presenter = TagPresenter(tag, tagsWriteService)
//
//    @Before
//    fun setUp() {
//        whenever(view.titleChanges()).thenReturn(titleChangesSubject)
//        whenever(view.colorChanges()).thenReturn(colorChangesSubject)
//        whenever(view.archiveToggles()).thenReturn(archiveToggleSubject)
//        whenever(view.saveRequests()).thenReturn(saveRequestsSubject)
//        whenever(tagsWriteService.saveTags(any())).thenReturn(just(Unit))
//        whenever(tagsWriteService.createTags(any())).thenReturn(just(Unit))
//    }
//
//    @Test
//    fun showsInitialValues() {
//        presenter.attach(view)
//
//        verify(view).showModelState(tag.modelState)
//        verify(view).showTitle(tag.title)
//        verify(view).showColor(tag.color)
//    }
//
//    @Test
//    fun showsUpdateValuesWhenValuesAreUpdated() {
//        presenter.attach(view)
//
//        setTitle("updatedTitle")
//        setColor(10)
//
//        verify(view).showTitle(Title("updatedTitle"))
//        verify(view).showColor(Color(10))
//    }
//
//    @Test
//    fun showsUpdatedValuesAfterReattach() {
//        presenter.attach(view)
//
//        setTitle("updatedTitle")
//        setColor(10)
//        presenter.detach(view)
//        presenter.attach(view)
//
//        verify(view, times(2)).showTitle(Title("updatedTitle"))
//        verify(view, times(2)).showColor(Color(10))
//    }
//
//    @Test
//    fun showsDefaultColorWhenCreatingNewTag() {
//        TagPresenter(noTag, tagsWriteService).attach(view)
//
//        verify(view).showColor(Color(color(0x607d8b)))
//    }
//
//    @Test
//    fun doesNotTryToSaveAndShowsErrorWhenTitleIsEmptyOnSave() {
//        presenter.attach(view)
//        setTitle("")
//
//        save()
//
//        verify(tagsWriteService, never()).saveTags(any())
//        verify(view).showTitleCannotBeEmptyError()
//    }
//
//    @Test
//    fun savesTagWhenValidationSucceeds() {
//        presenter.attach(view)
//
//        save()
//
//        verify(tagsWriteService).saveTags(argThat { first() == tag })
//    }
//
//    @Test
//    fun createsNewTagWithVeryHighOrder() {
//        TagPresenter(noTag, tagsWriteService).attach(view)
//        setTitle("Updated title")
//        setColor(10)
//
//        save()
//
//        verify(tagsWriteService).createTags(argThat { first() == CreateTag(Title("Updated title"), Color(10), Order(VERY_HIGH_ORDER)) })
//    }
//
//    @Test
//    fun trimsTitleWhenSaving() {
//        presenter.attach(view)
//        setTitle(" title ")
//
//        save()
//
//        verify(tagsWriteService).saveTags(argThat { first().title.text == "title" })
//    }
//
//    @Test
//    fun displaysResultWhenTagIsSavedSuccessfully() {
//        presenter.attach(view)
//
//        save()
//
//        verify(view).displayResult()
//    }
//
//    @Test
//    fun archiveIsDisabledForNewTag() {
//        val presenter = TagPresenter(noTag, tagsWriteService)
//
//        presenter.attach(view)
//
//        verify(view).showArchiveEnabled(false)
//    }
//
//    @Test
//    fun archiveIsEnabledForExistingTag() {
//        presenter.attach(view)
//
//        verify(view).showArchiveEnabled(true)
//    }
//
//    @Test
//    fun archivesTagAndDisplaysResult() {
//        val archivedTag = tag.copy(modelState = ARCHIVED)
//        presenter.attach(view)
//
//        toggleArchive()
//
//        verify(tagsWriteService).saveTags(argThat { first() == archivedTag })
//        verify(view).displayResult()
//    }
//
//    @Test
//    fun restoresTagAndDisplaysResult() {
//        val archivedTag = tag.copy(modelState = ARCHIVED)
//        val restoredTag = tag.copy(modelState = NONE)
//        val presenter = TagPresenter(archivedTag, tagsWriteService)
//        presenter.attach(view)
//
//        toggleArchive()
//
//        verify(tagsWriteService).saveTags(argThat { first() == restoredTag })
//        verify(view).displayResult()
//    }
//
//    private fun setTitle(title: String) = titleChangesSubject.onNext(title)
//    private fun setColor(color: Int) = colorChangesSubject.onNext(color)
//    private fun toggleArchive() = archiveToggleSubject.onNext(Unit)
//    private fun save() = saveRequestsSubject.onNext(Unit)
}
