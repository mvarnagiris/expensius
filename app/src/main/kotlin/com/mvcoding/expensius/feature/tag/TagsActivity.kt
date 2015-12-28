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
import android.view.Menu
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.ARCHIVED
import com.mvcoding.expensius.feature.tag.TagsPresenter.DisplayType.VIEW

class TagsActivity : BaseActivity() {
    private val tagsView by lazy { findViewById(R.id.tagsView) as TagsView }

    companion object {
        private const val EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE"
        private const val EXTRA_SELECTED_TAGS = "EXTRA_SELECTED_TAGS"

        fun startView(context: Context) {
            ActivityStarter(context, TagsActivity::class)
                    .extra(EXTRA_DISPLAY_TYPE, VIEW)
                    .start()
        }

        fun startArchived(context: Context) {
            ActivityStarter(context, TagsActivity::class)
                    .extra(EXTRA_DISPLAY_TYPE, ARCHIVED)
                    .start()
        }
    }

    private val displayType by lazy { intent.getSerializableExtra(EXTRA_DISPLAY_TYPE) as TagsPresenter.DisplayType }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_tags)

        val displayType = displayType
        val selectedTags = setOf<Tag>() // TODO: Get selected tags from extras.
        tagsView.init(displayType, selectedTags)
        supportActionBar.title = if (displayType == ARCHIVED) getString(R.string.archived_tags) else getString(R.string.tags)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)
        if (displayType != ARCHIVED) {
            menuInflater.inflate(R.menu.tags, menu)
            return true
        }
        return result
    }
}