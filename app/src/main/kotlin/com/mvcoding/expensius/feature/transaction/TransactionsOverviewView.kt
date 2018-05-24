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

package com.mvcoding.expensius.feature.transaction

//class TransactionsOverviewView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
//        LinearLayout(context, attrs, defStyleAttr), TransactionsOverviewPresenter.View {
//
//    private val presenter by lazy { provideTransactionsOverviewPresenter() }
//    private val adapter by lazy { TransactionsAdapter(isClickable = false) }
//
//    override fun onFinishInflate() {
//        super.onFinishInflate()
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = adapter
//    }
//
//    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean = true
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        doNotInEditMode { presenter.attach(this) }
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        presenter.detach(this)
//    }
//
//    override fun showTransactions(transactions: List<Transaction>): Unit = adapter.setItems(transactions)
//    override fun showEmptyView(): Unit = emptyTextView.setVisible()
//    override fun hideEmptyView(): Unit = emptyTextView.setGone()
//}