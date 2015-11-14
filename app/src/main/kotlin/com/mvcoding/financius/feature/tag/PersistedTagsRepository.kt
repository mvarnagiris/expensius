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

package com.mvcoding.financius.feature.tag

import android.graphics.Color
import rx.Observable
import rx.lang.kotlin.BehaviourSubject

class PersistedTagsRepository : TagsRepository {
    private var tags = listOf(
            Tag("1", "Essential", Color.parseColor("#ff8bc34a")),
            Tag("2", "Fixed", Color.parseColor("#ff00bcd4")),
            Tag("3", "Non-essential", Color.parseColor("#ffff5722")))
    private val tagsSubject = BehaviourSubject(tags)

    override fun save(tag: Tag) {
        tags = tags.plus(tag)
        tagsSubject.onNext(tags)
    }

    override fun observeTags(): Observable<List<Tag>> {
        return tagsSubject
    }
}