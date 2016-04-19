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
import android.support.design.widget.Snackbar
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.jakewharton.rxbinding.support.v4.widget.refreshes
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.extension.snackbar
import com.mvcoding.expensius.feature.Error
import kotlinx.android.synthetic.main.view_premium.view.*

class PremiumView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        LinearLayout(context, attrs, defStyleAttr), PremiumPresenter.View {

    private val presenter by lazy { provideActivityScopedSingleton(PremiumPresenter::class) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.onViewAttached(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached(this)
    }

    override fun showFreeUser() = subscriptionTextView.setText(R.string.long_user_is_using_free_version)
    override fun showPremiumUser() = subscriptionTextView.setText(R.string.long_user_is_using_premium_version)
    override fun onRefresh() = swipeRefreshLayout.refreshes()

    override fun showBillingProducts(billingProducts: List<BillingProduct>) {
        Log.d("DEBUG", billingProducts.toString())
    }

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
}