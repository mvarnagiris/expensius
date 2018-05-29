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
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.getColorFromTheme
import com.mvcoding.expensius.extension.getString
import com.mvcoding.expensius.model.AccountIncomeExpense
import com.mvcoding.expensius.model.RealAccount
import com.mvcoding.expensius.model.TotalAccount
import com.mvcoding.expensius.provideMoneyFormatter
import kotlinx.android.synthetic.main.balance_view.view.*

class BalanceView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        MaterialCardView(context, attrs, defStyleAttr) {

    private val moneyFormatter by lazy { provideMoneyFormatter() }

    fun initialize(accountIncomeExpense: AccountIncomeExpense) {
        val account = accountIncomeExpense.account
        setCardBackgroundColor(when (account) {
            is TotalAccount -> getColorFromTheme(R.attr.colorBackgroundPrimary)
            is RealAccount -> account.color.rgb
        })
        titleTextView.text = when (account) {
            is TotalAccount -> getString(R.string.total_balance)
            is RealAccount -> account.title.text
        }
        balanceTextView.text = moneyFormatter.format(account.balance)
        expensesTextView.text = moneyFormatter.format(accountIncomeExpense.expenses)
        incomeTextView.text = moneyFormatter.format(accountIncomeExpense.income)
    }
}