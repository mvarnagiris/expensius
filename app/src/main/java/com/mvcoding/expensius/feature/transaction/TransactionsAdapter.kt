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

package com.mvcoding.expensius.feature.transaction

//class TransactionsAdapter(private val isClickable: Boolean = true) : BaseClickableAdapter<Transaction, ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, clickSubject: PublishSubject<Pair<View, Int>>) =
//            if (isClickable) ClickableViewHolder(TransactionItemView.inflate(parent), clickSubject)
//            else ViewHolder(TransactionItemView.inflate(parent).apply { background = null })
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.getView<TransactionItemView>().setTransaction(getItem(position))
//    }
//}