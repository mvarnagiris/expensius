package com.mvcoding.expensius.feature.login

import com.mvcoding.expensius.aString
import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.RawRealtimeData
import com.mvcoding.expensius.data.RealtimeList
import com.mvcoding.expensius.model.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just
import rx.Observable.never
import rx.lang.kotlin.PublishSubject
import rx.observers.TestSubscriber

class TagsSnapshotSourceTest {

    val realtimeDataSubject = PublishSubject<RawRealtimeData.AllItems<Tag>>()
    val addedItemsSubject = PublishSubject<RawRealtimeData.AddedItems<Tag>>()

    val appUser = anAppUser()
    val realtimeList = mock<RealtimeList<Tag>>()

    val appUserSource = mock<DataSource<AppUser>>()
    val createRealtimeList = mock<(UserId) -> RealtimeList<Tag>>()
    val tagsSnapshotSource = TagsSnapshotSource(appUserSource, createRealtimeList)
    val subscriber = TestSubscriber<List<Tag>>()

    @Before
    fun setUp() {
        whenever(appUserSource.data()).thenReturn(just(appUser))
        whenever(createRealtimeList(appUser.userId)).thenReturn(realtimeList)
        whenever(realtimeList.getAllItems()).thenReturn(realtimeDataSubject)
        whenever(realtimeList.getAddedItems()).thenReturn(addedItemsSubject)
        whenever(realtimeList.getChangedItems()).thenReturn(never())
        whenever(realtimeList.getRemovedItems()).thenReturn(never())
        whenever(realtimeList.getMovedItem()).thenReturn(never())
    }

    @Test
    fun `returns only once and only after getting all items`() {
        val tags = listOf(aTag())

        tagsSnapshotSource.data().subscribe(subscriber)
        subscriber.assertNoValues()

        addedItemsSubject.onNext(RawRealtimeData.AddedItems(listOf(aTag()), aString()))
        subscriber.assertNoValues()

        realtimeDataSubject.onNext(RawRealtimeData.AllItems(tags))
        subscriber.assertValue(tags)

        addedItemsSubject.onNext(RawRealtimeData.AddedItems(listOf(aTag()), aString()))
        subscriber.assertValue(tags)

        verify(realtimeList).close()
    }
}