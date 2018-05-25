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
import com.mvcoding.mvp.Presenter
import com.mvcoding.mvp.RxSchedulers
import com.mvcoding.mvp.views.LoadingView
import io.reactivex.Observable

class BalancePresenter(
        private val getBalance: () -> Observable<Money>,
        private val schedulers: RxSchedulers) : Presenter<BalancePresenter.View>() {

    override fun onViewAttached(view: View) {
        super.onViewAttached(view)

        view.showLoading()
        getBalance()
                .subscribeOn(schedulers.io)
                .observeOn(schedulers.main)
                .doOnNext { view.hideLoading() }
                .subscribeUntilDetached { view.showBalance(it) }
    }

    interface View : Presenter.View, LoadingView {
        fun showBalance(balance: Money)
    }
}