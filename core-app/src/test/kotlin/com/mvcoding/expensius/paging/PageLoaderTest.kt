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

package com.mvcoding.expensius.paging

import org.junit.Test
import rx.Observable.just
import rx.observers.TestSubscriber
import rx.subjects.BehaviorSubject

class PageLoaderTest {
    private val query = Query()
    private val subscriber = TestSubscriber.create<PageResult<Item>>()
    private val converter = { dataItem: DataItem -> Item() }
    private val pageLoader = PageLoaderForTest()

    @Test
    fun emitsEmptyListWhenDataSetIsEmpty() {
        val page = Page(0, 2)
        pageLoader.queryData = Data(emptyList())

        pageLoader.load(converter, query, just(page)).subscribe(subscriber)

        subscriber.assertValue(PageResult(page, emptyList(), true))
    }

    @Test
    fun emitsLessItemsThanRequestedWhenDataSetDoesNotContainEnoughItems() {
        val page = Page(0, 2)
        pageLoader.queryData = Data(listOf(DataItem()))

        pageLoader.load(converter, query, just(page)).subscribe(subscriber)

        subscriber.assertValue(PageResult(page, listOf(Item()), true))
    }

    @Test
    fun emitsRequestedAmountOfItemsWhenDataSetContainsEnoughItems() {
        val page = Page(0, 2)
        pageLoader.queryData = Data(listOf(DataItem(), DataItem(), DataItem()))

        pageLoader.load(converter, query, just(page)).subscribe(subscriber)

        subscriber.assertValue(PageResult(page, listOf(Item(), Item()), true))
    }

    @Test
    fun emitsLessItemsThanRequestedWhenRequestingSecondPageAndThereAreNotEnoughItemsFotIt() {
        val firstPage = Page(0, 2)
        val secondPage = firstPage.nextPage()
        val pageObservable = BehaviorSubject.create(firstPage)
        pageLoader.queryData = Data(listOf(DataItem(), DataItem(), DataItem()))

        pageLoader.load(converter, query, pageObservable).subscribe(subscriber)
        pageObservable.onNext(secondPage)

        subscriber.assertValues(
                PageResult(firstPage, listOf(Item(), Item()), true),
                PageResult(secondPage, listOf(Item()), false))
    }

    @Test
    fun emitsRequestedAmountOfItemsWhenRequestingSecondPageAndDataSetContainsEnoughItems() {
        val firstPage = Page(0, 2)
        val secondPage = firstPage.nextPage()
        val pageObservable = BehaviorSubject.create(firstPage)
        pageLoader.queryData = Data(listOf(DataItem(), DataItem(), DataItem(), DataItem()))

        pageLoader.load(converter, query, pageObservable).subscribe(subscriber)
        pageObservable.onNext(secondPage)

        subscriber.assertValues(
                PageResult(firstPage, listOf(Item(), Item()), true),
                PageResult(secondPage, listOf(Item(), Item()), false))
    }

    private data class Item(val id: Int = 0)

    private class Query

    private class Data(val dataItems: List<DataItem>)

    private class DataItem

    private class PageLoaderForTest : PageLoader<Item, Query, Data, DataItem>() {
        var queryData: Data = Data(emptyList())

        override fun load(query: Query) = just(queryData);

        override fun sizeOf(data: Data) = data.dataItems.size

        override fun dataItemAtPosition(data: Data, position: Int) = data.dataItems.elementAt(position)
    }
}