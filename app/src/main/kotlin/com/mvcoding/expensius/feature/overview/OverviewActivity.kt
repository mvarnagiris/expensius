/*
 * Copyright (C) 2017 Mantas Varnagiris.
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
import com.jakewharton.rxbinding.support.v7.widget.itemClicks
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.feature.reports.tags.TagsReportActivity
import com.mvcoding.expensius.feature.reports.trends.TrendsReportActivity
import com.mvcoding.expensius.feature.settings.SettingsActivity
import com.mvcoding.expensius.feature.tag.TagsActivity
import com.mvcoding.expensius.feature.transaction.TransactionsActivity
import com.mvcoding.expensius.model.ReportPeriod
import com.mvcoding.expensius.provideDateFormatter
import kotlinx.android.synthetic.main.activity_overview.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_tags_report_overview.*
import kotlinx.android.synthetic.main.view_transactions_overview.*
import kotlinx.android.synthetic.main.view_trend_report.*
import org.joda.time.Interval
import rx.Observable

class OverviewActivity : BaseActivity(), OverviewPresenter.View {

    companion object {
        fun start(context: Context) = ActivityStarter(context, OverviewActivity::class).start()
    }

    private val presenter by lazy { provideOverviewPresenter() }
    private val dateFormatter by lazy { provideDateFormatter() }
    private val toolbarClicks by lazy { toolbar.itemClicks().share() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        removeUpArrowFromToolbar()
        presenter.attach(this)
    }

    override fun onDestroy() {
        presenter.detach(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.overview, menu)
        return true
    }

    override fun createTransactionSelects(): Observable<Unit> = createTransactionButton.clicks()
    override fun transactionsSelects(): Observable<Unit> = transactionsOverviewView.clicks()
    override fun tagsSelects(): Observable<Unit> = toolbarClicks.filter { it.itemId == R.id.action_tags }.map { Unit }
    override fun trendsReportSelects(): Observable<Unit> = trendsReportView.clicks()
    override fun tagsReportSelects(): Observable<Unit> = tagsReportOverviewView.clicks()
    override fun settingsSelects(): Observable<Unit> = toolbarClicks.filter { it.itemId == R.id.action_settings }.map { Unit }
    override fun showInterval(interval: Interval, reportPeriod: ReportPeriod) = with(supportActionBar) { title = dateFormatter.formatInterval(reportPeriod, interval) }
    override fun displayCreateTransaction(): Unit = CalculatorActivity.start(this)
    override fun displayTransactions(): Unit = TransactionsActivity.start(this)
    override fun displayTags(): Unit = TagsActivity.startView(this)
    override fun displayTrendsReport(): Unit = TrendsReportActivity.start(this)
    override fun displayTagsReport(): Unit = TagsReportActivity.start(this)
    override fun displaySettings(): Unit = SettingsActivity.start(this)

    private fun removeUpArrowFromToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        val keyline = resources.getDimensionPixelSize(R.dimen.keyline)
        toolbar.setContentInsetsRelative(keyline, keyline)
    }
}