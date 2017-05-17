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
import com.mvcoding.expensius.model.ModelState
import com.mvcoding.expensius.model.Tag
import rx.Observable

class NotArchivedTagsSource(private val allTagsSource: DataSource<List<Tag>>) : DataSource<List<Tag>> {
    override fun data(): Observable<List<Tag>> = allTagsSource.data().map { it.filter { it.modelState == ModelState.NONE } }
}