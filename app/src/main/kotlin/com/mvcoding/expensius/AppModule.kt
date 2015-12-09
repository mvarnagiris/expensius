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

package com.mvcoding.expensius

import android.content.Context
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.mvcoding.expensius.cache.DatabaseTagsCache
import com.mvcoding.expensius.cache.database.Database
import com.mvcoding.expensius.cache.database.SqliteDatabase
import com.mvcoding.expensius.cache.database.table.TagsTable
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.feature.tag.TagsCache
import com.squareup.sqlbrite.SqlBrite

class AppModule(val context: Context) : ShankModule {
    init {
        registerFactory(Context::class.java, { context })
    }

    override fun registerFactories() {
        registerFactory(Settings::class.java, { UserSettings() })
        registerFactory(Session::class.java, { UserSession() })
        database()
        tagsCache()
    }

    private fun database() {
        val briteDatabase = SqlBrite.create().wrapDatabaseHelper(DBHelper(context, TagsTable()))
        registerFactory(Database::class.java, { SqliteDatabase(briteDatabase) })
    }

    private fun tagsCache() {
        val database = provideSingleton(Database::class)
        registerFactory(TagsCache::class.java, { DatabaseTagsCache(database, TagsTable()) })
    }
}