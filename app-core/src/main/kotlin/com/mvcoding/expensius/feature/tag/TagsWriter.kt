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
import com.mvcoding.expensius.model.AppUser
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.model.UserId

class TagsWriter(
        private val appUserSource: DataSource<AppUser>,
        private val updateTags: (UserId, Set<Tag>) -> Unit) : DataWriter<Set<Tag>> {

    override fun write(data: Set<Tag>) {
        appUserSource.data().first().map { it.userId }.subscribe { updateTags(it, data) }
    }
}