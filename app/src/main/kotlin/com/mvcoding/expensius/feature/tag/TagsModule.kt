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

import android.app.Activity
import android.view.View
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provideDatabase
import com.mvcoding.expensius.provideRxSchedulers
import com.mvcoding.expensius.provideTagsService
import com.mvcoding.expensius.provideTagsWriteService
import com.mvcoding.expensius.provider.DatabaseTagsProvider
import com.mvcoding.expensius.provider.database.table.TagsTable
import memoizrlabs.com.shankandroid.withActivityScope
import memoizrlabs.com.shankandroid.withThisScope

class TagsModule : ShankModule {
    override fun registerFactories() {
        tagsProvider()
        quickTagsPresenter()
        tagsPresenter()
        tagPresenter()
    }

    private fun tagsProvider() = registerFactory(TagsProvider::class) { -> DatabaseTagsProvider(provideDatabase(), TagsTable()) }

    private fun quickTagsPresenter() = registerFactory(QuickTagsPresenter::class) { ->
        QuickTagsPresenter(provideTagsProvider(), provideRxSchedulers())
    }

    private fun tagPresenter() = registerFactory(TagPresenter::class) { tag: Tag -> TagPresenter(tag, provideTagsProvider()) }

    private fun tagsPresenter() = registerFactory(TagsPresenter::class) { modelDisplayType: ModelDisplayType ->
        TagsPresenter(
                modelDisplayType,
                provideTagsService(modelDisplayType == VIEW_ARCHIVED),
                provideTagsWriteService(),
                provideRxSchedulers())
    }
}

fun provideTagsProvider(): TagsProvider = provideGlobalSingleton()
fun View.provideQuickTagsPresenter(): QuickTagsPresenter = withActivityScope.provideSingletonFor()
fun View.provideTagPresenter(tag: Tag): TagPresenter = withActivityScope.provideSingletonFor(tag)
fun Activity.provideTagsPresenter(modelDisplayType: ModelDisplayType) = withThisScope.provideSingletonFor<TagsPresenter>(modelDisplayType)