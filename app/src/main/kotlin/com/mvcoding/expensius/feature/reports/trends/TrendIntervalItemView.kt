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

package com.mvcoding.expensius.feature.reports.trends

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import com.mvcoding.expensius.model.GroupedMoney
import com.mvcoding.expensius.provideDateFormatter
import com.mvcoding.expensius.provideMoneyFormatter
import kotlinx.android.synthetic.main.item_view_trend_interval.view.*
import org.joda.time.Interval
import java.math.BigDecimal

class TrendIntervalItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    private val moneyFormatter by lazy { provideMoneyFormatter() }
    private val dateFormatter by lazy { provideDateFormatter() }

    fun initialize(groupedMoney: GroupedMoney<Interval>) {
        amountTextView.text = moneyFormatter.format(groupedMoney.money)
        amountTextView.isEnabled = groupedMoney.money.amount > BigDecimal.ZERO
        intervalTextView.text = dateFormatter.formatInterval(groupedMoney.group)
    }
}