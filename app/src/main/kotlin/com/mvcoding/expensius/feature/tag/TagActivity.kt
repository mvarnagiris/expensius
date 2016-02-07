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

class TagActivity : BaseActivity() {
    companion object {
        private const val EXTRA_TAG = "EXTRA_TAG"

        fun start(context: Context, tag: Tag) {
            ActivityStarter(context, TagActivity::class)
                    .extra(EXTRA_TAG, tag)
                    .start()
        }
    }

    private val tagView by lazy { findViewById(R.id.tagView) as TagView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_tag)
        tagView.init(intent.getSerializableExtra(TagActivity.EXTRA_TAG) as Tag)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.tag, menu)
        menu.findItem(R.id.action_archive).isVisible = tagView.isArchiveVisible
        return true
    }
}