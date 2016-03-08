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
import com.mvcoding.expensius.feature.Paging
import com.mvcoding.expensius.feature.PagingOnScrollListener
import com.mvcoding.expensius.feature.calculator.CalculatorActivity
import com.mvcoding.expensius.model.Transaction
import rx.Observable
import rx.Observable.never
import rx.lang.kotlin.PublishSubject

class TransactionsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        RecyclerView(context, attrs, defStyleAttr), TransactionsPresenter.View, PagingOnScrollListener.PagingListener {

    private val PAGING_THRESHOLD = 5

    private val transactionsAdapter = TransactionsAdapter()
    private val paging = PagingOnScrollListener(this, Paging(PAGING_THRESHOLD))
    private val pagingSubject = PublishSubject<TransactionsPresenter.PagingEdge>()

    private lateinit var presenter: TransactionsPresenter
    private lateinit var createTransactionObservable: Observable<Unit>

    override fun onFinishInflate() {
        super.onFinishInflate()
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        itemAnimator = DefaultItemAnimator()
        addItemDecoration(DividerItemDecoration(
                getColorFromTheme(context, R.attr.colorDivider),
                resources.getDimensionPixelSize(R.dimen.divider)))
        addOnScrollListener(paging)
        adapter = transactionsAdapter
    }

    fun init(modelDisplayType: ModelDisplayType, createTransactionObservable: Observable<Unit>) {
        this.createTransactionObservable = createTransactionObservable
        this.presenter = provideActivityScopedSingleton(TransactionsPresenter::class, modelDisplayType)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onViewAttached(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached(this)
    }

    override fun onTransactionSelected() = transactionsAdapter.itemPositionClicks().map { transactionsAdapter.getItem(it) }
    override fun onCreateTransaction() = createTransactionObservable
    override fun onDisplayArchivedTransactions() = never<Unit>() // TODO Implement
    override fun onPagingEdgeReached() = pagingSubject

    override fun showModelDisplayType(modelDisplayType: ModelDisplayType) {
        // TODO Implement
    }

    override fun showTransactions(transactions: List<Transaction>) {
        transactionsAdapter.setItems(transactions)
        paging.reset()
    }

    override fun addTransactions(transactions: List<Transaction>, position: Int) {
        transactionsAdapter.insert(transactions, position)
    }

    override fun displayCreateTransaction() {
        CalculatorActivity.start(context)
    }

    override fun displayTransactionEdit(transaction: Transaction) {
        TransactionActivity.start(context, transaction)
    }

    override fun displayArchivedTransactions() {
        TransactionsActivity.startArchived(context)
    }

    override fun onEnteredPagingArea(pagingEdge: TransactionsPresenter.PagingEdge) {
        pagingSubject.onNext(pagingEdge)
    }

    override fun onLeftPagingArea(pagingEdge: TransactionsPresenter.PagingEdge) {
    }
}