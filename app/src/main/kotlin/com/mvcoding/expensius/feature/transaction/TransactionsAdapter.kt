package com.mvcoding.expensius.feature.transaction

import android.view.ViewGroup
import com.mvcoding.expensius.feature.BaseClickableAdapter
import com.mvcoding.expensius.feature.ClickableViewHolder
import com.mvcoding.expensius.model.Transaction
import rx.subjects.PublishSubject

class TransactionsAdapter : BaseClickableAdapter<Transaction, ClickableViewHolder<TransactionItemView>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, positionClickedSubject: PublishSubject<Int>) =
            ClickableViewHolder(TransactionItemView.inflate(parent), positionClickedSubject)

    override fun onBindViewHolder(holder: ClickableViewHolder<TransactionItemView>, position: Int) {
        holder.view.setTransaction(getItem(position))
    }
}