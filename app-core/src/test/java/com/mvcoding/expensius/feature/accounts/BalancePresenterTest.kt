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

import com.mvcoding.expensius.model.Money
import com.mvcoding.mvp.trampolines
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import ro.kreator.aRandom

class BalancePresenterTest {

    private val balance by aRandom<Money>()
    private val updatedBalance by aRandom<Money>()

    private val getBalance = mock<() -> Observable<Money>>()
    private val view = mock<BalancePresenter.View>()
    private val presenter = BalancePresenter(getBalance, trampolines)

    @Before
    fun setUp() {
        whenever(getBalance()).thenReturn(Observable.fromIterable(listOf(balance, updatedBalance)))
    }

    @Test
    fun `shows balance and updates it when it changes`() {
        presenter attach view

        inOrder(view) {
            verify(view).showLoading()
            verify(view).hideLoading()
            verify(view).showBalance(balance)
            verify(view).showBalance(updatedBalance)
        }
    }
}