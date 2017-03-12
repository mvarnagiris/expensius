package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.model.*
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import rx.Observable.just

class CreateTagsWriterTest {

    val appUser = anAppUser()

    val appUserSource = mock<DataSource<AppUser>>()
    val createTags = mock<(UserId, Set<CreateTag>) -> Unit>()
    val createTagsWriter = CreateTagsWriter(appUserSource, createTags)

    @Before
    fun setUp() {
        whenever(appUserSource.data()).thenReturn(just(appUser))
    }

    @Test
    fun `writes tags for app user`() {
        val createTagsSet = setOf(aCreateTag(), aCreateTag(), aCreateTag())

        createTagsWriter.write(createTagsSet)

        verify(createTags).invoke(appUser.userId, createTagsSet)
    }
}