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

import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule

class TagsModule(private val displayType: TagsPresenter.DisplayType, private val selectedTags: Set<Tag>) : ShankModule {
    override fun registerFactories() {
        val tagsRepository = Shank.provide(TagsRepository::class.java)
        Shank.registerFactory(TagsPresenter::class.java, { TagsPresenter(tagsRepository, displayType, selectedTags) })
    }
}