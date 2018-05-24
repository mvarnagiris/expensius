/*
 * Copyright (C) 2018 Mantas Varnagiris.
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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.memoizrlabs.Scope
import com.mvcoding.expensius.shank.Scoped
import java.io.Closeable
import java.util.*

private const val STATE_SCOPE_ID = "ACTIVITY_STATE_SCOPE_ID"

abstract class BaseActivity : AppCompatActivity(), Scoped {

    private lateinit var scopeUUID: UUID

    private val finalAction: (Any) -> Unit = {
        when (it) {
            is Closeable -> it.close()
        }
    }

    override val scope: Scope by lazy { Scope.scope(scopeUUID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scopeUUID = savedInstanceState?.getSerializable(STATE_SCOPE_ID) as UUID? ?: UUID.randomUUID()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(STATE_SCOPE_ID, scopeUUID)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            scope.clearWithFinalAction { finalAction(it) }
        }
    }

//
//    override fun setContentView(layoutResID: Int) {
//        super.setContentView(layoutResID)
//        setupToolbar()
//    }
//
//    override fun setContentView(view: View?) {
//        super.setContentView(view)
//        setupToolbar()
//    }
//
//    override fun setContentView(view: View?, params: ViewGroup.LayoutParams?) {
//        super.setContentView(view, params)
//        setupToolbar()
//    }
//
//    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
//        tintToolbarIcons(menu)
//        return super.onPrepareOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    protected fun setupToolbar() {
//        toolbar?.run {
//            setSupportActionBar(this)
//            supportActionBar?.run {
//                setDisplayHomeAsUpEnabled(true)
//                setHomeButtonEnabled(true)
//            }
//        }
//    }
//
//    private fun tintToolbarIcons(menu: Menu) {
//        toolbar?.apply {
//            val tintColor = getColorFromTheme(context, android.R.attr.textColorPrimary)
//            menu.forEach {
//                val icon = it.icon
//                if (icon != null) {
//                    icon.mutate()
//                    icon.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)
//                }
//            }
//        }
//    }
}