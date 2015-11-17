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

package com.mvcoding.financius

import android.content.Context
import com.memoizrlabs.Shank
import com.memoizrlabs.ShankModule
import com.mvcoding.financius.database.sqlite.SqliteDatabase
import com.mvcoding.financius.database.sqlite.TagsTable
import com.mvcoding.financius.feature.tag.DatabaseTagsRepository
import com.mvcoding.financius.feature.tag.TagsRepository

class AppModule(val context: Context) : ShankModule {
    init {
        Shank.registerFactory(Context::class.java, { context })
    }

    override fun registerFactories() {
        Shank.registerFactory(Settings::class.java, { UserSettings() })
        Shank.registerFactory(Session::class.java, { UserSession() })
        tagsRepository()
    }

    private fun tagsRepository() {
        val database = SqliteDatabase()
        val tagsTable = TagsTable();
        Shank.registerFactory(TagsRepository::class.java, { DatabaseTagsRepository(database, tagsTable) })
    }
}