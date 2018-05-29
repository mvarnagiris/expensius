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

import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.BaseFragment
import com.mvcoding.expensius.feature.SimpleAdapter
import com.mvcoding.expensius.model.*
import kotlinx.android.synthetic.main.balances_fragment.*
import java.math.BigDecimal

class BalancesFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.balances_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = SimpleAdapter<AccountIncomeExpense>(R.layout.balance_view) { viewHolder, accountIncomeExpense ->
            viewHolder.getView<BalanceView>().initialize(accountIncomeExpense)
        }.apply {
            setItems(listOf(
                    AccountIncomeExpense(
                            TotalAccount(Money(BigDecimal(35476.87), Currency("GBP"))),
                            Money(BigDecimal(876.13), Currency("GBP")),
                            Money(BigDecimal(1254.79), Currency("GBP"))),
                    AccountIncomeExpense(
                            TotalAccount(Money(BigDecimal(3578.44), Currency("EUR"))),
                            Money(BigDecimal(15.78), Currency("EUR")),
                            Money(BigDecimal(780.55), Currency("EUR"))),
                    AccountIncomeExpense(
                            RemoteAccount(
                                    AccountId("1"),
                                    Title("Monzo"),
                                    Color(parseColor("#F4434B")),
                                    Money(BigDecimal(586.10), Currency("GBP"))
                            ),
                            Money(BigDecimal(2578.11), Currency("GBP")),
                            Money(BigDecimal(1000.00), Currency("GBP"))),
                    AccountIncomeExpense(
                            RemoteAccount(
                                    AccountId("4"),
                                    Title("Revolut"),
                                    Color(parseColor("#774DB8")),
                                    Money(BigDecimal(15.89), Currency("GBP"))
                            ),
                            Money(BigDecimal(0.00), Currency("GBP")),
                            Money(BigDecimal(0.00), Currency("GBP"))),
                    AccountIncomeExpense(
                            RemoteAccount(
                                    AccountId("5"),
                                    Title("Revolut"),
                                    Color(parseColor("#774DB8")),
                                    Money(BigDecimal(89.00), Currency("EUR"))
                            ),
                            Money(BigDecimal(0.00), Currency("EUR")),
                            Money(BigDecimal(15.00), Currency("EUR"))),
                    AccountIncomeExpense(
                            RemoteAccount(
                                    AccountId("6"),
                                    Title("Revolut"),
                                    Color(parseColor("#774DB8")),
                                    Money(BigDecimal(0.0012248), Currency("BTC"))
                            ),
                            Money(BigDecimal(0.00), Currency("BTC")),
                            Money(BigDecimal(0.00), Currency("BTC"))),
                    AccountIncomeExpense(
                            RemoteAccount(
                                    AccountId("2"),
                                    Title("Lloyds Debit"),
                                    Color(parseColor("#257D45")),
                                    Money(BigDecimal(1988.41), Currency("GBP"))
                            ),
                            Money(BigDecimal(2118.05), Currency("GBP")),
                            Money(BigDecimal(5000.00), Currency("GBP"))),
                    AccountIncomeExpense(
                            RemoteAccount(
                                    AccountId("3"),
                                    Title("Lloyds Credit"),
                                    Color(parseColor("#257D45")),
                                    Money(BigDecimal(-547.54), Currency("GBP"))
                            ),
                            Money(BigDecimal(180.05), Currency("GBP")),
                            Money(BigDecimal(0.00), Currency("GBP")))))
        }
    }
}