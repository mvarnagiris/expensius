/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

class AllTagsSourceTest {
//    @Test
//    fun `returns all tags from all realtime data events`() {
//        val allTags = listOf(aTag(), aTag())
//        val addedTags = allTags.plus(aTag())
//        val changedTags = addedTags.plus(aTag())
//        val removedTags = changedTags.plus(aTag())
//        val movedTags = removedTags.plus(aTag())
//        val tagsSource = mock<DataSource<RealtimeData<Tag>>>()
//        val archivedTagsSource = mock<DataSource<RealtimeData<Tag>>>()
//        val tagsSubject = PublishSubject<RealtimeData<Tag>>()
//        val allTagsSource = AllTagsSource(tagsSource, archivedTagsSource)
//        val subscriber = TestSubscriber<List<Tag>>()
//        whenever(tagsSource.data()).thenReturn(tagsSubject)
//        whenever(archivedTagsSource.data()).thenReturn(just(AllItems(emptyList())))
//        allTagsSource.data().subscribe(subscriber)
//
//        tagsSubject.onNext(AllItems(allTags))
//        tagsSubject.onNext(AddedItems(addedTags, addedTags, 0))
//        tagsSubject.onNext(ChangedItems(changedTags, changedTags, 0))
//        tagsSubject.onNext(RemovedItems(removedTags, removedTags, 0))
//        tagsSubject.onNext(MovedItems(movedTags, movedTags, 0, 1))
//
//        subscriber.assertValues(allTags, addedTags, changedTags, removedTags, movedTags)
//    }
//
//    @Test
//    fun `combines tags and archived tags`() {
//        val allTags = listOf(aTag(), aTag())
//        val archivedAllTags = listOf(aTag(), aTag())
//        val tagsSource = mock<DataSource<RealtimeData<Tag>>>()
//        val archivedTagsSource = mock<DataSource<RealtimeData<Tag>>>()
//        val allTagsSource = AllTagsSource(tagsSource, archivedTagsSource)
//        val subscriber = TestSubscriber<List<Tag>>()
//        whenever(tagsSource.data()).thenReturn(just(AllItems(allTags)))
//        whenever(archivedTagsSource.data()).thenReturn(just(AllItems(archivedAllTags)))
//        allTagsSource.data().subscribe(subscriber)
//
//        subscriber.assertValues(allTags + archivedAllTags)
//    }
}