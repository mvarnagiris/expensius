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

package com.mvcoding.expensius.feature.premium

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.inflate
import com.mvcoding.expensius.extension.snackbar
import com.mvcoding.expensius.extension.toBaseActivity
import com.mvcoding.expensius.feature.BaseClickableAdapter
import com.mvcoding.expensius.feature.ClickableViewHolder
import com.mvcoding.expensius.feature.Error
import kotlinx.android.synthetic.main.view_premium.view.*
import rx.subjects.PublishSubject

class PremiumView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), PremiumPresenter.View {

    private val REQUEST_BILLING = 1

    private val presenter by lazy { providePremiumPresenter() }
    private val billingFlow by lazy { provideBillingFlow() }
    private val adapter by lazy { Adapter() }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = billingFlow.onActivityResult(requestCode, resultCode, data)

    override fun onFinishInflate() {
        super.onFinishInflate()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        doNotInEditMode { recyclerView.adapter = adapter }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.attach(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detach(this)
    }

    override fun showFreeUser() = subscriptionTextView.setText(R.string.long_user_is_using_free_version)
    override fun showPremiumUser() = subscriptionTextView.setText(R.string.long_user_is_using_premium_version)
    override fun onRefresh() = swipeRefreshLayout.refreshes()
    override fun onBillingProductSelected() = adapter.itemPositionClicks().map { adapter.getItem(it) }
    override fun showBillingProducts(billingProducts: List<BillingProduct>) = adapter.set(billingProducts)
    override fun displayBuyProcess(productId: String) = billingFlow.startPurchase(context.toBaseActivity(), REQUEST_BILLING, productId)

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showEmptyView() {
        emptyTextView.visibility = VISIBLE
    }

    override fun hideEmptyView() {
        emptyTextView.visibility = GONE
    }

    override fun showError(error: Error) {
        snackbar(R.string.error_refreshing_purchases, Snackbar.LENGTH_LONG).show()
    }

    private class Adapter : BaseClickableAdapter<BillingProduct, ClickableViewHolder<View>>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, positionClickedSubject: PublishSubject<Int>) =
                ClickableViewHolder<View>(parent.inflate(R.layout.item_view_billing_product), positionClickedSubject)

        override fun onBindViewHolder(holder: ClickableViewHolder<View>, position: Int) {
            (holder.view.findViewById(R.id.titleTextView) as TextView).text = getItem(position).title
            (holder.view.findViewById(R.id.priceTextView) as TextView).text = getItem(position).price
        }
    }
}