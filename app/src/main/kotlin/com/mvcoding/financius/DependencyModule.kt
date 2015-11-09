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
import com.mvcoding.financius.feature.intro.IntroPage
import com.mvcoding.financius.feature.intro.IntroPresenter
import com.mvcoding.financius.feature.splash.SplashPresenter
import com.mvcoding.financius.feature.tag.PersistedTagsRepository
import com.mvcoding.financius.feature.tag.TagsRepository

class DependencyModule(val context: Context) : ShankModule {
    init {
        Shank.registerFactory(Context::class.java, { context })
    }

    override fun registerFactories() {
        userSettings()
        session()

        tagsRepository()

        splashPresenter()
        introPresenter()
    }

    private fun tagsRepository() {
        Shank.registerFactory(TagsRepository::class.java, { PersistedTagsRepository() })
    }

    private fun introPresenter() {
        val context = Shank.withScope(Unit::class.java).provide(Context::class.java)
        val userSettings = Shank.withScope(Unit::class.java).provide(Settings::class.java)

        val introPage1 = IntroPage(ResourceImage(R.drawable.intro_1), context.getString(R.string.intro_title_1), context.getString(R.string.intro_message_1))
        val introPage2 = IntroPage(ResourceImage(R.drawable.intro_2), context.getString(R.string.intro_title_2), context.getString(R.string.intro_message_2))
        val introPage3 = IntroPage(ResourceImage(R.drawable.intro_3), context.getString(R.string.intro_title_3), context.getString(R.string.intro_message_3))
        val introPage4 = IntroPage(ResourceImage(R.drawable.intro_4), context.getString(R.string.intro_title_4), context.getString(R.string.intro_message_4))

        val introPages = listOf(introPage1, introPage2, introPage3, introPage4)

        Shank.registerFactory(IntroPresenter::class.java, { IntroPresenter(introPages, userSettings) })
    }

    private fun splashPresenter() {
        val userSettings = Shank.withScope(Unit::class.java).provide(Settings::class.java)
        val session = Shank.withScope(Unit::class.java).provide(Session::class.java)
        Shank.registerFactory(SplashPresenter::class.java, { SplashPresenter(userSettings, session) })
    }

    private fun session() {
        Shank.registerFactory(Session::class.java, { UserSession() })
    }

    private fun userSettings() {
        Shank.registerFactory(Settings::class.java, { UserSettings() })
    }
}

