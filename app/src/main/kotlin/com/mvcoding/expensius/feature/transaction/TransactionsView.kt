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
import android.widget.LinearLayout
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import rx.Observable.just

class TransactionsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), TransactionsPresenter.View {

    private val presenter by lazy { provideActivityScopedSingleton(TransactionsPresenter::class, context) }
    private val recyclerView by lazy { findViewById(R.id.recyclerView) as RecyclerView }
    private val adapter = TransactionsAdapter()

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapter
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.onAttachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onDetachView(this)
    }

    override fun onPagingEdgeReached() = just(TransactionsPresenter.PagingEdge.END)

    override fun showTransactions(transactions: List<Transaction>) {
        adapter.setItems(transactions)
    }

    override fun addTransactions(transactions: List<Transaction>, position: Int) {
        adapter.insert(transactions, position)
    }
}