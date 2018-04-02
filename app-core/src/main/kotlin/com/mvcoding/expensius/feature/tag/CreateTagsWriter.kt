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

import com.mvcoding.expensius.data.DataSource
import com.mvcoding.expensius.data.DataWriter
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.CreateTag
import com.mvcoding.expensius.model.UserId

class CreateTagsWriter(
        private val appUserSource: DataSource<AppUser>,
        private val createTags: (UserId, Set<CreateTag>) -> Unit) : DataWriter<Set<CreateTag>> {

    override fun write(data: Set<CreateTag>) {
        appUserSource.data().firstOrError().toObservable().map { it.userId }.subscribe { createTags(it, data) }
    }
}