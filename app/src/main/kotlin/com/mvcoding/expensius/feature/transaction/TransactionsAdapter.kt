package com.mvcoding.expensius.feature.transaction

import android.view.View
import android.view.ViewGroup
import com.mvcoding.expensius.feature.BaseClickableAdapter
import com.mvcoding.expensius.feature.ClickableViewHolder
import com.mvcoding.expensius.model.Transaction
import rx.subjects.PublishSubject

class TransactionsAdapter : BaseClickableAdapter<Transaction, ClickableViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, clickSubject: PublishSubject<Pair<View, Int>>) =
            ClickableViewHolder(TransactionItemView.inflate(parent), clickSubject)

    override fun onBindViewHolder(holder: ClickableViewHolder, position: Int) {
        holder.getView<TransactionItemView>().setTransaction(getItem(position))
    }
}