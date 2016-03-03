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

import com.mvcoding.expensius.Settings
import com.mvcoding.expensius.feature.transaction.Currency
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Test

class SettingsPresenterTest {
    val settings = mock<Settings>()
    val view = mock<SettingsPresenter.View>()
    val presenter = SettingsPresenter(settings)

    @Test
    fun showsMainCurrency() {
        val mainCurrency = Currency("GBP")
        whenever(settings.mainCurrency).thenReturn(mainCurrency)

        presenter.onViewAttached(view)

        verify(view).showMainCurrency(mainCurrency)
    }
}