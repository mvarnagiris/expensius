/*
 * Copyright (C) 2016 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.GONE
import android.support.v4.view.ViewPager.VISIBLE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding.support.v4.view.pageSelections
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.forEachTabIndexed
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.inflate
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.tag.TagsView
import com.mvcoding.expensius.feature.transaction.TransactionsView

class MainActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            ActivityStarter(context, MainActivity::class).start()
        }
    }

    private val addTransactionFloatingActionButton by lazy { findViewById(R.id.addTransactionFloatingActionButton) }
    private val addTagFloatingActionButton by lazy { findViewById(R.id.addTagFloatingActionButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager = findViewById(R.id.viewPager) as ViewPager
        val tabLayout = findViewById(R.id.tabLayout) as TabLayout

        val defaultTabColor = getColorFromTheme(tabLayout.context, R.attr.colorActionIcon)
        val selectedTabColor = getColorFromTheme(tabLayout.context, R.attr.colorAccent)
        val screens = listOf(
                Screen(transactionsInflater(), R.drawable.ic_navigation_transactions),
                Screen(tagsInflater(), R.drawable.ic_navigation_tags)
        )

        viewPager.adapter = ScreensAdapter(screens)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.forEachTabIndexed { index, tab ->
            val icon = ContextCompat.getDrawable(this, screens[index].iconResId).mutate()
            DrawableCompat.setTint(icon, getColorFromTheme(tabLayout.context, R.attr.colorActionIcon))
            tab.icon = icon
        }
        viewPager.pageSelections().subscribe { position ->
            updateFloatingActionButtons(position)
            tabLayout.forEachTabIndexed { tabIndex, tab ->
                tab.icon?.let { DrawableCompat.setTint(it, if (tabIndex == position) selectedTabColor else defaultTabColor) }
            }
        }
    }

    private fun transactionsInflater() = {
        layoutInflater: LayoutInflater, parent: ViewGroup ->
        val transactionsView = parent.inflate<TransactionsView>(R.layout.view_transactions)
        transactionsView.init(VIEW_NOT_ARCHIVED, addTransactionFloatingActionButton.clicks())
        transactionsView
    }

    private fun tagsInflater() = {
        layoutInflater: LayoutInflater, parent: ViewGroup ->
        val tagsView = parent.inflate<TagsView>(R.layout.view_tags)
        tagsView.init(VIEW_NOT_ARCHIVED, addTagFloatingActionButton.clicks())
        tagsView
    }

    private fun updateFloatingActionButtons(position: Int) {
        when (position) {
            0 -> addTagFloatingActionButton.animate().setDuration(100).scaleX(0f).scaleY(0f).withEndAction {
                addTransactionFloatingActionButton.scaleX = 0f;
                addTransactionFloatingActionButton.scaleY = 0f;
                addTransactionFloatingActionButton.visibility = VISIBLE
                addTransactionFloatingActionButton.animate().scaleX(1f).scaleY(1f)
                addTagFloatingActionButton.visibility = GONE
            }
            1 -> addTransactionFloatingActionButton.animate().setDuration(100).scaleX(0f).scaleY(0f).withEndAction {
                addTagFloatingActionButton.scaleX = 0f;
                addTagFloatingActionButton.scaleY = 0f;
                addTagFloatingActionButton.visibility = VISIBLE
                addTagFloatingActionButton.animate().scaleX(1f).scaleY(1f)
                addTransactionFloatingActionButton.visibility = GONE
            }
        }
    }

    private data class Screen(val inflate: (LayoutInflater, ViewGroup) -> View, val iconResId: Int)

    private class ScreensAdapter(private val screens: List<Screen>) : PagerAdapter() {
        override fun getCount() = screens.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val screen = screens[position]
            val view = screen.inflate.invoke(LayoutInflater.from(container.context), container)
            container.addView(view)
            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            if (obj is View) {
                container.removeView(obj)
            }
        }

        override fun isViewFromObject(view: View?, obj: Any?) = view === obj
    }
}