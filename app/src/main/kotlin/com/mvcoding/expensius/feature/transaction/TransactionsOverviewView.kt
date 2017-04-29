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

package com.mvcoding.expensius.feature.transaction

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.model.Transaction
import kotlinx.android.synthetic.main.view_transactions_overview.view.*

class TransactionsOverviewView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), TransactionsOverviewPresenter.View {

    private val presenter by lazy { provideTransactionsOverviewPresenter() }
    private val adapter by lazy { TransactionsAdapter(isClickable = false) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = true

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun showTransactions(transactions: List<Transaction>): Unit = adapter.setItems(transactions)
}