package com.mvcoding.expensius.feature.tag

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.UserId

class CreateTagsWriter(
        private val appUserSource: DataSource<AppUser>,
        private val createTags: (UserId, Set<CreateTag>) -> Unit) : DataWriter<Set<CreateTag>> {

    override fun write(data: Set<CreateTag>) {
        appUserSource.data().first().map { it.userId }.subscribe { createTags(it, data) }
    }
}