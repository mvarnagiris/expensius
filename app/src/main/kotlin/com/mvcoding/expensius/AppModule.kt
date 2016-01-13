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
import com.mvcoding.expensius.cache.DatabaseTagsProvider
import com.mvcoding.expensius.cache.database.Database
import com.mvcoding.expensius.cache.database.SqliteDatabase
import com.mvcoding.expensius.cache.database.table.TagsTable
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.feature.AmountFormatter
import com.mvcoding.expensius.feature.CurrencyFormat
import com.mvcoding.expensius.feature.CurrencyFormat.DecimalSeparator.DOT
import com.mvcoding.expensius.feature.CurrencyFormat.GroupSeparator.COMMA
import com.mvcoding.expensius.feature.CurrencyFormat.SymbolDistance.CLOSE
import com.mvcoding.expensius.feature.CurrencyFormat.SymbolPosition.START
import com.mvcoding.expensius.feature.DateFormatter
import com.mvcoding.expensius.feature.tag.TagsProvider
import com.mvcoding.expensius.feature.transaction.Currency
import com.mvcoding.expensius.feature.transaction.Transaction
import com.mvcoding.expensius.feature.transaction.TransactionsProvider
import com.mvcoding.expensius.paging.Page
import com.mvcoding.expensius.paging.PageResult
import com.squareup.sqlbrite.SqlBrite
import rx.Observable
import rx.Observable.empty
import java.math.BigDecimal

class AppModule(val context: Context) : ShankModule {
    init {
        registerFactory<Context>(Context::class.java, { context })
    }

    override fun registerFactories() {
        registerFactory<Settings>(Settings::class.java, { UserSettings() })
        registerFactory<Session>(Session::class.java, { UserSession() })
        database()
        tagsCache()
        transactionsCache()
        amountFormatter()
        dateFormatter()
    }

    private fun database() {
        val briteDatabase = SqlBrite.create().wrapDatabaseHelper(DBHelper(context, TagsTable()))
        registerFactory<Database>(Database::class.java, { SqliteDatabase(briteDatabase) })
    }

    private fun tagsCache() {
        val database = provideSingleton(Database::class)
        registerFactory<TagsProvider>(TagsProvider::class.java, { DatabaseTagsProvider(database, TagsTable()) })
    }

    private fun transactionsCache() {
        // TODO: This is temporary
        registerFactory<TransactionsProvider>(TransactionsProvider::class.java, {
            object : TransactionsProvider {
                override fun transactions(pages: Observable<Page>): Observable<PageResult<Transaction>> {
                    return empty()
                }

                override fun save(transactions: Set<Transaction>) {
                }
            }
        })
    }

    private fun amountFormatter() {
        // TODO: This is temporary
        registerFactory<AmountFormatter>(AmountFormatter::class.java, {
            object : AmountFormatter {
                private val currencyFormat = CurrencyFormat("£", START, CLOSE, DOT, COMMA, 2, 2)

                override fun format(amount: BigDecimal, currency: Currency) = currencyFormat.format(amount)
            }
        })
    }

    private fun dateFormatter() {
        registerFactory<DateFormatter>(DateFormatter::class.java, { DateFormatter(context) })
    }
}