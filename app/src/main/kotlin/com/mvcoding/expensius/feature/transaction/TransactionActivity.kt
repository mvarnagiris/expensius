/*
 * Copyright (C) 2015 Mantas Varnagiris.
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

package com.mvcoding.expensius.feature.transaction

import android.content.Context
import android.os.Bundle
import com.mvcoding.expensius.R
import com.mvcoding.expensius.feature.ActivityStarter
import com.mvcoding.expensius.feature.BaseActivity

class TransactionActivity : BaseActivity() {
    companion object {
        private const val EXTRA_TRANSACTION = "EXTRA_TRANSACTION"

        fun start(context: Context, transaction: Transaction) {
            ActivityStarter(context, TransactionActivity::class)
                    .extra(EXTRA_TRANSACTION, transaction)
                    .start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_transaction)

        val transaction = intent.getSerializableExtra(EXTRA_TRANSACTION) as Transaction
        val transactionView = findViewById(R.id.transactionView) as TransactionView
        transactionView.init(transaction)
    }
}