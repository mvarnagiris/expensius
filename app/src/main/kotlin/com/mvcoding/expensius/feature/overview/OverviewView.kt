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
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.activity
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.feature.settings.SettingsActivity
import com.mvcoding.expensius.feature.tag.TagsActivity
import com.mvcoding.expensius.feature.transaction.TransactionsActivity
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.view_overview.view.*
import net.danlew.android.joda.DateUtils
import org.joda.time.Interval
import rx.Observable
import rx.Observable.never

class OverviewView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        CoordinatorLayout(context, attrs, defStyleAttr), OverviewPresenter.View {

    private val presenter by lazy { provideOverviewPresenter() }
    private val toolbarClicks by lazy { toolbar.itemClicks().share() }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun createTransactionSelects() = createTransactionFloatingActionButton.clicks()
    override fun transactionsSelects(): Observable<Unit> = never<Unit>() // TODO: Implement
    override fun tagsSelects(): Observable<Unit> = toolbarClicks.filter { it.itemId == R.id.action_tags }.map { Unit }
    override fun settingsSelects(): Observable<Unit> = toolbarClicks.filter { it.itemId == R.id.action_settings }.map { Unit }

    override fun showInterval(interval: Interval) {
        val start = DateUtils.formatDateTime(context, interval.start, DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_DATE)
        val end = DateUtils.formatDateTime(context, interval.end.minusMillis(1), DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_DATE)
        activity().supportActionBar?.title = "$start - $end"
    }

    override fun displayCreateTransaction() = CalculatorActivity.start(context)
    override fun displayTransactions() = TransactionsActivity.start(context)
    override fun displayTags() = TagsActivity.startView(context)
    override fun displaySettings() = SettingsActivity.start(context)
}