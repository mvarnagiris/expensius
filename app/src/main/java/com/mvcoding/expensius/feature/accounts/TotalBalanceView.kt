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

package com.mvcoding.expensius.feature.accounts

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.Money
import com.mvcoding.expensius.provideMoneyFormatter
import kotlinx.android.synthetic.main.total_balance_view.view.*
import java.math.BigDecimal

class TotalBalanceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        MaterialCardView(context, attrs, defStyleAttr) {

    private val moneyFormatter by lazy { provideMoneyFormatter() }

    override fun onFinishInflate() {
        super.onFinishInflate()

        balanceTextView.text = moneyFormatter.format(Money(BigDecimal(35476.87), Currency("GBP")))
        expensesTextView.text = moneyFormatter.format(Money(BigDecimal(1254.76), Currency("GBP")))
    }
}