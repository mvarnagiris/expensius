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
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.feature.DividerItemDecoration
import com.mvcoding.expensius.feature.ModelDisplayType
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.model.Transaction
import rx.Observable
import rx.Observable.just
import rx.Observable.never

class TransactionsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        RecyclerView(context, attrs, defStyleAttr), TransactionsPresenter.View {
    lateinit var createTransactionObservable: Observable<Unit>

    private val presenter by lazy { provideActivityScopedSingleton(TransactionsPresenter::class) }
    private val transactionsAdapter = TransactionsAdapter()

    override fun onFinishInflate() {
        super.onFinishInflate()
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        itemAnimator = DefaultItemAnimator()
        addItemDecoration(DividerItemDecoration(
                getColorFromTheme(context, R.attr.colorDivider),
                resources.getDimensionPixelSize(R.dimen.divider)))
        adapter = transactionsAdapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView(this)
    }

    override fun onAddNewTransaction() = createTransactionObservable

    override fun onDisplayArchivedTransactions() = never<Unit>() // TODO

    override fun onPagingEdgeReached() = just(TransactionsPresenter.PagingEdge.END)

    override fun showModelDisplayType(modelDisplayType: ModelDisplayType) {
        // TODO
    }

    override fun showTransactions(transactions: List<Transaction>) {
        transactionsAdapter.setItems(transactions)
    }

    override fun addTransactions(transactions: List<Transaction>, position: Int) {
        transactionsAdapter.insert(transactions, position)
    }

    override fun displayTransactionEdit() {
        CalculatorActivity.start(context)
    }

    override fun displayArchivedTransactions() {
        // TODO
    }
}