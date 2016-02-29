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

package com.mvcoding.expensius.feature.tag

import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provideDatabase
import com.mvcoding.expensius.provider.DatabaseTagsProvider
import com.mvcoding.expensius.provider.database.table.TagsTable

class TagsModule : ShankModule {
    override fun registerFactories() {
        tagsProvider()
        quickTagsPresenter()
        tagsPresenter()
        tagPresenter()
    }

    private fun tagsProvider() = registerFactory(TagsProvider::class.java, { -> DatabaseTagsProvider(provideDatabase(), TagsTable()) })
    private fun quickTagsPresenter() = registerFactory(QuickTagsPresenter::class.java, { -> QuickTagsPresenter(provideTagsProvider()) })
    private fun tagPresenter() = registerFactory(TagPresenter::class.java, { tag: Tag -> TagPresenter(tag, provideTagsProvider()) })

    private fun tagsPresenter() = registerFactory(TagsPresenter::class.java, { modelDisplayType: ModelDisplayType ->
        TagsPresenter(provideTagsProvider(), modelDisplayType)
    })
}

fun provideTagsProvider() = provideSingleton(TagsProvider::class)