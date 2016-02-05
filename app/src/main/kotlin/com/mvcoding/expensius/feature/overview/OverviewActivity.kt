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

package com.mvcoding.expensius.feature.overview

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity

class OverviewActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            ActivityStarter(context, OverviewActivity::class).start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_overview)
        removeUpArrowFromToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.overview, menu)
        return true;
    }

    private fun removeUpArrowFromToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        toolbar.setContentInsetsRelative(keyline, keyline)
    }
}