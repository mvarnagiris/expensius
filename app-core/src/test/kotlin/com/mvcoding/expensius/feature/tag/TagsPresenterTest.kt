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

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.data.RealtimeData
import com.mvcoding.expensius.data.RealtimeData.*
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.tag.TagsPresenter.TagMove
import com.mvcoding.expensius.model.NullModels.noTag
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.Title
import com.mvcoding.expensius.model.extensions.aString
import com.mvcoding.expensius.model.extensions.aTag
import com.mvcoding.expensius.model.extensions.withOrder
import com.mvcoding.expensius.rxSchedulers
import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import rx.lang.kotlin.BehaviorSubject
import rx.lang.kotlin.PublishSubject

class TagsPresenterTest {
    val tagsSubject = BehaviorSubject<RealtimeData<Tag>>()
    val tagSelectsSubject = PublishSubject<Tag>()
    val createTagRequestsSubject = PublishSubject<Unit>()
    val displayArchivedTagsSubject = PublishSubject<Unit>()
    val tagMovesSubject = PublishSubject<TagsPresenter.TagMove>()

    val tagsSource = mock<DataSource<RealtimeData<Tag>>>()
    val tagsWriter = mock<DataWriter<Set<Tag>>>()
    val view: TagsPresenter.View = mock()
    val inOrder = inOrder(view)

    @Before
    fun setUp() {
        whenever(view.tagSelects()).thenReturn(tagSelectsSubject)
        whenever(view.createTagRequests()).thenReturn(createTagRequestsSubject)
        whenever(view.archivedTagsRequests()).thenReturn(displayArchivedTagsSubject)
        whenever(view.tagMoves()).thenReturn(tagMovesSubject)
        whenever(tagsSource.data()).thenReturn(tagsSubject)
    }

    @Test
    fun `shows model display type not archived and show archived tags request`() {
        presenter(VIEW_NOT_ARCHIVED).attach(view)

        verify(view).showModelDisplayType(VIEW_NOT_ARCHIVED)
        verify(view).showArchivedTagsRequest()
    }

    @Test
    fun `shows model display type archived and does not show archived tags request`() {
        presenter(VIEW_ARCHIVED).attach(view)

        verify(view).showModelDisplayType(VIEW_ARCHIVED)
        verify(view, never()).showArchivedTagsRequest()
    }

    @Test
    fun `shows loading until first data comes and then displays all types of realtime data as it comes`() {
        val tags = listOf(aTag(), aTag(), aTag())
        val addedTags = listOf(aTag(), aTag(), aTag())
        val changedTags = listOf(addedTags.first().copy(title = Title(aString())))
        val removedTags = listOf(addedTags.last())
        val movedTags = listOf(tags.first())

        presenter().attach(view)
        inOrder.verify(view).showLoading()

        receiveTags(tags)
        inOrder.verify(view).hideLoading()
        inOrder.verify(view).showItems(tags)

        receiveTagsAdded(addedTags, 3)
        inOrder.verify(view).showAddedItems(addedTags, 3)

        receiveTagsChanged(changedTags, 3)
        inOrder.verify(view).showChangedItems(changedTags, 3)

        receiveTagsChanged(changedTags, 3)
        inOrder.verify(view).showChangedItems(changedTags, 3)

        receiveTagsRemoved(removedTags, 5)
        inOrder.verify(view).showRemovedItems(removedTags, 5)

        receiveTagsMoved(movedTags, 0, 5)
        inOrder.verify(view).showMovedItems(movedTags, 0, 5)
    }

    @Test
    fun `displays tag edit when selecting a tag and display type is view`() {
        val tag = aTag()
        presenter().attach(view)

        selectTag(tag)

        verify(view).displayTagEdit(tag)
    }

    @Test
    fun `displays tag edit when create tag is requested`() {
        presenter().attach(view)

        requestCreateTag()

        verify(view).displayTagEdit(noTag)
    }

    @Test
    fun `displays archived tags`() {
        presenter().attach(view)

        requestArchivedTags()

        verify(view).displayArchivedTags()
    }

    @Test
    fun `reorders all tags when one tag was moved up`() {
        val tags = listOf(aTag(), aTag(), aTag(), aTag())
        val reorderedTags = listOf(tags[2].withOrder(0), tags[0].withOrder(1), tags[1].withOrder(2), tags[3].withOrder(3))
        val movedItems = listOf(reorderedTags[0])
        presenter().attach(view)

        receiveTags(tags)
        moveTag(2, 0)
        receiveTagsMoved(movedItems, 2, 0)

        verify(tagsWriter).write(reorderedTags.toSet())
        verify(view).showMovedItems(movedItems, 2, 0)
    }

    @Test
    fun `reorders all tags when one tag was moved down`() {
        val tags = listOf(aTag(), aTag(), aTag(), aTag())
        val reorderedTags = listOf(tags[1].withOrder(0), tags[2].withOrder(1), tags[0].withOrder(2), tags[3].withOrder(3))
        presenter().attach(view)

        receiveTags(tags)
        moveTag(0, 2)
        receiveTagsMoved(listOf(reorderedTags[0]), 1, 0)
        receiveTagsMoved(listOf(reorderedTags[1]), 2, 1)

        verify(tagsWriter).write(reorderedTags.toSet())
        verify(view).showMovedItems(listOf(reorderedTags[0]), 1, 0)
        verify(view).showMovedItems(listOf(reorderedTags[1]), 2, 1)
    }

    private fun moveTag(fromPosition: Int, toPosition: Int) = tagMovesSubject.onNext(TagMove(fromPosition, toPosition))
    private fun receiveTags(tags: List<Tag>) = tagsSubject.onNext(AllItems(tags))
    private fun requestCreateTag() = createTagRequestsSubject.onNext(Unit)
    private fun requestArchivedTags() = displayArchivedTagsSubject.onNext(Unit)
    private fun receiveTagsAdded(tags: List<Tag>, position: Int) = tagsSubject.onNext(AddedItems(tags, tags, position))
    private fun receiveTagsChanged(tags: List<Tag>, position: Int) = tagsSubject.onNext(ChangedItems(tags, tags, position))
    private fun receiveTagsRemoved(tags: List<Tag>, position: Int) = tagsSubject.onNext(RemovedItems(tags, tags, position))
    private fun receiveTagsMoved(tags: List<Tag>, fromPosition: Int, toPosition: Int) = tagsSubject.onNext(MovedItems(tags, tags, fromPosition, toPosition))
    private fun selectTag(tagToSelect: Tag) = tagSelectsSubject.onNext(tagToSelect)
    private fun presenter(modelViewType: ModelDisplayType = VIEW_NOT_ARCHIVED) = TagsPresenter(modelViewType, tagsSource, tagsWriter, rxSchedulers())
}