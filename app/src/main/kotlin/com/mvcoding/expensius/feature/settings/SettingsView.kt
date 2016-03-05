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

package com.mvcoding.expensius.feature.settings

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import com.jakewharton.rxbinding.view.clicks
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.doNotInEditMode
import com.mvcoding.expensius.extension.provideActivityScopedSingleton
import com.mvcoding.expensius.model.Currency
import kotlinx.android.synthetic.main.view_settings.view.*
import rx.Observable

class SettingsView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        NestedScrollView(context, attrs, defStyleAttr), SettingsPresenter.View {

    private val presenter by lazy { provideActivityScopedSingleton(SettingsPresenter::class) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        doNotInEditMode { presenter.onViewAttached(this) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.onViewDetached(this)
    }

    override fun onMainCurrencyRequested() = mainCurrencySettingsItemView.clicks()

    override fun requestMainCurrency(currencies: List<Currency>): Observable<Currency> {
        return Observable.empty() // TODO: Implement
    }

    override fun showMainCurrency(mainCurrency: Currency) {
        with(mainCurrencySettingsItemView as SettingsItemView) {
            setTitle(context.getString(R.string.main_currency))
            setSubtitle(mainCurrency.code)
        }
    }
}