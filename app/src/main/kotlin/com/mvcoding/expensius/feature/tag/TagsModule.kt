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

import android.app.Activity
import android.view.View
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideFirebaseTagsService
import com.mvcoding.expensius.provideRxSchedulers
import memoizrlabs.com.shankandroid.withActivityScope
import memoizrlabs.com.shankandroid.withThisScope

class TagsModule : ShankModule {
    override fun registerFactories() {
        createTagsWriter()
        tagsSource()
        tagsWriter()
        tagsPresenter()
//        quickTagsPresenter()
        tagPresenter()
    }

    private fun createTagsWriter() = registerFactory(CreateTagsWriter::class) { ->
        CreateTagsWriter(provideAppUserSource()) { userId, createTags -> provideFirebaseTagsService().createTags(userId, createTags) }
    }

    private fun tagsSource() = registerFactory(TagsSource::class) { modelDisplayType: ModelDisplayType ->
        TagsSource(provideAppUserSource()) {
            if (modelDisplayType == VIEW_ARCHIVED) provideFirebaseTagsService().getArchivedTags(it)
            else provideFirebaseTagsService().getTags(it)
        }
    }

    private fun tagsWriter() = registerFactory(TagsWriter::class) { ->
        TagsWriter(provideAppUserSource()) { userId, tags ->
            provideFirebaseTagsService().updateTags(userId, tags)
        }
    }

    private fun tagsPresenter() = registerFactory(TagsPresenter::class) { modelDisplayType: ModelDisplayType ->
        TagsPresenter(modelDisplayType, provideTagsSource(modelDisplayType), provideTagsWriter(), provideRxSchedulers())
    }

    private fun tagPresenter() = registerFactory(TagPresenter::class) { tag: Tag -> TagPresenter(tag, provideCreateTagsWriter(), provideTagsWriter()) }

//    private fun quickTagsPresenter() = registerFactory(QuickTagsPresenter::class) { ->
//        QuickTagsPresenter(provideTagsService(), provideRxSchedulers())
//    }
}

fun provideCreateTagsWriter() = provideNew<CreateTagsWriter>()
fun provideTagsWriter() = provideNew<TagsWriter>()
fun provideTagsSource(modelDisplayType: ModelDisplayType) = provideGlobalSingleton<TagsSource>(modelDisplayType)

fun Activity.provideTagsPresenter(modelDisplayType: ModelDisplayType) = withThisScope.provideSingletonFor<TagsPresenter>(modelDisplayType)
fun View.provideQuickTagsPresenter(): QuickTagsPresenter = withActivityScope.provideSingletonFor()
fun Activity.provideTagPresenter(tag: Tag): TagPresenter = withThisScope.provideSingletonFor(tag)
