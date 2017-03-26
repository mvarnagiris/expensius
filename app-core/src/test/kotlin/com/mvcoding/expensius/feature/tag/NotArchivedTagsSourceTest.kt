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
import com.mvcoding.expensius.model.ModelState.ARCHIVED
import com.mvcoding.expensius.model.ModelState.NONE
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.aTag
import com.mvcoding.expensius.model.withModelState
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test
import rx.Observable.just
import rx.observers.TestSubscriber

class NotArchivedTagsSourceTest {
    @Test
    fun `only emits not archived tags`() {
        val tags = listOf(aTag().withModelState(ARCHIVED), aTag().withModelState(NONE))
        val expectedTags = tags.filter { it.modelState == NONE }
        val allTagsSource = mock<DataSource<List<Tag>>>()
        whenever(allTagsSource.data()).thenReturn(just(tags))
        val notArchivedTagsSource = NotArchivedTagsSource(allTagsSource)
        val subscriber = TestSubscriber<List<Tag>>()

        notArchivedTagsSource.data().subscribe(subscriber)

        subscriber.assertValue(expectedTags)
    }
}