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

import android.content.Context
import android.os.Bundle
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import rx.Observable.never

class TagsActivity : BaseActivity() {
    private val tagsView by lazy { findViewById(R.id.tagsView) as TagsView }

    companion object {
        private const val EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE"

        fun startView(context: Context) {
            ActivityStarter(context, TagsActivity::class)
                    .extra(EXTRA_DISPLAY_TYPE, VIEW_NOT_ARCHIVED)
                    .start()
        }

        fun startArchived(context: Context) {
            ActivityStarter(context, TagsActivity::class)
                    .extra(EXTRA_DISPLAY_TYPE, VIEW_ARCHIVED)
                    .start()
        }
    }

    private val displayType by lazy { intent.getSerializableExtra(EXTRA_DISPLAY_TYPE) as ModelDisplayType }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tags)

        val displayType = displayType
        tagsView.init(displayType, never())
        supportActionBar?.title = if (displayType == VIEW_ARCHIVED) getString(R.string.archived_tags) else getString(R.string.tags)
    }
}