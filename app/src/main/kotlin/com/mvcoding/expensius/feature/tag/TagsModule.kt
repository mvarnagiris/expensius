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
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.provideSingletonFor
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.model.Tag
import com.mvcoding.expensius.provideAppUserSource
import com.mvcoding.expensius.provideFirebaseTagsService
import memoizrlabs.com.shankandroid.withActivityScope
import memoizrlabs.com.shankandroid.withThisScope

class TagsModule : ShankModule {
    override fun registerFactories() {
        createTagsWriter()
//        tagsPresenter()
//        quickTagsPresenter()
//        tagPresenter()
    }

    private fun createTagsWriter() = registerFactory(CreateTagsWriter::class) { ->
        CreateTagsWriter(provideAppUserSource()) { userId, createTags -> provideFirebaseTagsService().createTags(userId, createTags) }
    }

//    private fun tagsPresenter() = registerFactory(TagsPresenter::class) { modelDisplayType: ModelDisplayType ->
//        TagsPresenter(
//                modelDisplayType,
//                if (modelDisplayType == VIEW_ARCHIVED) provideArchivedTagsService() else provideTagsService(),
//                provideTagsWriteService(),
//                provideRxSchedulers())
//    }

//    private fun quickTagsPresenter() = registerFactory(QuickTagsPresenter::class) { ->
//        QuickTagsPresenter(provideTagsService(), provideRxSchedulers())
//    }
//
//    private fun tagPresenter() = registerFactory(TagPresenter::class) { tag: Tag -> TagPresenter(tag, provideTagsWriteService()) }
}

fun provideCreateTagsWriter() = provideNew<CreateTagsWriter>()
fun Activity.provideTagsPresenter(modelDisplayType: ModelDisplayType) = withThisScope.provideSingletonFor<TagsPresenter>(modelDisplayType)
fun View.provideQuickTagsPresenter(): QuickTagsPresenter = withActivityScope.provideSingletonFor()
fun Activity.provideTagPresenter(tag: Tag): TagPresenter = withThisScope.provideSingletonFor(tag)
