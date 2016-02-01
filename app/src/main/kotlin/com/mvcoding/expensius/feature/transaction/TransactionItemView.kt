package com.mvcoding.expensius.feature.transaction

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mvcoding.expensius.R
import com.mvcoding.expensius.extension.provideSingleton
import com.mvcoding.expensius.feature.AmountFormatter
import org.joda.time.DateTime

class TransactionItemView : LinearLayout {
    private val amountFormatter by lazy { provideSingleton(AmountFormatter::class) }

    private val monthTextView by lazy { findViewById(R.id.monthTextView) as TextView }
    private val dayTextView by lazy { findViewById(R.id.dayTextView) as TextView }
    private val noteTextView by lazy { findViewById(R.id.noteTextView) as TextView }
    private val tagsTextView by lazy { findViewById(R.id.tagsTextView) as TextView }
    private val amountTextView by lazy { findViewById(R.id.amountTextView) as TextView }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(parent: ViewGroup) = LayoutInflater.from(parent.context).inflate(R.layout.item_view_transaction, parent, false) as TransactionItemView
    }

    fun setTransaction(transaction: Transaction) {
        val dateTime = DateTime(transaction.timestamp)
        monthTextView.text = dateTime.monthOfYear().asShortText
        dayTextView.text = dateTime.dayOfMonth().asShortText
        noteTextView.text = transaction.note
        tagsTextView.text = transaction.tags.joinToString { it.title }
        amountTextView.text = amountFormatter.format(transaction.amount, transaction.currency)
    }
}