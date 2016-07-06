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
import android.preference.PreferenceManager
import com.memoizrlabs.Shank.registerFactory
import com.memoizrlabs.ShankModule
import com.memoizrlabs.shankkotlin.provideGlobalSingleton
import com.memoizrlabs.shankkotlin.registerFactory
import com.mvcoding.expensius.feature.AmountFormatter
import com.mvcoding.expensius.feature.DateFormatter
import com.mvcoding.expensius.feature.currency.provideCurrencyFormatsProvider
import com.mvcoding.expensius.firebase.FirebaseAppUserService
import com.mvcoding.expensius.provider.database.DBHelper
import com.mvcoding.expensius.provider.database.Database
import com.mvcoding.expensius.provider.database.SqliteDatabase
import com.mvcoding.expensius.provider.database.table.TagsTable
import com.mvcoding.expensius.provider.database.table.TransactionTagsTable
import com.mvcoding.expensius.provider.database.table.TransactionsTable
import com.mvcoding.expensius.service.AppUserService
import com.mvcoding.expensius.service.LoginService
import com.squareup.sqlbrite.SqlBrite
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.schedulers.Schedulers.computation
import rx.schedulers.Schedulers.io

class AppModule(val context: Context) : ShankModule {

    override fun registerFactories() {
        appContext()
        rxSchedulers()
        firebaseAppUserService()
        rxBus()
        dateFormatter()
        settings()
        database()
        amountFormatter()
    }


    private fun appContext() = registerFactory(Context::class) { -> context }
    private fun rxSchedulers() = registerFactory(RxSchedulers::class) { -> RxSchedulers(mainThread(), io(), computation()) }
    private fun firebaseAppUserService() = registerFactory(FirebaseAppUserService::class) { -> FirebaseAppUserService() }
    private fun rxBus() = registerFactory(RxBus::class) { -> RxBus() }
    private fun dateFormatter() = registerFactory(DateFormatter::class) { -> DateFormatter(context) }

    private fun settings() {
        registerFactory(Settings::class.java, { -> UserSettings(PreferenceManager.getDefaultSharedPreferences(provideContext())) })
    }

    private fun database() {
        val briteDatabase = SqlBrite.create().wrapDatabaseHelper(DBHelper(
                context,
                TagsTable(),
                TransactionsTable(),
                TransactionTagsTable()),
                io())
        registerFactory(Database::class.java, { -> SqliteDatabase(briteDatabase) })
    }

    private fun amountFormatter() {
        registerFactory(AmountFormatter::class.java) { -> AmountFormatter(provideCurrencyFormatsProvider()) }
    }
}

fun provideContext() = provideGlobalSingleton<Context>()
fun provideRxSchedulers() = provideGlobalSingleton<RxSchedulers>()
fun provideRxBus() = provideGlobalSingleton<RxBus>()
fun provideSettings() = provideGlobalSingleton<Settings>()
fun provideDatabase() = provideGlobalSingleton<Database>()
fun provideDateFormatter() = provideGlobalSingleton<DateFormatter>()
fun provideAmountFormatter() = provideGlobalSingleton<AmountFormatter>()

fun provideAppUserService(): AppUserService = provideFirebaseAppUserService()
fun provideLoginService(): LoginService = provideFirebaseAppUserService()
private fun provideFirebaseAppUserService(): FirebaseAppUserService = provideGlobalSingleton()