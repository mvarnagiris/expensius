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

package com.mvcoding.expensius

import android.content.Context
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.provideNew
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.DateFormatter
import com.mvcoding.expensius.feature.Filter
import com.mvcoding.expensius.feature.MoneyFormatter
import com.mvcoding.expensius.feature.currency.provideCurrencyFormatsProvider
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers.computation
import rx.schedulers.Schedulers.io

class AppModule(val context: Context) : ShankModule {

    override fun registerFactories() {
        appContext()
        rxSchedulers()
        dateFormatter()
        moneyFormatter()
//        filter()
        rxBus()
    }

    private fun appContext() = registerFactory(Context::class) { -> context }
    private fun rxSchedulers() = registerFactory(RxSchedulers::class) { -> RxSchedulers(mainThread(), io(), computation()) }
    private fun dateFormatter() = registerFactory(DateFormatter::class) { -> DateFormatter(context) }
    private fun moneyFormatter() {
        registerFactory(MoneyFormatter::class.java) { -> MoneyFormatter(provideCurrencyFormatsProvider()) }
    }

    //    private fun filter() = registerFactory(Filter::class) { -> Filter(provideAppUserService(), provideTimestampProvider()) }
    private fun rxBus() = registerFactory(RxBus::class) { -> RxBus() }

}

fun provideContext() = provideGlobalSingleton<Context>()
fun provideRxSchedulers() = provideGlobalSingleton<RxSchedulers>()
fun provideDateFormatter() = provideGlobalSingleton<DateFormatter>()
fun provideMoneyFormatter() = provideGlobalSingleton<MoneyFormatter>()
fun provideFilter() = provideNew<Filter>()
fun provideRxBus() = provideGlobalSingleton<RxBus>()
fun provideTimestampProvider() = SystemTimestampProvider()
