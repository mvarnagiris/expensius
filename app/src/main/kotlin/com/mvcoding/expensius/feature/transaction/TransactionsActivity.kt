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

package com.mvcoding.expensius.feature.transaction

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity
import com.mvcoding.expensius.feature.DividerItemDecoration
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_ARCHIVED
import com.mvcoding.expensius.feature.ModelDisplayType.VIEW_NOT_ARCHIVED
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.model.Transaction
import kotlinx.android.synthetic.main.activity_transactions.*
import rx.Observable
import rx.Observable.empty

class TransactionsActivity : BaseActivity(), TransactionsPresenter.View {
    companion object {
        private const val EXTRA_DISPLAY_TYPE = "EXTRA_DISPLAY_TYPE"

        fun start(context: Context) = ActivityStarter(context, TransactionsActivity::class)
                .extra(EXTRA_DISPLAY_TYPE, VIEW_NOT_ARCHIVED)
                .start()

        fun startArchived(context: Context) = ActivityStarter(context, TransactionsActivity::class)
                .extra(EXTRA_DISPLAY_TYPE, VIEW_ARCHIVED)
                .start()
    }

    private val presenter by lazy { provideTransactionsPresenter(intent.getSerializableExtra(EXTRA_DISPLAY_TYPE) as ModelDisplayType) }
    private val adapter by lazy { TransactionsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(getColorFromTheme(R.attr.colorDivider), resources.getDimensionPixelSize(R.dimen.divider)))
        recyclerView.adapter = adapter

        presenter.attach(this)
    }

    override fun onDestroy() {
        presenter.detach(this)
        super.onDestroy()
    }

    override fun showModelDisplayType(modelDisplayType: ModelDisplayType) {
        supportActionBar?.title = if (modelDisplayType == VIEW_ARCHIVED) getString(R.string.archived_transactions) else getString(R.string.transactions)
    }

    override fun transactionSelects(): Observable<Transaction> = adapter.itemPositionClicks().map { adapter.getItem(it) }
    override fun archivedTransactionsRequests(): Observable<Unit> = empty()
    override fun createTransactionRequests(): Observable<Unit> = createTransactionFloatingActionButton.clicks()
    override fun showItems(items: List<Transaction>): Unit = adapter.set(items)
    override fun showAddedItems(position: Int, items: List<Transaction>): Unit = adapter.add(position, items)
    override fun showChangedItems(position: Int, items: List<Transaction>): Unit = adapter.change(position, items)
    override fun showRemovedItems(position: Int, items: List<Transaction>): Unit = adapter.remove(position, items.size)
    override fun showMovedItems(fromPosition: Int, toPosition: Int, items: List<Transaction>): Unit = adapter.move(fromPosition, toPosition)
    override fun showLoading(): Unit = with(progressBar) { visibility = View.VISIBLE }
    override fun hideLoading(): Unit = with(progressBar) { visibility = View.GONE }
    override fun displayTransactionEdit(transaction: Transaction): Unit = TransactionActivity.start(this, transaction)
    override fun displayCalculator(): Unit = CalculatorActivity.start(this)
    override fun displayArchivedTransactions(): Unit = TransactionsActivity.startArchived(this)
}