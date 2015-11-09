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

import android.content.Context
import android.os.Bundle
import com.mvcoding.financius.R
import com.mvcoding.financius.feature.ActivityStarter
import com.mvcoding.financius.feature.BaseActivity
import kotlinx.android.synthetic.view_tags.tagsView

class TagsActivity : BaseActivity() {
    companion object {
        private const val EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE"
        private const val EXTRA_SELECTED_TAGS = "EXTRA_SELECTED_TAGS"

        fun startView(context: Context) {
            ActivityStarter(context, TagsActivity::class)
                    .extra(EXTRA_DISPLAY_TYPE, TagsPresenter.DisplayType.VIEW)
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_tags)

        val displayType = intent.getSerializableExtra(EXTRA_DISPLAY_TYPE) as TagsPresenter.DisplayType
        val selectedTags = setOf<Tag>() // TODO: Get selected tags from extras.
        tagsView.init(displayType, selectedTags)
    }
}