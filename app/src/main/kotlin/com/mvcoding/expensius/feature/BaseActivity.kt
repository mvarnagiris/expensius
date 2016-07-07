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

package com.mvcoding.expensius.feature

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.mvcoding.expensius.extension.forEach
import com.mvcoding.expensius.extension.getColorFromTheme
import kotlinx.android.synthetic.main.toolbar.*
import memoizrlabs.com.shankandroid.ShankAppCompatActivity

abstract class BaseActivity : ShankAppCompatActivity() {
    override val finalAction: (Any) -> Unit = { if (it is Destroyable) it.onDestroy() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupToolbar()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setupToolbar()
    }

    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        setupToolbar()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        tintToolbarIcons(menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun setupToolbar() {
        toolbar?.run {
            setSupportActionBar(this)
            supportActionBar?.run {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
            }
        }
    }

    private fun tintToolbarIcons(menu: Menu) {
        toolbar?.apply {
            val tintColor = getColorFromTheme(context, android.R.attr.textColorPrimary)
            menu.forEach {
                val icon = it.icon
                if (icon != null) {
                    icon.mutate()
                    icon.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
                }
            }
        }
    }
}