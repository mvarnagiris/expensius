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

import android.animation.ArgbEvaluator
import android.content.Context
import android.os.Bundle
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
import com.mvcoding.expensius.feature.settings.SettingsView
import com.mvcoding.expensius.feature.tag.TagsView
import com.mvcoding.expensius.feature.transaction.TransactionsView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            ActivityStarter(context, MainActivity::class).start()
        }
    }

    private val FAB_ANIMATION_DURATION = 100L
    private val argbEvaluator = ArgbEvaluator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val defaultTabColor = getColorFromTheme(tabLayout.context, R.attr.colorActionIcon)
        val selectedTabColor = getColorFromTheme(tabLayout.context, R.attr.colorAccent)
        val screens = listOf(
                Screen(reportsInflater(), R.drawable.ic_action_reports),
                Screen(transactionsInflater(), R.drawable.ic_action_transactions),
                Screen(tagsInflater(), R.drawable.ic_action_tag),
                Screen(settingsInflater(), R.drawable.ic_action_settings)
        )

        viewPager.adapter = ScreensAdapter(screens)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.forEachTabIndexed { index, tab ->
            tab.icon = ContextCompat.getDrawable(this, screens[index].iconResId).mutate().apply {
                DrawableCompat.setTint(this, defaultTabColor)
            }
        }
        viewPager.pageSelections().subscribe { position ->
            updateFloatingActionButtons(position)
        }
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                tabLayout.forEachTabIndexed { tabPosition, tab ->
                    val color = when (tabPosition) {
                        position -> argbEvaluator.evaluate(positionOffset, selectedTabColor, defaultTabColor) as Int
                        position + 1 -> argbEvaluator.evaluate(positionOffset, defaultTabColor, selectedTabColor) as Int
                        else -> defaultTabColor
                    }
                    tab.icon?.let { DrawableCompat.setTint(it, color) }
                }
            }
        })
    }

    private fun reportsInflater() = {
        layoutInflater: LayoutInflater, parent: ViewGroup ->
        parent.inflate<View>(R.layout.view_reports)
    }

    private fun transactionsInflater() = {
        layoutInflater: LayoutInflater, parent: ViewGroup ->
        parent.inflate<TransactionsView>(R.layout.view_transactions).apply {
            init(VIEW_NOT_ARCHIVED, addTransactionFloatingActionButton.clicks())
        }
    }

    private fun tagsInflater() = {
        layoutInflater: LayoutInflater, parent: ViewGroup ->
        parent.inflate<TagsView>(R.layout.view_tags).apply {
            init(VIEW_NOT_ARCHIVED, addTagFloatingActionButton.clicks())
        }
    }

    private fun settingsInflater() = {
        layoutInflater: LayoutInflater, parent: ViewGroup ->
        parent.inflate<SettingsView>(R.layout.view_settings)
    }

    private fun updateFloatingActionButtons(position: Int) {
        when (position) {
            0 -> showTransactionFab()
            1 -> showTransactionFab()
            2 -> showTagFab()
            3 -> hideFab()
        }
    }

    private fun showTransactionFab() {
        if (addTransactionFloatingActionButton.visibility == VISIBLE) return

        addTagFloatingActionButton.animate().setDuration(FAB_ANIMATION_DURATION).scaleX(0f).scaleY(0f).withEndAction {
            addTransactionFloatingActionButton.scaleX = 0f;
            addTransactionFloatingActionButton.scaleY = 0f;
            addTransactionFloatingActionButton.visibility = VISIBLE
            addTransactionFloatingActionButton.animate().scaleX(1f).scaleY(1f)
            addTagFloatingActionButton.visibility = GONE
        }
    }

    private fun showTagFab() {
        if (addTagFloatingActionButton.visibility == VISIBLE) return

        addTransactionFloatingActionButton.animate().setDuration(FAB_ANIMATION_DURATION).scaleX(0f).scaleY(0f).withEndAction {
            addTagFloatingActionButton.scaleX = 0f;
            addTagFloatingActionButton.scaleY = 0f;
            addTagFloatingActionButton.visibility = VISIBLE
            addTagFloatingActionButton.animate().scaleX(1f).scaleY(1f)
            addTransactionFloatingActionButton.visibility = GONE
        }
    }

    private fun hideFab() {
        if (addTransactionFloatingActionButton.visibility != GONE) {
            addTransactionFloatingActionButton.animate().setDuration(FAB_ANIMATION_DURATION).scaleX(0f).scaleY(0f).withEndAction {
                addTransactionFloatingActionButton.visibility = GONE
            }
        }

        if (addTagFloatingActionButton.visibility != GONE) {
            addTagFloatingActionButton.animate().setDuration(FAB_ANIMATION_DURATION).scaleX(0f).scaleY(0f).withEndAction {
                addTagFloatingActionButton.visibility = GONE
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