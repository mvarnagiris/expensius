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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.BaseFragment
import com.mvcoding.expensius.model.Currency
import com.mvcoding.expensius.model.Money
import com.mvcoding.expensius.provideMoneyFormatter
import kotlinx.android.synthetic.main.total_balance_fragment.*
import java.math.BigDecimal

class TotalBalanceFragment : BaseFragment() {

    private val moneyFormatter by lazy { provideMoneyFormatter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.total_balance_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        balanceTextView.text = moneyFormatter.format(Money(BigDecimal(35476.87), Currency("GBP")))
    }
}