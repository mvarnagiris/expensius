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

package com.mvcoding.financius.feature.overview

import android.content.Context
import android.os.Bundle
import android.widget.Button
import com.mvcoding.financius.feature.ActivityStarter
import com.mvcoding.financius.feature.BaseActivity
import com.mvcoding.financius.feature.tag.TagsActivity

class OverviewActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            ActivityStarter(context, OverviewActivity::class).start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this)
        setContentView(button)
        button.setOnClickListener { TagsActivity.startView(this) }
    }
}